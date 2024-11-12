package com.the.sample.app.service

import com.the.sample.app.model.{Author, Post}
import com.the.sample.app.repository.{CustomUserRepository, PostRepository, UserRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait UserService {
  def findById(id: Long): Option[Author]

  def findByEmail(email: String): Option[Author]

  def save(user: Author): Unit

  def deleteById(id: Long): Unit

  def findByEmailDomainOrFullNameStartsWith(domain: String, startingLetter: String): List[Author]

  def findPostsByAuthorId(id: Long): List[Post]
}

@Service
@Transactional
class UserServiceImpl(userRepository: UserRepository,
                      postRepository: PostRepository,
                      customUserRepository: CustomUserRepository) extends UserService{
  import scala.jdk.OptionConverters._
  override def findById(id: Long): Option[Author] = userRepository.findById(id).toScala

  override def findByEmail(email: String): Option[Author] = userRepository.findByEmail(email).toScala

  override def save(user: Author): Unit = userRepository.save(user)

  override def deleteById(id: Long): Unit = userRepository.deleteById(id)

  override def findByEmailDomainOrFullNameStartsWith(domain: String, startingLetter: String): List[Author] =
    customUserRepository.findByEmailDomainOrFullNameStartsWith(domain, startingLetter)

  override def findPostsByAuthorId(id: Long): List[Post] = postRepository.findByAuthorId(id)
}

