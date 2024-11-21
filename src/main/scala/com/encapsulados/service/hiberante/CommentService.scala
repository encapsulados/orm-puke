package com.encapsulados.service.hiberante

import com.encapsulados.model.hibernate.{Comment, Post}
import com.encapsulados.repository.hibernate.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait CommentService {
  def save(comment: Comment)           : Unit
  def countByPostId(post: Post)        : Long
  def deleteAll()                      : Unit
}

@Service
@Transactional
class CommentServiceImpl(commentRepository: CommentRepository) extends CommentService {

  override def save(comment: Comment): Unit            = commentRepository.save(comment)

  override def countByPostId(post: Post): Long         = commentRepository.countByPostId(post.id)

  override def deleteAll(): Unit                       = commentRepository.deleteAll()
}