package com.encapsulados.repository.hibernate

import com.encapsulados.model.hibernate.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait AuthorRepository extends JpaRepository[Author,Long] {
  def findByUsername(username: String): Author
}
