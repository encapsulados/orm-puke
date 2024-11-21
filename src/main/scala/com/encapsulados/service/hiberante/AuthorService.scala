package com.encapsulados.service

import com.encapsulados.model.{Author, Post}
import com.encapsulados.repository.{AuthorRepository, CustomAuthorRepository, PostRepository}
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters.RichOptional

trait AuthorService {
  def save(user: Author): Unit

  def saveAll(authors: Author*): Unit

  def deleteAll(): Unit

  def findByEmailDomain(domain: String): List[Author]

  def findPostsByAuthorId(id: Author): List[Post]

  def findPostsByAuthorUsername(username: String): List[Post]

  def findAll(): List[Author]

  def findById(id: Long): Option[Author]
}

@Service
@Transactional
class AuthorServiceImpl(authorRepository: AuthorRepository,
                        postRepository: PostRepository,
                        customUserRepository: CustomAuthorRepository) extends AuthorService {

  override def save(author: Author): Unit                      = authorRepository.save(author)

  override def findByEmailDomain(domain: String): List[Author] = customUserRepository.findByEmailDomain(domain)

  override def findPostsByAuthorId(id: Author): List[Post]     = postRepository.findByAuthor(id).asScala.toList

  override def saveAll(authors: Author*): Unit                 = authorRepository.saveAll(List(authors: _*).asJavaCollection)

  override def deleteAll(): Unit                               = authorRepository.deleteAll()

  override def findAll(): List[Author]                         = authorRepository.findAll().asScala.toList

  override def findPostsByAuthorUsername(username: String): List[Post] = {
    val author = authorRepository.findByUsername(username)
    postRepository.findByAuthor(author).asScala.toList
  }

  override def findById(id: Long): Option[Author] = authorRepository.findById(id).toScala
}