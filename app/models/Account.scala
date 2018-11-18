package models

import anorm.SqlParser._
import anorm._
import models.Account.BalanceRequestType
import models.Account.BalanceRequestType.BalanceRequestType
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Account(id: Int, customerId: Int, iban: String, balance: BigDecimal)

object Account {
  object BalanceRequestType extends Enumeration {
    type BalanceRequestType = Value
    val Withdraw, Deposit = Value
  }

  val parser: RowParser[Account] =
    get[Int]("a_id") ~
      get[Int]("a_customer_id") ~
      get[String]("a_iban") ~
      get[BigDecimal]("a_balance")  map {
      case id ~ customerId ~ iban ~ balance =>
        Account(id, customerId, iban, balance)
    }

  implicit val accountReads: Writes[Account] = account =>
    Json.obj(
      "account_id" -> account.id,
      "customer_id" -> account.customerId,
      "iban" -> account.iban,
      "balance" -> account.balance
    )
}

case class BalanceRequest(requestType: BalanceRequestType, amount: BigDecimal)

object BalanceRequest {
  implicit val balanceRequestTypeFormat = new Format[BalanceRequestType] {
    def reads(json: JsValue) = JsSuccess(BalanceRequestType.withName(json.as[String]))
    def writes(myEnum: BalanceRequestType) = JsString(myEnum.toString)
  }

  implicit val balanceRequestReads: Reads[BalanceRequest] = (
    (__ \ "request_type").read[BalanceRequestType] and
      (__ \ "amount").read[BigDecimal]
    )(BalanceRequest.apply _)
}
