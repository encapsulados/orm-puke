package repository.slick

import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.language.postfixOps
import slick.jdbc.JdbcBackend.Database

trait DatabaseSeeder {

  val authorLines = Source.fromResource("sql/authors.csv").getLines().drop(1)
  val postLines = Source.fromResource("sql/posts.csv").getLines().drop(1)
  val commentsLines = Source.fromResource("sql/comments.csv").getLines().drop(1)
  val r = new scala.util.Random()

  def cleanup()(implicit ec: ExecutionContext, db: Database) = {
    val action = DBIO.seq(
      sqlu"DELETE FROM comment",
      sqlu"DELETE FROM post",
      sqlu"DELETE FROM author"
    )
    db.run(action).andThen {
      _ => println("Cleanup completed")
    }
  }

  def seed()(implicit ec: ExecutionContext, db: Database) = {

    val futureAuthorIds: Future[Seq[Long]] = db.run {
      DBIO.sequence {
        authorLines.map { line =>
          val columns = line.split(",")
          val insertQuery = sqlu"INSERT INTO author (email, username) VALUES (${columns(0)}, ${columns(1)})"
          val getIdQuery = sql"SELECT LAST_INSERT_ID()".as[Long].head

          (for {
            _ <- insertQuery
            id <- getIdQuery
          } yield id).transactionally
        }.toSeq
      }
    }

    val futurePostIds: Future[Seq[Long]] = futureAuthorIds.flatMap { authorIds =>
      val action = DBIO.sequence {
        postLines.map { line =>
          val columns = line.split(",")
          val authorId = r.shuffle(authorIds).head
          val insertQuery = sqlu"INSERT INTO post (content, author_id) VALUES (${columns(0)}, $authorId)"
          val getIdQuery = sql"SELECT LAST_INSERT_ID()".as[Long].head

          (for {
            _ <- insertQuery
            id <- getIdQuery
          } yield id).transactionally
        }.toSeq
      }
      db.run(action)
    }


    val comments: Future[Seq[Int]] = for {
      authorIds <- futureAuthorIds
      postIds   <- futurePostIds
      result    <- db.run {
        DBIO.sequence {
          commentsLines.map { line =>
            val columns   = line.split(",")
            val postId    = r.shuffle(postIds).head
            val authorId  = r.shuffle(authorIds).head
            val insertQuery = sqlu"INSERT INTO comment (text, author_id, parent_comment_id, post_id) VALUES (${columns(0)}, $authorId, null, $postId)"
            insertQuery
          }.toSeq
        }
      }
    } yield result

    for {
      _ <- futureAuthorIds
      _ <- futurePostIds
      _ <- comments
    } yield {
      println("Seeding completed")
    }
  }

}
