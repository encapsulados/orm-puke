package com.encapsulados.model.hibernate

import jakarta.persistence._

import java.util
import scala.beans.BeanProperty

@Entity(name= "Post")
@Table(name = "post")
class Post (@BeanProperty
            @Column(name = "content", length = 500, nullable = false)
            var content: String
           ) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = _


  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  var author: Author = _


  @OneToMany(mappedBy = "post", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var comments: util.List[Comment] = new util.ArrayList[Comment]()


  def this() = this(null)
}
