package com.encapsulados.slick

import com.encapsulados.repository.slick.{AuthorRepository, CommentRepository, PostRepository}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike
import org.scalatest.{BeforeAndAfterEach, FutureOutcome, SequentialNestedSuiteExecution}
import repository.slick.DatabaseSeeder
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future
import scala.language.postfixOps

class RawQueriesSpec extends AsyncWordSpecLike
  with Matchers
  with DatabaseSeeder
  with BeforeAndAfterEach {

  implicit val db: _root_.slick.jdbc.JdbcBackend.JdbcDatabaseDef = Database.forConfig("db")

  val authorRepository  = new AuthorRepository()
  val commentRepository = new CommentRepository()
  val postRepository    = new PostRepository()

  override def withFixture(test: NoArgAsyncTest) = new FutureOutcome(for {
    _      <- cleanup()
    _      <- seed()
    result <- super.withFixture(test).toFuture
  } yield result)

  "RawQueries" should {
    "be able to run a raw query" in {
      for {
        users <- authorRepository.findAll()
      } yield {
        users should have size 100
      }
    }

    "find by domain" in {
      for {
        users <- Future.successful(println("queryando authors")).flatMap(_ => authorRepository.findByEmailDomain("encapsulados.io"))
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
        _    <- Future.sequence(
          posts.map { post =>
            commentRepository.saveAll(
              (1 to 3).map { i =>
                (s"Comentario $i", post.id, authors.head.id)
              }
            )
          }
        )
        comments <- commentRepository.countByPostId(posts.head.id)
      } yield {
        comments shouldBe 3
      }
    }
  }
}
