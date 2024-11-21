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

  def countByPostId(postId: Long): Future[Int] = ???

  def saveAll(tuples: (String, PostId, AuthorId)*): Future[Seq[Comment]] = ???

}
