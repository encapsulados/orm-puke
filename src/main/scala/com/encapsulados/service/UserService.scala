package com.encapsulados.service

import com.encapsulados.model.{Author, Post}
import com.encapsulados.repository.{CustomUserRepository, PostRepository, UserRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

trait UserService {
  def save(user: Author): Unit

  def saveAll(authors: Author*): Unit

  def deleteAll(): Unit

  def findByEmailDomain(domain: String): List[Author]

  def findPostsByAuthorId(id: Author): List[Post]
}

@Service
@Transactional
class UserServiceImpl(userRepository: UserRepository,
                      postRepository: PostRepository,
                      customUserRepository: CustomUserRepository) extends UserService {

  override def save(user: Author): Unit = userRepository.save(user)

  override def findByEmailDomain(domain: String): List[Author] =
    customUserRepository.findByEmailDomain(domain)

  override def findPostsByAuthorId(id: Author): List[Post] = postRepository.findByAuthor(id).asScala.toList

  override def saveAll(authors: Author*): Unit = userRepository.saveAll(List(authors: _*).asJavaCollection)

  override def deleteAll(): Unit = userRepository.deleteAll()
}