package models

import anorm.SqlParser._
import anorm._

case class Customer(id: Int, firstName: String, lastName: String, email: String)

object Customer {
  val parser: RowParser[Customer] =
    get[Int]("c_id") ~
      get[String]("c_first_name") ~
      get[String]("c_last_name") ~
      get[String]("c_email") map {
      case id ~ firstName ~ lastName ~ email =>
        Customer(id, firstName, lastName, email)
    }
}
