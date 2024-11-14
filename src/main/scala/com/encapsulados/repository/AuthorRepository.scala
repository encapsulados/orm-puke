package com.encapsulados.repository

import com.encapsulados.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.Optional

@Repository
trait AuthorRepository extends JpaRepository[Author,Long]
