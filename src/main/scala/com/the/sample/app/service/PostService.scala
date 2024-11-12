package com.the.sample.app.service

import com.the.sample.app.model.Post
import com.the.sample.app.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait PostService {
  def save(post: Post): Unit
}


@Service
@Transactional
class PostServiceImpl(postRepository: PostRepository) extends PostService {
  override def save(post: Post): Unit = postRepository.save(post)
}