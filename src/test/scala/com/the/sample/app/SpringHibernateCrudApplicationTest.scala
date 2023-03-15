package com.the.sample.app

import com.the.sample.app.model.User
import com.the.sample.app.service.UserService
import org.junit.jupiter.api.Assertions.assertNotNull
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
  @Test
  def testCreateAndFind = {
    userService.save(new User(fullName = "test user", email = "test@test.com"))
    assertNotNull(userService.findByEmail("test@test.com"))
  }
}
