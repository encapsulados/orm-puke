package com.encapsulados.repository

import com.encapsulados.model.Author
import jakarta.persistence.{EntityManager, PersistenceContext}

import scala.jdk.CollectionConverters._
import jakarta.persistence.criteria._
import org.springframework.stereotype.Repository

@Repository
class CustomUserRepository {

  @PersistenceContext
  private var entityManager: EntityManager = _

  def findByEmailDomain(domain: String): List[Author] = {
    val criteriaBuilder: CriteriaBuilder = entityManager.getCriteriaBuilder
    val criteriaQuery = criteriaBuilder.createQuery(classOf[Author])
    val root = criteriaQuery.from(classOf[Author])

    // Create conditions
    val emailCondition = criteriaBuilder.like(root.get("email"), s"%@$domain")

    criteriaQuery.select(root).where(List(criteriaBuilder.or(List(emailCondition): _*)):_*)

    // Execute query
    entityManager.createQuery(criteriaQuery).getResultList.asScala.toList
  }
}
