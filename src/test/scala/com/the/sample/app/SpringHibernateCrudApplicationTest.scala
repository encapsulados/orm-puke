package com.the.sample.app

import com.the.sample.app.model.{Author, Post}
import com.the.sample.app.repository.CustomUserRepository
import com.the.sample.app.service.{PostService, UserService}
import org.junit.jupiter.api.Assertions.{assertNotNull, assertTrue}
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
  var postService: PostService = null

  @Test
  def testCreateAndFind = {
    userService.save(new Author(username = "test user", email = "test@test.com"))
    assertNotNull(userService.findByEmail("test@test.com"))
    val value = userService.findByEmailDomainOrFullNameStartsWith("holom", "hola")
    assertTrue(value.isEmpty)
  }

  @Test
  def asdsad = {
    postService.save(new Post(content = "test content", author = new Author(username = "test user", email = "))
  }
}
