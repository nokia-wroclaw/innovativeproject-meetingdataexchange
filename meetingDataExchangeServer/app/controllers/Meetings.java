package controllers;

import static models.public_.Tables.*;

import java.sql.Timestamp;

import models.DbSingleton;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Meetings extends Controller {
	
	public static Result createNew(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String title = json.findPath("title").textValue();
		String topic = json.findPath("topic").textValue();
		String abilityToSendFiles = json.findPath("abilityToSendFiles").textValue();
		
		return ok(web_createNew(login, sid, title, topic, abilityToSendFiles));
	}
	
	public static ObjectNode web_createNew(String login, String sid, String title, String topic, String abilityToSendFiles){
		if(login==null || title==null || topic==null || abilityToSendFiles==null || sid==null)
			return errorObject("incorrect data");
		
		boolean permit;
		if(!abilityToSendFiles.equals("true") && !abilityToSendFiles.equals("false"))
			return errorObject("incorrect data");
		else if (abilityToSendFiles.equals("true"))
			permit = true;
		else
			permit = false;
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		java.util.Date date= new java.util.Date();
		Record record = DbSingleton.getInstance().getDsl()
				.insertInto(MEETING,
						MEETING.TITLE, MEETING.TOPIC, MEETING.STARTTIME, MEETING.ABILITYTOSENDFILES, MEETING.AUTHORID)
				.values(title, topic, new Timestamp(date.getTime()), permit, Integer.parseInt(login))
				.returning(MEETING.ID)
				.fetchOne();
		
		int meetingId = record.getValue(MEETING.ID);
		
		DbSingleton.getInstance().getDsl()
				.insertInto(MEETINGUSER,
						MEETINGUSER.MEETINGID, MEETINGUSER.USERID, MEETINGUSER.JOINTIME)
				.values(meetingId, Integer.parseInt(login), new Timestamp(date.getTime()))
				.execute();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("meetingid", meetingId);
		return result;
	}
	
	public static Result stop(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String meetingId = json.findPath("meetingid").textValue();
		
		return ok(web_stop(login, sid, meetingId));
	}
	
	public static ObjectNode web_stop(String login, String sid, String meetingId){
		if(login==null || meetingId==null || sid==null)
			return errorObject("incorrect data");
		
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		if(!userIsA_Host(login, meetingId))
			return errorObject("access denied");
		
		if(meetingIsAlreadyFinish(meetingId))
			return errorObject("already finished");
		
		java.util.Date date= new java.util.Date();
		
		DbSingleton.getInstance().getDsl()
		.update(MEETING)
		.set(MEETING.ENDTIME, new Timestamp(date.getTime()))
		.where(MEETING.ID.equal(Integer.parseInt(meetingId))).execute();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return result;
	}
	
	public static Result edit(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String meetingId = json.findPath("meetingid").textValue();
		String abilityToSendFiles = json.findPath("abilityToSendFiles").textValue();
		String title = json.findPath("title").textValue();
		String topic = json.findPath("topic").textValue();
		
		return ok(web_edit(login, sid, meetingId, abilityToSendFiles, title, topic));
	}
	
	public static ObjectNode web_edit(String login, String sid, String meetingId, String abilityToSendFiles, String title, String topic){
		if(login==null || sid==null || meetingId==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		if(!userIsA_Host(login, meetingId))
			return errorObject("access denied");
		
		//String abilityToSendFiles = json.findPath("abilityToSendFiles").textValue();
		if(abilityToSendFiles!=null){
			boolean permit;
			if(!abilityToSendFiles.equals("true") && !abilityToSendFiles.equals("false"))
				return errorObject("incorrect data");
			else if (abilityToSendFiles.equals("true"))
				permit = true;
			else
				permit = false;
			DbSingleton.getInstance().getDsl().update(MEETING)
			.set(MEETING.ABILITYTOSENDFILES,permit)
			.where(MEETING.ID.equal(Integer.parseInt(meetingId))).execute();
		}
		
		//String title = json.findPath("title").textValue();
		if(title!=null){
			DbSingleton.getInstance().getDsl().update(MEETING)
			.set(MEETING.TITLE, title)
			.where(MEETING.ID.equal(Integer.parseInt(meetingId))).execute();
		}
		
		//String topic = json.findPath("topic").textValue();
		if(topic!=null){
			DbSingleton.getInstance().getDsl().update(MEETING)
			.set(MEETING.TOPIC, topic)
			.where(MEETING.ID.equal(Integer.parseInt(meetingId))).execute();
		}
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return result;
	}
	
	public static Result adduser(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");

		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String meetingId = json.findPath("meetingid").textValue();
		
		return ok(web_addUser(login, sid, meetingId));
	}
	
	public static ObjectNode web_addUser(String login, String sid, String meetingId){
		if(login==null || sid==null || meetingId==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		if(userIsA_MemberOfMeeting(login, meetingId))
			return errorObject("you are a member");
		if(meetingIsAlreadyFinish(meetingId))
			return errorObject("meeting finished");
		
		java.util.Date date= new java.util.Date();
		Record record = DbSingleton.getInstance().getDsl()
				.insertInto(MEETINGUSER,
						MEETINGUSER.MEETINGID, MEETINGUSER.USERID, MEETINGUSER.JOINTIME)
				.values(Integer.parseInt(meetingId), Integer.parseInt(login), new Timestamp(date.getTime()))
				.returning(MEETINGUSER.ID)
				.fetchOne();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return result;
	}
	
	public static Result getList(String login, String sid){
		return ok(web_getList(login, sid));
	}
	
	public static ObjectNode web_getList(String login, String sid){
		if(login==null || sid==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		ObjectNode result = Json.newObject();
		ArrayNode array = Json.newObject().arrayNode();
		
		org.jooq.Result<Record7<Integer, String, String, Timestamp, Timestamp, Boolean, Integer>> record = 
				DbSingleton.getInstance().getDsl()
				.select(MEETING.ID, MEETING.TITLE, MEETING.TOPIC, MEETING.STARTTIME, 
						MEETING.ENDTIME, MEETING.ABILITYTOSENDFILES, MEETING.AUTHORID)
				.from(MEETING)
				.join(MEETINGUSER)
				.on(MEETING.ID.equal(MEETINGUSER.MEETINGID))
				.where(MEETINGUSER.USERID.equal(Integer.parseInt(login)))
				.orderBy(MEETING.STARTTIME.desc()).fetch();
		
		int count = record.size();
		
		for(int i=0; i<count; i++){
			ObjectNode meeting = Json.newObject();
			
			meeting.put("meetingid", record.getValue(i, MEETING.ID));
			meeting.put("title", record.getValue(i, MEETING.TITLE));
			meeting.put("topic", record.getValue(i, MEETING.TOPIC));
			
			org.jooq.Result<Record1<String>> recordHost = 
					DbSingleton.getInstance().getDsl()
					.select(USER.NAME)
					.from(USER)
					.where(USER.ID.equal(record.getValue(i, MEETING.AUTHORID))).fetch();
			
			meeting.put("hostname", recordHost.getValue(0, USER.NAME));
			meeting.put("starttime", record.getValue(i, MEETING.STARTTIME).toString());
			
			Timestamp ts = record.getValue(i, MEETING.ENDTIME);
			if(ts==null)
				meeting.putNull("endtime");
			else
				meeting.put("endtime", record.getValue(i, MEETING.ENDTIME).toString());
			
			org.jooq.Result<Record> recordMembers = 
					DbSingleton.getInstance().getDsl()
					.select()
					.from(MEETINGUSER)
					.where(MEETINGUSER.MEETINGID.equal(record.getValue(i, MEETING.ID))).fetch();
			
			meeting.put("members", recordMembers.size());
			boolean upl = record.getValue(i, MEETING.ABILITYTOSENDFILES);
			if(login.equals(Integer.toString(record.getValue(i, MEETING.AUTHORID))))
				meeting.put("permissions", "host");
			else if(upl)
				meeting.put("permissions", "memberUpload");
			else
				meeting.put("permissions", "member");
			array.add(meeting);
		}
		
		result.put("users", array);
		return result;
	}
	
	private static boolean userIsA_MemberOfMeeting(String login,
			String meetingId) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl()
				.select()
				.from(MEETINGUSER)
				.where(MEETINGUSER.MEETINGID.equal(Integer.parseInt(meetingId)))
				.and(MEETINGUSER.USERID.equal(Integer.parseInt(login))).fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}

	private static boolean checkIsSidCorrect(String login, String sid) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.ID.equal(new Integer(login)))
				.and(USER.SESSIONHASH.equal(sid)).fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}
	
	private static boolean userIsA_Host(String login, String meetingId) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl()
					.select()
					.from(MEETING)
					.where(MEETING.ID.equal(Integer.parseInt(meetingId)))
					.and(MEETING.AUTHORID.equal(Integer.parseInt(login)))
				    .fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}
	
	private static boolean meetingIsAlreadyFinish(String meetingId) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl()
					.select()
					.from(MEETING)
					.where(MEETING.ID.equal(Integer.parseInt(meetingId)))
					.and(MEETING.ENDTIME.isNotNull())
				    .fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}
	
	

	private static Result errorResult(String msg) {
		return ok(errorObject(msg));
	}
	
	private static ObjectNode errorObject(String msg){
		ObjectNode result = Json.newObject();
		result.put("status", "failed");
		result.put("reason", msg);
		return result;
	}
}
