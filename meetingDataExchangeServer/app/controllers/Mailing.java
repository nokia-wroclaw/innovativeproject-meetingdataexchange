package controllers;

import models.DbSingleton;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.typesafe.plugin.*;

import static models.Tables.*;

public class Mailing extends Controller {
	public static Result sendHyperlink(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String fileid = json.findPath("fileid").textValue();
		return ok(web_sendhyperlink(login, sid, fileid));
	}
	
	public static ObjectNode web_sendhyperlink(String login, String sid, String fileid){
		if(login==null || sid==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		org.jooq.Result<Record5<Integer, String, String, String, String>> record = DbSingleton.getInstance().getDsl()
				.select(MEETING.ID, MEETING.ACCESSCODE, FILE.NAME, USER.NAME, USER.EMAIL)
				.from(MEETING).join(MEETINGUSER).on(MEETING.ID.equal(MEETINGUSER.MEETINGID))
				.join(FILE).on(FILE.MEETINGUSERID.equal(MEETINGUSER.ID))
				.join(USER).on(USER.LOGIN.equal(MEETINGUSER.USERLOGIN))
				.where(FILE.ID.equal(Integer.parseInt(fileid))).fetch();
		
		String meetingid = String.valueOf(record.getValue(0, MEETING.ID));
		String meetingcode = String.valueOf(record.getValue(0, MEETING.ACCESSCODE));
		String filename = String.valueOf(record.getValue(0, FILE.NAME));
		
		if(!Meetings.userIsA_MemberOfMeeting(login, meetingid))
			return errorObject("access denied");
		
		String message = "Hello " + record.getValue(0, USER.NAME) + "!\n"
				+ "Here you can download " + filename + " file:\n"
				+ play.Play.application().configuration().getString("application.baseUrl") + "download/" 
				+ meetingid + "/" + meetingcode + "/" + filename + "\n\nMeeting Data Exchange Server";
		
		String topic = "MDE - " + filename + " download";
		sendEmail(record.getValue(0, USER.EMAIL), topic, message);

		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return result;
	}
	
	static void sendEmail(String recipient, String subject, String msg){
		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		mail.setSubject(subject);
		mail.addRecipient(recipient);
		mail.addFrom(play.Play.application().configuration().getString("smtp.email"));
		mail.send(msg);
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
	
	private static boolean userIsA_MemberOfMeeting(String login,
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
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(SESSION)
				.where(SESSION.USERLOGIN.equal(login)).and(SESSION.SID.equal(sid)).fetch();
				if(record.isNotEmpty())
					return true;	
				else
					return false;
	}
	
	
}
