package com.encapsulados.service

import com.encapsulados.model.Author
import com.encapsulados.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

trait AuthorService {
  def save(user: Author): Unit

  def saveAll(authors: Author*): Unit

  def deleteAll(): Unit

  def findAll(): List[Author]
}

@Service
@Transactional
class AuthorServiceImpl(authorRepository: AuthorRepository) extends AuthorService {

  override def save(author: Author): Unit                      = authorRepository.save(author)

  override def saveAll(authors: Author*): Unit                 = authorRepository.saveAll(List(authors: _*).asJavaCollection)

  override def deleteAll(): Unit                               = authorRepository.deleteAll()

  override def findAll(): List[Author]                         = authorRepository.findAll().asScala.toList

}