package daos

import anorm._
import helpers.Exceptions.DatabaseException
import javax.inject.Inject
import models.Account
import models.Account._
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AccountDAO @Inject()(db: Database)(implicit ec: ExecutionContext) {
  def get(id: Int): Future[Either[DatabaseException, Option[Account]]] =
    Future {
      Try {
        db.withConnection { implicit connection =>
          SQL("SELECT * FROM account WHERE a_customer_id = {id}")
            .on(
              "id" -> id
            )
            .as(parser.*)
            .headOption
        }
      }.toEither.left.map { e =>
        DatabaseException(e.getMessage)
      }
    }

  def deposit(id: Int, amount: BigDecimal): Future[Either[DatabaseException, Int]] =
    Future {
      Try {
        db.withConnection { implicit connection =>
          SQL("UPDATE account SET a_balance = {amount} + a_balance WHERE a_customer_id = {id}")
            .on(
              "id" -> id,
              "amount" -> amount
            )
            .executeUpdate()
        }
      }.toEither.left.map { e =>
        DatabaseException(e.getMessage)
      }
    }

  def withdraw(id: Int, amount: BigDecimal): Future[Either[DatabaseException, Int]] =
    Future {
      Try {
        db.withConnection { implicit connection =>
          SQL("UPDATE account SET a_balance = a_balance - {amount} WHERE a_customer_id = {id}")
            .on(
              "id" -> id,
              "amount" -> amount
            )
            .executeUpdate()
        }
      }.toEither.left.map { e =>
        DatabaseException(e.getMessage)
      }
    }
}
