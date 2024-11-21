package com.encapsulados.repository.hibernate

import com.encapsulados.model.hibernate.Comment
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CommentRepository extends JpaRepository[Comment, Long] {

  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
  def countByPostId(@Param("postId") postId: Long): Long
}