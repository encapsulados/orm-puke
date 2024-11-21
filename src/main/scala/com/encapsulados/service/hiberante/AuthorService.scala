package com.encapsulados.service

import com.encapsulados.model.Author
import com.encapsulados.model.hibernate.Post
import com.encapsulados.repository.{AuthorRepository, CustomAuthorRepository, PostRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

trait AuthorService {
  def save(user: Author): Unit

  def saveAll(authors: Author*): Unit

  def deleteAll(): Unit

  def findAll(): List[Author]

  def findAuthorByUsername(username: String): Author

  def findByEmailDomain(domain: String): List[Author]

  def findPostsByAuthorId(id: Author): List[Post]


}

@Service
@Transactional
class AuthorServiceImpl(authorRepository: AuthorRepository,
                        postRepository: PostRepository,
                        customUserRepository: CustomAuthorRepository) extends AuthorService {

  override def save(author: Author): Unit                      = authorRepository.save(author)

  override def saveAll(authors: Author*): Unit                 = authorRepository.saveAll(List(authors: _*).asJavaCollection)

  override def deleteAll(): Unit                               = authorRepository.deleteAll()

  override def findAll(): List[Author]                         = authorRepository.findAll().asScala.toList

  override def findAuthorByUsername(username: String): Author  = authorRepository.findByUsername(username)

  override def findByEmailDomain(domain: String): List[Author] = customUserRepository.findByEmailDomain(domain)

  override def findPostsByAuthorId(id: Author): List[Post]     = postRepository.findByAuthor(id).asScala.toList

}