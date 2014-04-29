package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class GeneralStuff extends Controller{
	public static Result getName() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("server.config"));
		String line = br.readLine();
		ObjectNode result = Json.newObject();
		result.put("servername", line);
		
		return ok(result);
	}
	
	public static ObjectNode web_getName() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("server.config"));
		String line = br.readLine();
		ObjectNode result = Json.newObject();
		result.put("servername", line);
		
		return result;
	}
	
}
