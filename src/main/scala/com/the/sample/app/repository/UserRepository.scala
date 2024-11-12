package com.the.sample.app.repository

import com.the.sample.app.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.Optional

@Repository
trait UserRepository extends JpaRepository[Author,Long]{
  def findByEmail(email: String): Optional[Author]
}
