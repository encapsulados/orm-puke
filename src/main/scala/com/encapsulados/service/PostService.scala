package com.encapsulados.service

import com.encapsulados.model.hibernate.Post
import com.encapsulados.repository.hibernate.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.OptionConverters.RichOptional


trait PostService {
  def save(post: Post): Unit
  def deleteAll(): Unit
  def findById(id: Long): Option[Post]
}


@Service
@Transactional
class PostServiceImpl(postRepository: PostRepository) extends PostService {
  override def save(post: Post): Unit = postRepository.save(post)

  override def deleteAll(): Unit      = postRepository.deleteAll()

  override def findById(id: Long): Option[Post] = postRepository.findById(id).toScala
}