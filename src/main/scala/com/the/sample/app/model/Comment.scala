//package com.the.sample.app.model
//
//import jakarta.persistence.{CascadeType, Column, Entity, GeneratedValue, Id, JoinColumn, ManyToOne, OneToMany, Table}
//
//import scala.beans.BeanProperty
//
//
//@Entity
//@Table(name = "comment")
//class Comment(@BeanProperty
//              @Column(name = "user_id", nullable = false)
//              var userId: String,
//              @ManyToOne
//              @JoinColumn(name = "post_id")
//              var post: Post,
////              @ManyToOne
////              @JoinColumn(name = "parent_comment_id")
////              var parentComment: Comment,
////              @OneToMany(mappedBy = "parentComment", cascade = Array(CascadeType.ALL), orphanRemoval = true)
////              var replies: java.util.List[Comment] = new java.util.ArrayList[Comment]()
//
//             ) {
//  @Id
//  @GeneratedValue
//  @Column(name = "id")
//  var id: Long = _
//
//  def this() = this(null, null)
//}
