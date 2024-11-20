package com.encapsulados.repository.slick

import com.encapsulados.model.slick.{Comment, Post}
import slick.jdbc.GetResult
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class CommentRepository()(implicit database: Database) {

  type Content = String
  type PostId = Long
  type AuthorId = Long

  implicit val getSupplierResult: GetResult[Comment] = GetResult(r => Comment(r.nextLong(), r.nextString, r.nextLong(), r.nextLong(), r.nextLong()))

  def countByPostId(postId: Long): Future[Int] =
    database.run(
      sql"SELECT COUNT(*) FROM comment WHERE post_id = $postId"
        .as[Int]
        .head
    )

  def saveAll(tuples: IndexedSeq[(String, PostId, AuthorId)]) = {
    val insertActions = tuples.map { data =>
      sqlu"INSERT INTO comment (text, post_id, author_id, parent_comment_id) VALUES (${data._1}, ${data._2}, ${data._3}, NULL)"
        .andThen(
          sql"SELECT id, text, author_id, parent_comment_id, post_id FROM comment WHERE text = ${data._1} AND post_id = ${data._2} AND author_id = ${data._3}"
            .as[Comment].head
        )
    }
    database.run(DBIO.sequence(insertActions).transactionally)
  }

}
