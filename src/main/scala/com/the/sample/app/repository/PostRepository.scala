package com.the.sample.app.repository

import com.the.sample.app.model.Post
import org.springframework.data.jpa.repository.JpaRepository

trait PostRepository extends JpaRepository[Post, Long]{
  def findByAuthorId(id: Long): List[Post]
}
