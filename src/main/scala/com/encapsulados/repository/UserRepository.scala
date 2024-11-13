package com.encapsulados.repository

import com.encapsulados.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.Optional

@Repository
trait UserRepository extends JpaRepository[Author,Long]{
  def findByEmail(email: String): Optional[Author]
}
