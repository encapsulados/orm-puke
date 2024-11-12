package com.the.sample.app.repository

import com.the.sample.app.model.Author
import jakarta.persistence.{EntityManager, PersistenceContext}
import scala.jdk.CollectionConverters._
import jakarta.persistence.criteria._
import org.springframework.stereotype.Repository

@Repository
class CustomUserRepository {

  @PersistenceContext
  private var entityManager: EntityManager = _

  def findByEmailDomainOrFullNameStartsWith(domain: String, startingLetter: String): List[Author] = {
    val criteriaBuilder: CriteriaBuilder = entityManager.getCriteriaBuilder
    val criteriaQuery = criteriaBuilder.createQuery(classOf[Author])
    val root = criteriaQuery.from(classOf[Author])

    // Create conditions
    val emailCondition = criteriaBuilder.like(root.get("email"), s"%@$domain")
    val nameCondition = criteriaBuilder.like(root.get("fullName"), s"$startingLetter%")

    criteriaQuery.select(root).where(List(criteriaBuilder.or(List(emailCondition, nameCondition): _*)):_*)

    // Execute query
    entityManager.createQuery(criteriaQuery).getResultList.asScala.toList
  }
}
