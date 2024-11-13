package com.encapsulados

import com.encapsulados.model.{Author, Comment, Post}
import com.encapsulados.service.{CommentService, PostService, UserService}
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{AfterEach, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.{ContextConfiguration, TestPropertySource}
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(Array(classOf[SpringExtension]))
@ContextConfiguration(classes = Array(classOf[OrmPukeApplication]))
@TestPropertySource(locations = Array("classpath:application.properties"))
class OrmPukeTest {
  @Autowired
  var userService: UserService       = null
  @Autowired
  var postService: PostService       = null
  @Autowired
  var commentService: CommentService = null

  @AfterEach
  def cleanDatabase(): Unit = {
    userService.deleteAll()
    postService.deleteAll()
    commentService.deleteAll()
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
}
