package controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Record;
import org.jooq.Record2;

import models.DbSingleton;
import static models.Tables.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import tools.MD5Checksum;

public class Accounts extends Controller {

	public static Result register(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		else{
			String login = json.findPath("login").textValue();
			String name = json.findPath("name").textValue();
			String email = json.findPath("email").textValue();
			String password = json.findPath("password").textValue();
			
			
			return ok(web_register(login, name, email, password));
		}
	}
	
	public static ObjectNode web_register(String login, String name, String email, String password){
		if(login == null)
			return errorObject("incorrect login");
		if(!checkEmail(email) || email == null)
			return errorObject("incorrect email");
		if(name == null)
			return errorObject("incorrect name");
		if(password == null)
			return errorObject("incorrect password");
		
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER)
							.where(USER.LOGIN.equal(login)).fetch();
		
		if(record.isNotEmpty())
			return errorObject("Login already exists");
		
		/*Record record = */ DbSingleton.getInstance().getDsl()
				.insertInto(USER,
						USER.LOGIN, USER.NAME, USER.EMAIL, USER.PASSWORD)
				.values(login, name, email, MD5Checksum.MD5(password)).execute();
//				.returning(USER.ID)
//				.fetchOne();

		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		//result.put("login", record.getValue(USER.ID));
		return result;
	}
	
	public static Result login(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");

		String login = json.findPath("login").textValue();
		String password = json.findPath("password").textValue();
		return ok(web_login(login, password));
	}
	
	public static ObjectNode web_login(String login, String password){
		if(login == null || password == null)
			return errorObject("incorrect data");

		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.LOGIN.equal(login))
				.and(USER.PASSWORD.equal(MD5Checksum.MD5(password))).fetch();
		if(record.isNotEmpty()){
			String hash = new BigInteger(130, new SecureRandom()).toString(32);
			
			//DbSingleton.getInstance().getDsl().update(USER).set(USER.SESSIONHASH, hash).where(USER.ID.equal(new Integer(login))).execute();
			DbSingleton.getInstance().getDsl().insertInto(SESSION,
					SESSION.USERLOGIN, SESSION.SID)
					.values(login, hash);
			ObjectNode result = Json.newObject();
			result.put("status", "ok");
			result.put("sid", hash);
			return result;
		}
		else
			return errorObject("incorrect data");
	}
	
	public static Result logoff(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		return ok(web_logoff(login, sid));
	}
	
	public static ObjectNode web_logoff(String login, String sid){
		if(login == null || sid == null)
			return errorObject("incorrect data");
		
		//org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.ID.equal(new Integer(login)))
		//		.and(USER.SESSIONHASH.equal(sid)).fetch();
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(SESSION)
				.where(SESSION.USERLOGIN.equal(login)).and(SESSION.SID.equal(sid)).fetch();
		if(record.isNotEmpty()){
			//DbSingleton.getInstance().getDsl().update(USER).set(USER.SESSIONHASH, "").where(USER.ID.equal(new Integer(login))).execute();
			DbSingleton.getInstance().getDsl().delete(SESSION).where(SESSION.USERLOGIN.equal(login)).and(SESSION.SID.equal(sid)).execute();
			ObjectNode result = Json.newObject();
			result.put("status", "ok");
			return result;
		}
		else
			return errorObject("incorrect sid");
	}
	
	public static Result getData(String login, String sid){
		return ok(web_getData(login,sid));
	}
	
	public static ObjectNode web_getData(String login, String sid){
		if(login==null || sid==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		org.jooq.Result<Record2<String, String>> record = DbSingleton.getInstance().getDsl()
				.select(USER.NAME, USER.EMAIL)
				.from(USER)
				.where(USER.LOGIN.equal(login)).fetch();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("name", record.getValue(0, USER.NAME));
		result.put("email", record.getValue(0, USER.EMAIL));
		
		return result;
	}
	
	public static Result setData(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		String email = json.findPath("email").textValue();
		String name = json.findPath("name").textValue();
		String password = json.findPath("password").textValue();
		
		return ok(web_setData(login,sid, email, name, password));
	}
	
	public static ObjectNode web_setData(String login, String sid, String email, String name, String password){
		if(login==null || sid==null)
			return errorObject("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorObject("incorrect sid");
		
		if(email != null){
			if(!checkEmail(email))
				return errorObject("incorrect email");
			
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.EMAIL, email)
				.where(USER.LOGIN.equal(login)).execute();
		}
		
		if(name != null){
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.NAME, name)
				.where(USER.LOGIN.equal(login)).execute();
		}
		
		if(password != null){
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.PASSWORD, MD5Checksum.MD5(password))
				.where(USER.LOGIN.equal(login)).execute();
		}
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		return result;
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
	
	private static boolean checkEmail(String email) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
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
	
}
