package com.encapsulados.repository

import com.encapsulados.model.{Author, Comment, Post}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util

@Repository
trait PostRepository extends JpaRepository[Post, Long]{
  def findByAuthor(author: Author): util.List[Post]
}
