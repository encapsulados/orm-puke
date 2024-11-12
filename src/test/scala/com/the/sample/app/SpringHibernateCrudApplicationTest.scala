package com.the.sample.app

import com.the.sample.app.model.{Author, Post}
import com.the.sample.app.repository.CustomUserRepository
import com.the.sample.app.service.{PostService, UserService}
import org.junit.jupiter.api.Assertions.{assertEquals, assertNotNull, assertTrue}
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(Array(classOf[SpringExtension]))
@ContextConfiguration(classes = Array(classOf[SpringHibernateCrudApplication]))
class SpringHibernateCrudApplicationTest {
  @Autowired
  var userService: UserService = null

  @Autowired
  var postService: PostService = null

  @Test
  def testCreateAndFind = {
    userService.save(new Author(username = "palan", email = "palan@encapsulados.io"))
    assertNotNull(userService.findByEmail("palan@encapsulados.io"))

    val value = userService.findByEmailDomainOrFullNameStartsWith("holom", "hola")
    assertTrue(value.isEmpty)
  }

  @Test
  def asdsad = {
    val zeta = new Author(username = "zeta", email = "zeta@encapsulados.io")
    val post = new Post(content = "test content")
    zeta.posts.add(post)

    post.author = zeta
    userService.save(zeta)
    postService.save(post)


    assertEquals(zeta.id, 1)
    assertEquals(zeta.posts.size(), 1)
    assertEquals(userService.findPostsByAuthorId(zeta).size, 1)
  }
}
