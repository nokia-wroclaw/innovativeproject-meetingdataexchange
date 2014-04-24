package controllers;

import static models.public_.Tables.*;

import java.sql.Timestamp;

import models.DbSingleton;

import org.jooq.Record;

import com.fasterxml.jackson.databind.JsonNode;
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
		
		if(login==null || title==null || topic==null || abilityToSendFiles==null || sid==null)
			return errorResult("incorrect data");
		
		boolean permit;
		if(!abilityToSendFiles.equals("true") && !abilityToSendFiles.equals("false"))
			return errorResult("incorrect data");
		else if (abilityToSendFiles.equals("true"))
			permit = true;
		else
			permit = false;
		if(!checkIsSidCorrect(login, sid))
			return errorResult("incorrect sid");
		
		java.util.Date date= new java.util.Date();
		Record record = DbSingleton.getInstance().getDsl()
				.insertInto(MEETING,
						MEETING.TITLE, MEETING.TOPIC, MEETING.STARTTIME, MEETING.ABILITYTOSENDFILES, MEETING.AUTHORID)
				.values(title, topic, new Timestamp(date.getTime()), permit, Integer.parseInt(login))
				.returning(MEETING.ID)
				.fetchOne();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		
		return ok(result);
	}
	
	public static Result stop(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String meetingId = json.findPath("meetingId").textValue();
		
		if(login==null || meetingId==null || sid==null)
			return errorResult("incorrect data");
		
		if(!checkIsSidCorrect(login, sid))
			return errorResult("incorrect sid");
		
		if(!userIsA_Host(login, meetingId))
			return errorResult("access denied");
		
		if(meetingIsAlreadyFinish(meetingId))
			return errorResult("already finished");
		
		java.util.Date date= new java.util.Date();
		
		DbSingleton.getInstance().getDsl()
		.update(MEETING)
		.set(MEETING.ENDTIME, new Timestamp(date.getTime()))
		.where(MEETING.ID.equal(Integer.parseInt(meetingId))).execute();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		
		return ok(result);
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
		ObjectNode result = Json.newObject();
		result.put("status", "failed");
		result.put("reason", msg);
		return ok(result);
	}
}
