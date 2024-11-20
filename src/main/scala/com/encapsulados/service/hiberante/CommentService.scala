package com.encapsulados.service

import com.encapsulados.model.{Comment, Post}
import com.encapsulados.repository.{CommentRepository, CustomCommentRepository, PostRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

trait CommentService {
  def save(comment: Comment)           : Unit
  def findByPost(post: Post)           : List[Comment]
  def countByPostId(post: Post)        : Long
  def deleteAll()                      : Unit
  def findAll()                        : List[Comment]
  def countCommentsPerPost()           : Map[Post, Long]
}

@Service
@Transactional
class CommentServiceImpl(commentRepository: CommentRepository, customCommentRepository: CustomCommentRepository) extends CommentService {
  override def save(comment: Comment): Unit            = commentRepository.save(comment)

  override def countByPostId(post: Post): Long         = commentRepository.countByPostId(post.id)

  override def findByPost(post: Post): List[Comment]   = commentRepository.findByPost(post).asScala.toList

  override def deleteAll(): Unit                       = commentRepository.deleteAll()

  override def countCommentsPerPost(): Map[Post, Long] = customCommentRepository.countCommentsPerPost()

  override def findAll(): List[Comment] = commentRepository.findAll().asScala.toList
}