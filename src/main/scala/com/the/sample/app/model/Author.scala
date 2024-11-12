package com.the.sample.app.model

import jakarta.persistence.{CascadeType, Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, Table}
import org.hibernate.`type`.SqlTypes
import org.hibernate.annotations.JdbcTypeCode

import scala.beans.BeanProperty
@Entity(name = "Author")
@Table(name = "author")
class Author(@BeanProperty
            @Column(name = "username", length = 40, nullable = false)
            @JdbcTypeCode(SqlTypes.VARCHAR)  // Explicitly define JDBC type
            var username: String,

            @Column(name = "email", length = 50, nullable = false, unique = true)
            @BeanProperty
            @JdbcTypeCode(SqlTypes.VARCHAR)  // Explicitly define JDBC type
            var email: String,
            ){
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "id")
  var id: Long = _


  @OneToMany(mappedBy = "author", cascade = Array(CascadeType.ALL), orphanRemoval = true)
//  @JoinColumn(name = "author_id")
  var posts: java.util.List[Post] = new java.util.ArrayList[Post]()


  def this() = this(null,null)
}
