package controllers;

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
			return registerErrorResult("json excepted");
		else{
			String name = json.findPath("name").textValue();
			String email = json.findPath("email").textValue();
			String password = json.findPath("password").textValue();
			
			if(!checkEmail(email) || email == null)
				return registerErrorResult("incorrect email");
			if(name == null)
				return registerErrorResult("incorrect name");
			if(password == null)
				return registerErrorResult("incorrect password");
			return registerTryResult(name, email, password);
		}
	}

	private static Result registerTryResult(String name, String email,
			String password) {
		Record record = DbSingleton.getInstance().getDsl()
		.insertInto(USER,
				USER.NAME, USER.EMAIL, USER.PASSWORD)
		.values(name, email, MD5Checksum.MD5(password))
		.returning(USER.ID)
		.fetchOne();
		return loginResult(record.getValue(USER.ID));
	}

	private static Result loginResult(int idUser) {
		ObjectNode result = Json.newObject();
		result.put("status", "ok");
		result.put("login", idUser);
		return ok(result);
	}

	private static Result registerErrorResult(String msg) {
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
	
	
}
