package com.encapsulados.repository

import com.encapsulados.model.{Comment, Post}
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CommentRepository extends JpaRepository[Comment, Long] {
  def findByPost(post: Post): java.util.List[Comment]

  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
  def countByPostId(@Param("postId") postId: Long): Long
}