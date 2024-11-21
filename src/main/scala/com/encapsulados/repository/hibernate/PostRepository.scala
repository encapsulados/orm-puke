package com.encapsulados.repository.hibernate

import com.encapsulados.model.hibernate.{Author, Post}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util

@Repository
trait PostRepository extends JpaRepository[Post, Long]{
  def findByAuthor(author: Author): util.List[Post]
}
