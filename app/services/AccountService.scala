package services

import daos.AccountDAO
import helpers.Exceptions.{DatabaseException, InsufficientAmountException}
import javax.inject.Inject
import models.Account

import scala.concurrent.{ExecutionContext, Future}

class AccountService @Inject()(accountDAO: AccountDAO)(implicit ec: ExecutionContext) {
  def get(id: Int): Future[Either[DatabaseException, Option[Account]]] = {
    accountDAO.get(id).map {
      case Right(account) => Right(account)
      case Left(e) => Left(e)
    }
  }

  def withdraw(id: Int, amount: BigDecimal): Future[Either[RuntimeException, Unit]] = {
    accountDAO.withdraw(id, amount) map {
      case Right(_) => Right(())
      case Left(e) =>
        if (e.msg.contains("balance_nonnegative")) {
          Left(InsufficientAmountException)
        } else {
          Left(e)
        }
    }
  }

  def deposit(id: Int, amount: BigDecimal): Future[Either[DatabaseException, Unit]] = {
    accountDAO.deposit(id, amount).map {
      case Right(_) => Right(())
      case Left(e) => Left(e)
    }
  }
}
