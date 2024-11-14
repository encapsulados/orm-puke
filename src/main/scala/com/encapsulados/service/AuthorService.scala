package com.encapsulados.service

import com.encapsulados.model.{Author, Post}
import com.encapsulados.repository.{CustomAuthorRepository, PostRepository, AuthorRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

trait AuthorService {
  def save(user: Author): Unit

  def saveAll(authors: Author*): Unit

  def deleteAll(): Unit

  def findByEmailDomain(domain: String): List[Author]

  def findPostsByAuthorId(id: Author): List[Post]

  def findAll(): List[Author]
}

@Service
@Transactional
class AuthorServiceImpl(userRepository: AuthorRepository,
                        postRepository: PostRepository,
                        customUserRepository: CustomAuthorRepository) extends AuthorService {

  override def save(user: Author): Unit                        = userRepository.save(user)

  override def findByEmailDomain(domain: String): List[Author] = customUserRepository.findByEmailDomain(domain)

  override def findPostsByAuthorId(id: Author): List[Post]     = postRepository.findByAuthor(id).asScala.toList

  override def saveAll(authors: Author*): Unit                 = userRepository.saveAll(List(authors: _*).asJavaCollection)

  override def deleteAll(): Unit                               = userRepository.deleteAll()

  override def findAll(): List[Author]                         = userRepository.findAll().asScala.toList
}