package controllers;
import com.fasterxml.jackson.core._
import play.api._
import play.api.libs.json._

import play.api.mvc.Result
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Register extends Controller {
    
	val form = Form(
			tuple(
					"login" -> text,
					"password" -> text,
					"email"-> text
			)
	)
	
	def registerMain = Action {
		Ok(views.html.RegisterForm());
    }
	
	def submit = Action { implicit request =>
		val (name, pass, email) = form.bindFromRequest.get;
		val json = Accounts.web_register(name, email, pass);
		//val json: JsValue = Json.parse(jsonString);
		
        val status = json.get("status").asText();
		if( status == "ok"){
			val id = json.get("login").asText();
			Ok(views.html.RegisterSuccess(id));
		}
		else{
			Ok(views.html.RegisterFail(status));
		}
  }
}