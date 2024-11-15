package repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.{JdbcTemplate, PreparedStatementCreator}
import org.springframework.jdbc.support.{GeneratedKeyHolder, KeyHolder}

import java.sql.{Connection, PreparedStatement}
import scala.io.Source
import scala.language.postfixOps

trait DatabaseSeeder {

  @Autowired
  var jdbcTemplate: JdbcTemplate      = _

  def seed() = {
    val authorLines     = Source.fromResource("sql/authors.csv").getLines().drop(1)
    val postLines       = Source.fromResource("sql/posts.csv").getLines().drop(1)
    val commentsLines   = Source.fromResource("sql/comments.csv").getLines().drop(1)
    val r               = new scala.util.Random()

    val authorIds = authorLines.map { line =>
      val columns = line.split(",")
      val insert = "INSERT INTO author (email, username) VALUES (?, ?)"

      val keyHolder: KeyHolder = new GeneratedKeyHolder()

      val psc = new PreparedStatementCreator {
        override def createPreparedStatement(connection: Connection): PreparedStatement = {
          val ps: PreparedStatement = connection.prepareStatement(insert, Array("id"))
          ps.setString(1, columns(0).trim)
          ps.setString(2, columns(1).trim)
          ps
        }
      }

      jdbcTemplate.update(psc, keyHolder)
      keyHolder.getKey().longValue()
    } toList

    val postIds = postLines.map { line =>
      val columns = line.split(",")
      val insert = "INSERT INTO post (content, author_id) VALUES (?, ?)"

      val keyHolder: KeyHolder = new GeneratedKeyHolder()

      val psc = new PreparedStatementCreator {
        override def createPreparedStatement(connection: Connection): PreparedStatement = {
          val ps: PreparedStatement = connection.prepareStatement(insert, Array("id"))
          ps.setString(1, columns(0).trim)
          ps.setString(2, r.shuffle(authorIds).head.toString)
          ps
        }
      }

      jdbcTemplate.update(psc, keyHolder)
      keyHolder.getKey().longValue()
    } toList


    commentsLines.foldLeft(List.empty[Long]){(ids, line) =>
      val columns = line.split(",")
      val insert = "INSERT INTO comment (text, author_id, parent_comment_id, post_id) VALUES (?, ?, ?, ?)"

      val keyHolder: KeyHolder = new GeneratedKeyHolder()

      val psc = new PreparedStatementCreator {
        override def createPreparedStatement(connection: Connection): PreparedStatement = {
          val ps: PreparedStatement = connection.prepareStatement(insert, Array("id"))
          ps.setString(1, columns(0).trim)
          ps.setString(2, r.shuffle(authorIds).head.toString)
          ps.setString(3, r.shuffle(ids).headOption.map(_.toString).orNull)
          ps.setString(4, r.shuffle(postIds).head.toString)
          ps
        }
      }

      jdbcTemplate.update(psc, keyHolder)
      ids :+ keyHolder.getKey().longValue()
    }
  }
}
