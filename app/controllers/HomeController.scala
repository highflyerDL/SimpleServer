package controllers

import javax.inject.Inject

import play.api.mvc._

class HomeController @Inject()
(cc: ControllerComponents)
  extends AbstractController(cc) {

  def index = Action {
    Ok("It works!")
  }

}