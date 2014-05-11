package controllers;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import org.jooq.Record1;
import org.jooq.exception.DataAccessException;

import models.DbSingleton;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import tools.MD5Checksum;
import static models.Tables.*;

public class FilesManagement extends Controller {
	
	public static Result upload(String meetingid, String filename) throws DataAccessException, Exception {
		String login = session("id");
		String sid = session("sid");
		if(login==null || sid==null)
			return errorResult("Incorrect sid");
		
		if(!Meetings.userIsA_MemberOfMeeting(login, meetingid))
			return errorResult("Not a member of meeting");
		
		org.jooq.Result<Record1<Integer>> record = DbSingleton.getInstance().getDsl().select(MEETINGUSER.ID)
				.from(MEETING)
				.join(MEETINGUSER).on(MEETINGUSER.MEETINGID.equal(Integer.parseInt(meetingid)))
					.and(MEETINGUSER.USERLOGIN.equal(login))
				.where(MEETING.ID.equal(Integer.parseInt(meetingid)))
				.and(MEETING.ABILITYTOSENDFILES.isTrue().or(MEETING.AUTHORLOGIN.equal(login))).fetch();
		
		int meetinguserid;
		
		if(record.isEmpty())
			return errorResult("Upload forbidden");
		else
			meetinguserid = record.getValue(0, MEETINGUSER.ID);
		
		File newFile = new File(System.getProperty("user.dir")+"/upload/"+meetingid+"/"+filename);
		
		if(newFile.exists())
			return errorResult("File with this name already exists");
			
		File rawFile = request().body().asRaw().asFile();
		newFile.createNewFile();
		Files.copy(rawFile, newFile);
		
		java.util.Date date= new java.util.Date();
		
		DbSingleton.getInstance().getDsl()
				.insertInto(FILE,
						FILE.MEETINGUSERID, FILE.NAME, FILE.SIZEKB, FILE.ADDEDTIME, FILE.HASHMD5)
				.values(meetinguserid, filename, 
						new Integer((int) (newFile.length()/1024.0)), new Timestamp(date.getTime()),
						MD5Checksum.getMD5Checksum(System.getProperty("user.dir")+"/upload/"+meetingid+"/"+filename))
				.execute();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return ok(result);
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
