package com.encapsulados.repository.slick

import com.encapsulados.model.slick.{Author, Post}
import slick.jdbc.GetResult
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class PostRepository()(implicit database: Database) {

  implicit val getSupplierResult: GetResult[Post] = GetResult(r => Post(r.nextInt, r.nextString, r.nextLong()))

  def saveAll(postData: (String, Long)*): Future[Seq[Post]] = ???
}
