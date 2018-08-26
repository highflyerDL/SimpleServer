package daos

import javax.inject.Inject
import play.api.db.Database
import anorm._
import models.Customer._

import scala.concurrent.{ExecutionContext, Future}

class CustomerDAO @Inject()(db: Database)(implicit ec: ExecutionContext) {
  def get(id: Int) = Future {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM customer WHERE c_id = {id}")
        .on("id" -> id)
        .as(parser.*)
    }
  }
}
