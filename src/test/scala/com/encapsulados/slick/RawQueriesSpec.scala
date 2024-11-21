package com.encapsulados.slick

import com.encapsulados.repository.slick.{AuthorRepository, CommentRepository, PostRepository}
import org.scalatest.FutureOutcome
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike
import repository.slick.DatabaseSeeder
import slick.jdbc.JdbcBackend.Database

import scala.language.postfixOps

class RawQueriesSpec extends AsyncWordSpecLike
  with Matchers
  with DatabaseSeeder {

  implicit val db: _root_.slick.jdbc.JdbcBackend.JdbcDatabaseDef = Database.forConfig("db")

  val authorRepository  = new AuthorRepository()
  val commentRepository = new CommentRepository()
  val postRepository    = new PostRepository()

  override def withFixture(test: NoArgAsyncTest) = new FutureOutcome(for {
//    _      <- createSchema()
    _      <- cleanup()
    _      <- seed()
    result <- super.withFixture(test).toFuture
  } yield result)


  "RawQueries" should {

    "find all authors" in {
      for {
        users <- authorRepository.findAll()
      } yield {
        users should have size 100
      }
    }

    "find authors by domain" in {
      for {
        users <- authorRepository.findByEmailDomain("encapsulados.io")
      } yield {
        users should have size 20
      }
    }

    "comments by post id" in {
      for {
        authors <- authorRepository.saveAll("palan" -> "palan@encapsulados.io")
        palan   = authors.head
        posts   <- postRepository.saveAll(
                      "Aguante Jira!"                    -> palan.id,
                      "Que ganas de ser un Scrum Master" -> palan.id
                  )
        firstPost = posts.head
        _    <- commentRepository.saveAll(
                ("Comentario 1", firstPost.id, palan.id),
                ("Comentario 2", firstPost.id, palan.id)
            )
        comments <- commentRepository.countByPostId(firstPost.id)
      } yield {
        comments shouldBe 3
      }
    }
  }
}
