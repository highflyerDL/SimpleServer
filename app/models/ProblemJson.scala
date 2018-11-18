package models

import play.api.libs.json._

case class ProblemJson(title: String,
                       status: Int,
                       detail: String,
                       problemType: String = "about:blank")

object ProblemJson {
  implicit val problemWrites = new Writes[ProblemJson] {
    override def writes(errorResult: ProblemJson): JsValue = Json.obj(
      "type" -> errorResult.problemType,
      "title" -> errorResult.title,
      "status" -> errorResult.status,
      "detail" -> errorResult.detail
    )
  }
}