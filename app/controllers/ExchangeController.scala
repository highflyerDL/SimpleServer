package controllers

import helpers.{ErrorJson, Logging}
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class ExchangeController  @Inject()
(cc: ControllerComponents, ws: WSClient)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with Logging with ErrorJson {

  def getEurToUsd() = Action.async { implicit request =>
      ws.url("http://free.currencyconverterapi.com/api/v5/convert?q=EUR_USD&compact=y").get().map { response =>
      val rate = (response.json \ "EUR_USD" \ "val").as[Float]
      Ok(Json.toJson(rate))
    }
  }
}
