package controllers;
import com.fasterxml.jackson.core._
import play.api._
import play.api.libs.json._

import play.api.mvc.Result
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object LogIn extends Controller {
    
	val form = Form(
			tuple(
					"login" -> text,
					"password" -> text
			)
	)
	
	def loginMain = Action {
		Ok(views.html.LogInForm());
    }
	
	def submit = Action { implicit request =>
	val (name, pass) = form.bindFromRequest.get
	val json = Accounts.web_login(name,  pass);
	val status = json.get("status").asText();
	if( status == "ok")
		Ok(views.html.LogInSuccess());
	else{
		Ok(views.html.LogInFail(status));
	}
  }
}