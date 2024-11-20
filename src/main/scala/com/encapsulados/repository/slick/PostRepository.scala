package com.encapsulados.repository.slick

import com.encapsulados.model.slick.{Author, Post}
import slick.jdbc.GetResult
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class PostRepository()(implicit database: Database) {

  implicit val getSupplierResult: GetResult[Post] = GetResult(r => Post(r.nextInt, r.nextString, r.nextLong()))

  def saveAll(postData: (String, Long)*): Future[Seq[Post]] = {
    val insertActions = postData.map { data =>
      sqlu"INSERT INTO post (content, author_id) VALUES (${data._1}, ${data._2})"
        .andThen(
          sql"SELECT id, content, author_id FROM post WHERE content = ${data._1} AND author_id = ${data._2}"
            .as[Post].head
        )
    }
    database.run(DBIO.sequence(insertActions).transactionally)
  }
}
