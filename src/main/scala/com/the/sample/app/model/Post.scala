package com.the.sample.app.model

import jakarta.persistence._

import scala.beans.BeanProperty

@Entity(name= "Post")
@Table(name = "post")
class Post (@BeanProperty
            @Column(name = "content", length = 500, nullable = false)
            var content: String,

            @ManyToOne
            @JoinColumn(name = "author_id")
            var author: Author
           ) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = _


  def this() = this(null, null)
}
