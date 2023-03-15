package com.the.sample.app.model

import jakarta.persistence.{Column, Entity, GeneratedValue, Id, Table}
import scala.beans.BeanProperty
@Entity
@Table(name = "UserData")
class User (@BeanProperty
            @Column(name = "fullName", length = 40, nullable = false)
            var fullName: String,
            @Column(name = "email", length = 50, nullable = false, unique = true)
            @BeanProperty var email: String){
  @Id
  @GeneratedValue
  @Column(name = "identifier")
  var id: Long = _
  def this() = this(null,null)
}
