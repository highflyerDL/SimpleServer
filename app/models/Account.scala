package models

import anorm.SqlParser._
import anorm._

case class Account(id: Int, customerId: Int, iban: String, balance: BigDecimal)

object Account {
  val parser: RowParser[Account] =
    get[Int]("a_id") ~
      get[Int]("a_customer_id") ~
      get[String]("a_iban") ~
      get[BigDecimal]("a_balance")  map {
      case id ~ customerId ~ iban ~ balance =>
        Account(id, customerId, iban, balance)
    }
}
