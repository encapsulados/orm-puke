package com.encapsulados.hibernate

import com.encapsulados.OrmPukeApplication
import com.encapsulados.model.Author
import com.encapsulados.service.AuthorService
import org.junit.jupiter.api.Assertions.{assertEquals, assertNotEquals, assertNull}
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


}
