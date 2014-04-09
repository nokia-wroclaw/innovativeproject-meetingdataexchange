package controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Record;

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
			return accountsErrorResult("json excepted");
		else{
			String name = json.findPath("name").textValue();
			String email = json.findPath("email").textValue();
			String password = json.findPath("password").textValue();
			
			if(!checkEmail(email) || email == null)
				return accountsErrorResult("incorrect email");
			if(name == null)
				return accountsErrorResult("incorrect name");
			if(password == null)
				return accountsErrorResult("incorrect password");
			return registerTryResult(name, email, password);
		}
	}
	
	public static Result login(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return accountsErrorResult("json excepted");
		else{
			String login = json.findPath("login").textValue();
			String password = json.findPath("password").textValue();
			if(login == null || password == null)
				return accountsErrorResult("incorrect data");
			return loginTryResult(login, password);
		}
	}
	
	public static Result logoff(){
		JsonNode json = request().body().asJson();
		if(json == null)
			return accountsErrorResult("json excepted");
		else{
			String login = json.findPath("login").textValue();
			String sid = json.findPath("sid").textValue();
			if(login == null || sid == null)
				return accountsErrorResult("incorrect data");
			return logoffTryResult(login, sid);
		}
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
					return accountsErrorResult("incorrect sid");
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

	private static Result accountsErrorResult(String msg) {
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
			return accountsErrorResult("incorrect data");
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
	
}
