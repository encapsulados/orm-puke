package com.the.sample.app.repository

import com.the.sample.app.model.{Author, Post}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util
import java.util.List
@Repository
trait PostRepository extends JpaRepository[Post, Long]{
  def findByAuthor(author: Author): util.List[Post]
}
