package com.the.sample.app.service

import com.the.sample.app.model.User
import com.the.sample.app.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait UserService {
  def findById(id: Long): Option[User]

  def findByEmail(email: String): Option[User]

  def save(user: User): Unit

  def deleteById(id: Long): Unit
}

@Service
@Transactional
class UserServiceImpl(userRepository: UserRepository) extends UserService{
  import scala.jdk.OptionConverters._
  override def findById(id: Long): Option[User] = userRepository.findById(id).toScala

  override def findByEmail(email: String): Option[User] = userRepository.findByEmail(email).toScala

  override def save(user: User): Unit = userRepository.save(user)

  override def deleteById(id: Long): Unit = userRepository.deleteById(id)
}

