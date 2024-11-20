package com.encapsulados.model

import jakarta.persistence._
import java.util

import scala.beans.BeanProperty
@Entity(name = "Author")
@Table(name = "author")
class Author(@BeanProperty
            @Column(name = "username", length = 40, nullable = false, unique = true)
            var username: String,

            @Column(name = "email", length = 50, nullable = false, unique = true)
            @BeanProperty
            var email: String,
            ){
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  var id: Long = _

  @OneToMany(mappedBy = "author", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var posts: util.List[Post] = new util.ArrayList[Post]()



  @OneToMany(mappedBy = "author", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var comments: util.List[Comment] = new util.ArrayList[Comment]()


  def this() = this(null,null)
}
