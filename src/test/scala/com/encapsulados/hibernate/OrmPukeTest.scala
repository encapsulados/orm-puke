package com.encapsulados.hibernate

import com.encapsulados.OrmPukeApplication
import com.encapsulados.model.{Author, Comment, Post}
import com.encapsulados.service.{AuthorService, CommentService, PostService}
import org.junit.jupiter.api.Assertions.{assertEquals, assertNull}
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{AfterEach, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.{ContextConfiguration, TestPropertySource}
import repository.hibernate.DatabaseSeeder

import scala.language.postfixOps

@ExtendWith(Array(classOf[SpringExtension]))
@ContextConfiguration(classes = Array(classOf[OrmPukeApplication]))
@TestPropertySource(locations = Array("classpath:application.properties"))
class OrmPukeTest extends DatabaseSeeder {
  @Autowired
  var authorService: AuthorService      = _
  @Autowired
  var postService: PostService        = _
  @Autowired
  var commentService: CommentService  = _

  @AfterEach
  def cleanDatabase(): Unit = {
    authorService.deleteAll()
    postService.deleteAll()
    commentService.deleteAll()
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
  def findAllAuthorsByDomain = {
    val zeta  = new Author(username = "zeta", email =  "zeta@encapsulados.io")
    val palan = new Author(username = "palan", email = "palan@encapsulados.io")
    val pedro = new Author(username = "pedro", email = "pedro@gmail.com")
    val lucas = new Author(username = "lucas", email = "lucas@gmail.com")
    authorService.saveAll(zeta, palan, pedro, lucas)

    val authors: Seq[Author] = authorService.findByEmailDomain("encapsulados.io")
    assertEquals(authors.size, 2)
  }


  @Test
  def findPostsByAuthor = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val post = new Post(content = "test content")
    zeta.posts.add(post)

    post.author = zeta
    authorService.save(zeta)
    postService.save(post)

    assertEquals(authorService.findPostsByAuthorId(zeta).size, 1)
    assertEquals(authorService.findPostsByAuthorId(zeta).head.content, "test content")
  }

  @Test
  def findCommentsByPost = {
    val zeta           = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val palan          = new Author(username = "palan", email = "palan@encapsulados.io")
    val mariano        = new Author(username = "mariano", email = "mariano@encapsulados.io")
    val post           = new Post(content = "Loro aprende a decir como vas? y lo ascienden a scrum master")
    val oneComment     = new Comment(text = "altos marxistas son ustedes. Aguante scrum a mi me hace feliz")
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

    assertEquals(commentService.findByPost(post).size, 2)
    assertEquals(commentService.findByPost(post).size, commentService.countByPostId(post))
  }

  @Test
  def concurrentUpdateWithoutVersion(): Unit = {
    // Step 1: Create and save an Author
    val author = new Author(username = "zeta", email = "zeta@encapsulados.io")
    authorService.save(author)

    // Step 2: Simulate first transaction fetching and modifying the Author
    val tx1Author = authorService.findById(author.id).get
    tx1Author.email = "zeta_updated_tx1@encapsulados.io"

    // Step 3: Simulate second transaction fetching and modifying the same Author
    val tx2Author = authorService.findById(author.id).get
    tx2Author.username = "zeta_updated_tx2"

    // Step 4: Save changes from Transaction 1
    authorService.save(tx1Author)

    // Step 5: Save changes from Transaction 2 (overwrites changes from Transaction 1)
    authorService.save(tx2Author)

    val finalAuthor = authorService.findById(author.id).get

    // Expect: The changes from Transaction 1 are lost
    assertEquals(finalAuthor.username, "zeta_updated_tx2")
    assertEquals(finalAuthor.email, "zeta_updated_tx1@encapsulados.io") //this should be the same as tx1Author.email
  }
}
