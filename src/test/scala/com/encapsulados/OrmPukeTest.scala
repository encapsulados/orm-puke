package com.encapsulados

import com.encapsulados.model.{Author, Comment, Post}
import com.encapsulados.service.{AuthorService, CommentService, PostService}
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer.Random
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{AfterEach, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.{JdbcTemplate, PreparedStatementCreator}
import org.springframework.jdbc.support.{GeneratedKeyHolder, KeyHolder}
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.{ContextConfiguration, TestPropertySource}

import java.sql.{Connection, PreparedStatement}
import scala.io.Source
import scala.language.postfixOps

@ExtendWith(Array(classOf[SpringExtension]))
@ContextConfiguration(classes = Array(classOf[OrmPukeApplication]))
@TestPropertySource(locations = Array("classpath:application.properties"))
class OrmPukeTest {
  @Autowired
  var userService: AuthorService      = _
  @Autowired
  var postService: PostService        = _
  @Autowired
  var commentService: CommentService  = _
  @Autowired
  var jdbcTemplate: JdbcTemplate      = _

  @AfterEach
  def cleanDatabase(): Unit = {
    userService.deleteAll()
    postService.deleteAll()
    commentService.deleteAll()
  }


  @Test
  def findAll = {
    val zeta  = new Author(username = "zeta", email =  "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val pedro = new Author(username = "pedro", email = "pedro@gmail.com")
    val lucas = new Author(username = "lucas", email = "lucas@gmail.com")
    userService.saveAll(zeta, palan, pedro, lucas)

    val authors: Seq[Author] = userService.findAll()
    assertEquals(authors.size, 4)
  }

  @Test
  def getAllUsersByDomain = {
    val zeta  = new Author(username = "zeta", email =  "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val pedro = new Author(username = "pedro", email = "pedro@gmail.com")
    val lucas = new Author(username = "lucas", email = "lucas@gmail.com")
    userService.saveAll(zeta, palan, pedro, lucas)

    val authors: Seq[Author] = userService.findByEmailDomain("encapsulados.io")
    assertEquals(authors.size, 2)
  }


  @Test
  def authorWithPost = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val post = new Post(content = "test content")
    zeta.posts.add(post)

    post.author = zeta
    userService.save(zeta)
    postService.save(post)

    assertEquals(userService.findPostsByAuthorId(zeta).size, 1)
    assertEquals(userService.findPostsByAuthorId(zeta).head.content, "test content")
  }

  @Test
  def authorWithPostAndComments = {
    val zeta           = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan          = new Author(username = "palan", email = "palan@encapsulados.io")
    val mariano        = new Author(username = "mariano", email = "mariano@encapsulados.io")
    val post           = new Post(content = "Loro aprende a decir como vas? y lo ascienden a scrum master")
    val oneComment     = new Comment(text = "altos marxistas son ustedes. Aguante scrum a mi me hace feliz")
    val anotherComment = new Comment(text = "Certificate y luego vemos de scrum")


    userService.save(zeta)
    userService.save(palan)
    userService.save(mariano)

    zeta.posts.add(post)
    post.author = zeta
    post.comments.add(oneComment)
    post.comments.add(anotherComment)
    zeta.comments.add(oneComment)
    palan.comments.add(anotherComment)
    oneComment.author = zeta
    oneComment.post = post
    anotherComment.post = post
    anotherComment.author = mariano

    postService.save(post)
    commentService.save(oneComment)
    commentService.save(anotherComment)

    assertEquals(commentService.findByPost(post).size, 2)
    assertEquals(commentService.findByPost(post).size, commentService.countByPostId(post))

  }

  @Test
  def commentsPerPost = {
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
          ps.setString(2, scala.util.Random.shuffle(authorIds).head.toString)
          ps
        }
      }

      jdbcTemplate.update(psc, keyHolder)
      keyHolder.getKey().longValue()
    } toList

    commentsLines.foreach { line =>
      val columns = line.split(",")
      val insert = "INSERT INTO comment (text, author_id, parent_comment_id, post_id) VALUES (?, ?, ?, ?)"
      jdbcTemplate.update(insert, columns(0), columns(1), columns(2), columns(3))
    }


  }
}
