package com.encapsulados.repository.hibernate

import com.encapsulados.model.hibernate.Author
import jakarta.persistence.criteria._
import jakarta.persistence.{EntityManager, PersistenceContext}
import org.springframework.stereotype.Repository

import scala.jdk.CollectionConverters._

@Repository
class CustomAuthorRepository {

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
