package controllers;

import java.util.Map;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
    
    public static Result witam() {
    	return ok("Witam na serwerze");
    }
    
    public static Result powitanie(String imie) {
    	return ok("Cześć "+imie);
    }
    
    public static Result powitaniePost() {
    	Map<String, String[]> values = Application.request().body().asFormUrlEncoded();
    	if(values.containsKey("imie"))
    		return ok("Cześć "+values.get("imie")[0]+". Umiesz wysyłać formularze metodą POST!");
    	else
    		return Application.badRequest();
    }
    
    public static Result podzbiory(int n, int k) {
    	return ok(Integer.toString(newton(n,k)));
    }
    
    public static int newton(int n, int k){
    	if(k==0 || k==n)
    		return 1;
    	else
    		return newton(n-1,k-1)+newton(n-1,k);
    }
}
