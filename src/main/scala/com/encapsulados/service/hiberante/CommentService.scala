package com.encapsulados.service.hiberante

import com.encapsulados.model.hibernate.{Comment, Post}
import com.encapsulados.repository.hibernate.{CommentRepository, CustomCommentRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

trait CommentService {
  def save(comment: Comment)           : Unit
  def countByPostId(post: Post)        : Long
  def deleteAll()                      : Unit
  def countCommentsPerPost()           : Map[Long, Long]

}

@Service
@Transactional
class CommentServiceImpl(commentRepository: CommentRepository,  customCommentRepository: CustomCommentRepository) extends CommentService {

  override def save(comment: Comment): Unit            = commentRepository.save(comment)

  override def countByPostId(post: Post): Long         = commentRepository.countByPostId(post.id)

  override def deleteAll(): Unit                       = commentRepository.deleteAll()

  override def countCommentsPerPost(): Map[Long, Long] = customCommentRepository.countCommentsPerPost()
}