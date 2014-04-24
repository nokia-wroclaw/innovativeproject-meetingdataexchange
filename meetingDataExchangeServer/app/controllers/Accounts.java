package controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Record;
import org.jooq.Record2;

import models.DbSingleton;
import static models.public_.Tables.*;

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
			String name = json.findPath("name").textValue();
			String email = json.findPath("email").textValue();
			String password = json.findPath("password").textValue();
			
			if(!checkEmail(email) || email == null)
				return errorResult("incorrect email");
			if(name == null)
				return errorResult("incorrect name");
			if(password == null)
				return errorResult("incorrect password");
			return registerTryResult(name, email, password);
		}
	}
	
	public static Result login(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		else{
			String login = json.findPath("login").textValue();
			String password = json.findPath("password").textValue();
			if(login == null || password == null)
				return errorResult("incorrect data");
			return loginTryResult(login, password);
		}
	}
	
	public static Result logoff(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		else{
			String login = json.findPath("login").textValue();
			String sid = json.findPath("sid").textValue();
			if(login == null || sid == null)
				return errorResult("incorrect data");
			return logoffTryResult(login, sid);
		}
	}
	
	public static Result getData(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		
		if(login==null || sid==null)
			return errorResult("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorResult("incorrect sid");
		
		org.jooq.Result<Record2<String, String>> record = DbSingleton.getInstance().getDsl()
				.select(USER.NAME, USER.EMAIL)
				.from(USER)
				.where(USER.ID.equal(Integer.parseInt(login))).fetch();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("name", record.getValue(0, USER.NAME));
		result.put("email", record.getValue(0, USER.EMAIL));
		
		return ok(result);
	}
	
	public static Result setData(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return errorResult("json excepted");
		
		String login = json.findPath("login").textValue();
		String sid = json.findPath("sid").textValue();
		
		if(login==null || sid==null)
			return errorResult("incorrect data");
		if(!checkIsSidCorrect(login, sid))
			return errorResult("incorrect sid");
		
		String email = json.findPath("email").textValue();
		if(email != null){
			if(!checkEmail(email))
				return errorResult("incorrect email");
			
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.EMAIL, email)
				.where(USER.ID.equal(Integer.parseInt(login))).execute();
		}
		
		String name = json.findPath("name").textValue();
		if(name != null){
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.NAME, name)
				.where(USER.ID.equal(Integer.parseInt(login))).execute();
		}
		
		String password = json.findPath("password").textValue();
		if(password != null){
			DbSingleton.getInstance().getDsl().update(USER)
				.set(USER.PASSWORD, MD5Checksum.MD5(password))
				.where(USER.ID.equal(Integer.parseInt(login))).execute();
		}
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		
		return ok(result);
	}

	private static Result logoffTryResult(String login, String sid) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.ID.equal(new Integer(login)))
				.and(USER.SESSIONHASH.equal(sid)).fetch();
				if(record.isNotEmpty()){
					//stop current session
					DbSingleton.getInstance().getDsl().update(USER).set(USER.SESSIONHASH, "").where(USER.ID.equal(new Integer(login))).execute();
					
					ObjectNode result = Json.newObject();
					result.put("status", "ok");
					return ok(result);
				}
				else
					return errorResult("incorrect sid");
	}

	private static Result registerTryResult(String name, String email,
			String password) {
		Record record = DbSingleton.getInstance().getDsl()
		.insertInto(USER,
				USER.NAME, USER.EMAIL, USER.PASSWORD)
		.values(name, email, MD5Checksum.MD5(password))
		.returning(USER.ID)
		.fetchOne();
		return registerResult(record.getValue(USER.ID));
	}

	private static Result registerResult(int idUser) {
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("login", idUser);
		return ok(result);
	}

	private static Result errorResult(String msg) {
		ObjectNode result = Json.newObject();
		result.put("status", "failed");
		result.put("reason", msg);
		return ok(result);
	}
	
	private static boolean checkEmail(String email) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private static Result loginTryResult(String login, String password) {
		org.jooq.Result<Record> record = DbSingleton.getInstance().getDsl().select().from(USER).where(USER.ID.equal(new Integer(login)))
		.and(USER.PASSWORD.equal(MD5Checksum.MD5(password))).fetch();
		if(record.isNotEmpty())
				return loginResult(new Integer(login));
		else
			return errorResult("incorrect data");
	}

	private static Result loginResult(int login) {
		String hash = new BigInteger(130, new SecureRandom()).toString(32);
		
		//register new session
		DbSingleton.getInstance().getDsl().update(USER).set(USER.SESSIONHASH, hash).where(USER.ID.equal(login)).execute();
		
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("sid", hash);
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
	
}
