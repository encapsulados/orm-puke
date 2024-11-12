package com.the.sample.app.model

import jakarta.persistence._

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


  def this() = this(null)
}
