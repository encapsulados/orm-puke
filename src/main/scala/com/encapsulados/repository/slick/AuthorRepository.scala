package com.encapsulados.repository.slick

import com.encapsulados.model.slick.Author
import slick.jdbc.GetResult
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class AuthorRepository()(implicit database: Database) {

  type Email = String
  type Username = String

  implicit val getSupplierResult: GetResult[Author] = GetResult(r => Author(r.nextInt, r.nextString, r.nextString))

  def findAll(): Future[Vector[Author]] = Future.successful(List.empty[Author].toVector)

  def findByEmailDomain(domain: String): Future[Vector[Author]] = ???

  def saveAll(authorData: (Username, Email)*): Future[Seq[Author]] = ???
}
