package helpers

import play.api.Logger

trait Logging {
  protected val logger = Logger(this.getClass)
}