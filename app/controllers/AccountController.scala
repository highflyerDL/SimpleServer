package controllers

import helpers.{ErrorJson, Logging}
import helpers.Exceptions.{DatabaseException, InsufficientAmountException}
import javax.inject.Inject
import models.Account.BalanceRequestType
import models.BalanceRequest
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._
import services.AccountService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class AccountController @Inject()
(cc: ControllerComponents, accountService: AccountService)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with Logging with ErrorJson {

  def getAccount(id: Int) = Action.async { implicit request =>
    accountService.get(id) map {
      case Right(accountOption) =>
        if (accountOption.isDefined) {
          Ok(Json.toJson(accountOption.get))
        } else {
          respondWithError("Not found",
            NOT_FOUND,
            "The account with specified id is not found.")
        }

      case Left(e) =>
        logger.error(s"Error getting account", e)
        respondWithError(
          "Internal Server Error occurred",
          INTERNAL_SERVER_ERROR,
          e.msg)
    } recover {
      case NonFatal(e) =>
        logger.error("Error getting account", e)
        respondWithError(
          "Internal Server Error occurred",
          INTERNAL_SERVER_ERROR,
          e.getMessage)
    }
  }

  def balancePOST(id: Int) = Action.async(parse.json) { implicit request =>
    request.body.validate[BalanceRequest] match {
      case JsSuccess(request: BalanceRequest, _) => {
        request.requestType match {
          case BalanceRequestType.Withdraw =>
            accountService.withdraw(id, request.amount) map {
              case Right(_) => NoContent
              case Left(exception) =>
                logger.error(s"Error withdrawing", exception)
                exception match {
                  case DatabaseException(msg) =>
                    respondWithError(
                      "Internal Server Error occurred",
                      INTERNAL_SERVER_ERROR,
                      msg)
                  case InsufficientAmountException =>
                    respondWithError(
                      "Insufficient balance to withdraw",
                      BAD_REQUEST,
                      "")
                  case e =>
                    respondWithError(
                      "Internal Server Error occurred",
                      INTERNAL_SERVER_ERROR,
                      e.getMessage)
                }
            } recover {
              case NonFatal(e) =>
                logger.error("Error withdrawing", e)
                respondWithError(
                  "Internal Server Error occurred",
                  INTERNAL_SERVER_ERROR,
                  e.getMessage)
            }
          case BalanceRequestType.Deposit =>
            accountService.deposit(id, request.amount) map {
              case Right(_) => NoContent
              case Left(exception) =>
                logger.error(s"Error depositing", exception)
                exception match {
                  case DatabaseException(msg) =>
                    respondWithError(
                      "Internal Server Error occurred",
                      INTERNAL_SERVER_ERROR,
                      msg)
                  case e =>
                    respondWithError(
                      "Internal Server Error occurred",
                      INTERNAL_SERVER_ERROR,
                      e.getMessage)
                }
            } recover {
              case NonFatal(e) =>
                logger.error("Error depositing", e)
                respondWithError(
                  "Internal Server Error occurred",
                  INTERNAL_SERVER_ERROR,
                  e.getMessage)
            }
        }
      }

      case jsError: JsError =>
        Future.successful(
          respondWithJsError("Bad request",
            BAD_REQUEST,
            "Balance request has invalid format.",
            jsError)
        )
    }
  }
}

