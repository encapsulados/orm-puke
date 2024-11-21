package repository.slick

import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Source}
import scala.language.postfixOps
import slick.jdbc.JdbcBackend.Database

trait DatabaseSeeder {

  val r                               = new scala.util.Random()

  def cleanup()(implicit ec: ExecutionContext, db: Database): Future[Unit] = {
    for {
      _ <- db.run(sqlu"DELETE FROM comment")
      _ <- db.run(sqlu"DELETE FROM post")
      _ <- db.run(sqlu"DELETE FROM author")
    } yield ()
  }

  def seed()(implicit ec: ExecutionContext, db: Database): Future[Unit] = {

    val authorsSource: BufferedSource   = Source.fromResource("sql/authors.csv")
    val postsSource: BufferedSource     = Source.fromResource("sql/posts.csv")
    val commentsSource: BufferedSource  = Source.fromResource("sql/comments.csv")

    val authorLines: Iterator[String]   = authorsSource.getLines().drop(1)
    val postLines: Iterator[String]     = postsSource.getLines().drop(1)
    val commentsLines: Iterator[String] = commentsSource.getLines().drop(1)

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
      authorsSource.close()
      postsSource.close()
      commentsSource.close()
      println("Seeding completed")
    }
  }

}
