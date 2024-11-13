package com.encapsulados.repository

import com.encapsulados.model.{Comment, Post}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait CommentRepository extends JpaRepository[Comment, Long] {
  def findByPost(post: Post): java.util.List[Comment]
}