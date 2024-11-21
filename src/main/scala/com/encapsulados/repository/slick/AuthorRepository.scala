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

  def findAll(): Future[Vector[Author]] =
    database.run(
      sql"SELECT id, username, email FROM author"
        .as[Author]
    )

  def findByEmailDomain(domain: String): Future[Vector[Author]] =
    database.run(
      sql"""
            SELECT id, username, email FROM author WHERE email LIKE ${"%@" + domain}
        """
        .as[Author]
    )

  def saveAll(authorData: (Username, Email)*): Future[Seq[Author]] = {
    val insertActions = authorData.map { data =>
      sqlu"INSERT INTO author (username, email) VALUES (${data._1}, ${data._2})"
        .andThen(
          sql"SELECT id, username, email FROM author WHERE username = ${data._1} AND email = ${data._2}"
            .as[Author].head
        )
    }

    database.run(DBIO.sequence(insertActions).transactionally)
  }
}
