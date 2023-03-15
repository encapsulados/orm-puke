ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-data-jpa" % "3.0.4"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-validation" % "3.0.4"
//We have added H2, however you can mysql, Postgres
libraryDependencies += "com.h2database" % "h2" % "2.1.214"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-test" % "3.0.4" % Test
lazy val root = (project in file("."))
  .settings(
    name := "SpringScalaHibernateCrud"
  )