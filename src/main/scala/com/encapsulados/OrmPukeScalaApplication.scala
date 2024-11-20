package com.encapsulados

import slick.jdbc.JdbcBackend.Database

object OrmPukeScalaApplication extends App {

  private val database = Database.forConfig("db")

  println("Hello, Scala!")
}
