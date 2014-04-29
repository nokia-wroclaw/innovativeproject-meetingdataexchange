package controllers;

import play.api._
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
	val (log, pass, email) = form.bindFromRequest.get
	Ok("done")
  }
}