package controllers;

import play.api._
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
	val (log, pass) = form.bindFromRequest.get
	Ok("done")
  }
}