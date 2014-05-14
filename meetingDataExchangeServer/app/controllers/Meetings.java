package controllers;

import static models.Tables.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;

import models.DbSingleton;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record8;

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
		String hash = new BigInteger(130, new SecureRandom()).toString(32);
		hash = hash.substring(1,6);
		Record record = DbSingleton.getInstance().getDsl()
				.insertInto(MEETING,
						MEETING.TITLE, MEETING.TOPIC, MEETING.STARTTIME, MEETING.ABILITYTOSENDFILES, 
						MEETING.AUTHORLOGIN, MEETING.ACCESSCODE)
				.values(title, topic, new Timestamp(date.getTime()), permit, login, hash)
				.returning(MEETING.ID)
				.fetchOne();
		
		int meetingId = record.getValue(MEETING.ID);
		
		DbSingleton.getInstance().getDsl()
				.insertInto(MEETINGUSER,
						MEETINGUSER.MEETINGID, MEETINGUSER.USERLOGIN, MEETINGUSER.JOINTIME)
				.values(meetingId, login, new Timestamp(date.getTime()))
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
		String accessCode = json.findPath("accessCode").textValue();
		
		return ok(web_addUser(login, sid, meetingId, accessCode));
	}
	
	public static ObjectNode web_addUser(String login, String sid, String meetingId, String accessCode){
		if(login==null || sid==null || meetingId==null || accessCode==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		if(codeIsWrong(login, meetingId, accessCode))
			return errorObject("incorrect accessCode");
		if(userIsA_MemberOfMeeting(login, meetingId))
			return errorObject("you are a member");
		if(meetingIsAlreadyFinish(meetingId))
			return errorObject("meeting finished");
		
		java.util.Date date= new java.util.Date();
		Record record = DbSingleton.getInstance().getDsl()
				.insertInto(MEETINGUSER,
						MEETINGUSER.MEETINGID, MEETINGUSER.USERLOGIN, MEETINGUSER.JOINTIME)
				.values(Integer.parseInt(meetingId), login, new Timestamp(date.getTime()))
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
		
		org.jooq.Result<Record8<Integer, String, String, Timestamp, Timestamp, Boolean, String, String>> record = 
				DbSingleton.getInstance().getDsl()
				.select(MEETING.ID, MEETING.TITLE, MEETING.TOPIC, MEETING.STARTTIME, 
						MEETING.ENDTIME, MEETING.ABILITYTOSENDFILES, MEETING.AUTHORLOGIN, MEETING.ACCESSCODE)
				.from(MEETING)
				.join(MEETINGUSER)
				.on(MEETING.ID.equal(MEETINGUSER.MEETINGID))
				.where(MEETINGUSER.USERLOGIN.equal(login))
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
					.where(USER.LOGIN.equal(record.getValue(i, MEETING.AUTHORLOGIN))).fetch();
			
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
			if(login.equals(record.getValue(i, MEETING.AUTHORLOGIN)))
				meeting.put("permissions", "host");
			else if(upl)
				meeting.put("permissions", "memberUpload");
			else
				meeting.put("permissions", "member");
			meeting.put("accessCode", record.getValue(i, MEETING.ACCESSCODE));
			array.add(meeting);
		}
		
		result.put("meetings", array);
		return result;
	}
	
	public static boolean userIsA_MemberOfMeeting(String login,
			String meetingId) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl()
				.select()
				.from(MEETINGUSER)
				.where(MEETINGUSER.MEETINGID.equal(Integer.parseInt(meetingId)))
				.and(MEETINGUSER.USERLOGIN.equal(login)).fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}

	private static boolean checkIsSidCorrect(String login, String sid) {
//		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.ID.equal(new Integer(login)))
//				.and(USER.SESSIONHASH.equal(sid)).fetch();
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(SESSION)
				.where(SESSION.USERLOGIN.equal(login)).and(SESSION.SID.equal(sid)).fetch();
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
					.and(MEETING.AUTHORLOGIN.equal(login))
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
	
	private static boolean codeIsWrong(String login, String meetingId,
			String accessCode) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl()
				.select()
				.from(MEETING)
				.where(MEETING.ID.equal(Integer.parseInt(meetingId)))
				.and(MEETING.ACCESSCODE.equal(accessCode))
			    .fetch();
		if(record.isEmpty())
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
