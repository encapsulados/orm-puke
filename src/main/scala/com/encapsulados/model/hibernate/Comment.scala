package com.encapsulados.model.hibernate

import jakarta.persistence._

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "comment")
class Comment(@BeanProperty
              @Column(name = "text", nullable = false)
              var text: String,
             ) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  var id: Long = _


  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  var post: Post = _


  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  var author: Author = _

  @ManyToOne
  @JoinColumn(name = "parent_comment_id")
  var parentComment: Comment = _


  @OneToMany(mappedBy = "parentComment", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var replies: util.List[Comment] = new util.ArrayList[Comment]()


  def this() = this(null)
}
