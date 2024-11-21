package com.encapsulados.repository.hibernate

import com.encapsulados.model.hibernate.{Comment, Post}
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.{CriteriaBuilder, CriteriaQuery, Root}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import scala.jdk.CollectionConverters._

@Repository
class CustomCommentRepository @Autowired()(private val entityManager: EntityManager) {
  def countCommentsPerPost(): Map[Long, Long] = {
    val criteriaBuilder: CriteriaBuilder = entityManager.getCriteriaBuilder
    val criteriaQuery: CriteriaQuery[Array[AnyRef]] = criteriaBuilder.createQuery(classOf[Array[AnyRef]])
    val root: Root[Comment] = criteriaQuery.from(classOf[Comment])

    criteriaQuery.multiselect(root.get("post"), criteriaBuilder.count(root))
    criteriaQuery.groupBy(root.get("post"))

    val results: List[Array[AnyRef]] = entityManager.createQuery(criteriaQuery).getResultList.asScala.toList

    results.map { result =>
      val post = result(0).asInstanceOf[Post].id
      val count = result(1).asInstanceOf[Long]
      post -> count
    }.toMap
  }

}