package helpers

import models.ProblemJson
import play.api.libs.json.{JsError, Json}
import play.api.mvc.Result
import play.api.mvc.Results.Status

trait ErrorJson { this: Logging =>

  def respondWithJsError(title: String,
                         status: Int,
                         detail: String,
                         jsError: JsError): Result = {
    val errorMessage = jsError.errors
      .map({
        case (path, errors) => {
          path.toString() + ": " + errors.map(_.message).mkString(",")
        }
      })
      .mkString("; ")
    respondWithError(title, status, f"$detail. $errorMessage")
  }

  def respondWithError(title: String,
                       status: Int,
                       detail: String,
                       errorMessage: Option[String] = None): Result = {
    logger.info(errorMessage.getOrElse(detail))
    new Status(status)(Json.toJson(ProblemJson(title, status, detail)))
      .as("application/problem+json")
  }
}