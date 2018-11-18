package helpers

object Exceptions {
  case object InsufficientAmountException extends RuntimeException
  case class DatabaseException(msg: String) extends RuntimeException(msg)
}
