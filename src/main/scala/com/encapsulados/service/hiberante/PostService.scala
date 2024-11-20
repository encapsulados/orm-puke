package com.encapsulados.service

import com.encapsulados.model.Post
import com.encapsulados.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait PostService {
  def save(post: Post): Unit
  def deleteAll(): Unit
}


@Service
@Transactional
class PostServiceImpl(postRepository: PostRepository) extends PostService {
  override def save(post: Post): Unit = postRepository.save(post)

  override def deleteAll(): Unit      = postRepository.deleteAll()
}