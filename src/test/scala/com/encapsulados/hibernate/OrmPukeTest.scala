package com.encapsulados.hibernate

import com.encapsulados.OrmPukeApplication
import com.encapsulados.model.hibernate.{Author, Comment, Post}
import com.encapsulados.service.PostService
import com.encapsulados.service.hiberante.{AuthorService, CommentService}
import org.junit.jupiter.api.Assertions.{assertEquals, assertNotEquals, assertNull, assertTrue}
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{AfterEach, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.{ContextConfiguration, TestPropertySource}
import repository.hibernate.DatabaseSeeder

import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.language.postfixOps

@ExtendWith(Array(classOf[SpringExtension]))
@ContextConfiguration(classes = Array(classOf[OrmPukeApplication]))
@TestPropertySource(locations = Array("classpath:application.properties"))
class OrmPukeTest extends DatabaseSeeder {
  @Autowired
  var authorService: AuthorService      = _

  @Autowired
  var postService: PostService = _

  @Autowired
  var commentService: CommentService = _

  @AfterEach
  def cleanDatabase(): Unit = {
    authorService.deleteAll()
  }

  @Test
  def findAllAuthors = {
    val zeta  = new Author(username = "zeta", email =  "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val pedro = new Author(username = "pedro", email = "pedro@gmail.com")
    val lucas = new Author(username = "lucas", email = "lucas@gmail.com")
    authorService.saveAll(zeta, palan, pedro, lucas)

    val authors: Seq[Author] = authorService.findAll()
    assertEquals(authors.size, 4)
  }

  @Test
  def findAuthorByUserName = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")

    authorService.save(zeta)

    val zetaDb = authorService.findAuthorByUsername("zeta")

    assertNotEquals(zeta, zetaDb) // aunque deberia serlo!!

    //Nos obliga a verificar propiedad por propiedad.
    assertEquals(zeta.username, zetaDb.username)
    assertEquals(zeta.email, zetaDb.email)

  }

  @Test
  def findNonExistantUser = {
    val author = authorService.findAuthorByUsername("cualquiera")

    assertNull(author) //El ORM a este nivel devuelve null, y no Option
  }

  @Test
  def findAuthorsByDomain = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val pedro = new Author(username = "pedro", email = "pedro@gmail.com")
    val lucas = new Author(username = "lucas", email = "lucas@gmail.com")
    authorService.saveAll(zeta, palan, pedro, lucas)

    val authors: Seq[Author] = authorService.findByEmailDomain("encapsulados.io")
    assertEquals(authors.size, 2)
  }


  @Test
  def countCommentsByPostId = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val mariano = new Author(username = "mariano", email = "mariano@encapsulados.io")
    val post = new Post(content = "Loro aprende a decir como vas? y lo ascienden a scrum master")
    val oneComment = new Comment(text = "altos marxistas son ustedes. Aguante scrum a mi me hace feliz")
    val anotherComment = new Comment(text = "Certificate y luego vemos de scrum")


    authorService.save(zeta)
    authorService.save(palan)
    authorService.save(mariano)

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

    assertEquals(2, commentService.countByPostId(post))
  }

  @Test
  def totalCommentsPerPost = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val mariano = new Author(username = "mariano", email = "mariano@encapsulados.io")
    val post = new Post(content = "Loro aprende a decir como vas? y lo ascienden a scrum master")
    val oneComment = new Comment(text = "altos marxistas son ustedes. Aguante scrum a mi me hace feliz")
    val anotherComment = new Comment(text = "Certificate y luego vemos de scrum")


    authorService.save(zeta)
    authorService.save(palan)
    authorService.save(mariano)

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

    val map: Map[Long, Int] = authorService.findAll()
      .map(_.comments)
      .flatMap(_.asScala)
      .groupBy(_.post.id)
      .map{ case (k, v) => k -> v.size}

    assertTrue(map.keys.size == 1)
    assertEquals(map(post.id), 2)

  }

  @Test
  def totalCommentsPerPostQueryCriteria = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val mariano = new Author(username = "mariano", email = "mariano@encapsulados.io")
    val post = new Post(content = "Loro aprende a decir como vas? y lo ascienden a scrum master")
    val oneComment = new Comment(text = "altos marxistas son ustedes. Aguante scrum a mi me hace feliz")
    val anotherComment = new Comment(text = "Certificate y luego vemos de scrum")


    authorService.save(zeta)
    authorService.save(palan)
    authorService.save(mariano)

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

    val map: Map[Long, Long] = commentService.countCommentsPerPost()

    assertTrue(map.keys.size == 1)
    assertEquals(map(post.id), 2)

  }


}
