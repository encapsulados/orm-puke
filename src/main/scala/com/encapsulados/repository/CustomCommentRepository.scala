package com.encapsulados.repository

import com.encapsulados.model.{Comment, Post}
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.{CriteriaBuilder, CriteriaQuery, Root}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CustomCommentRepository @Autowired()(private val entityManager: EntityManager) {

  def countByPostId(post: Post): Long = {
    val criteriaBuilder: CriteriaBuilder = entityManager.getCriteriaBuilder
    val criteriaQuery: CriteriaQuery[java.lang.Long] = criteriaBuilder.createQuery(classOf[java.lang.Long])
    val root: Root[Comment] = criteriaQuery.from(classOf[Comment])

    criteriaQuery.select(criteriaBuilder.count(root))
    criteriaQuery.where(List(criteriaBuilder.equal(root.get("post").get("id"), post.id)):_*)

    entityManager.createQuery(criteriaQuery).getSingleResult
  }
}