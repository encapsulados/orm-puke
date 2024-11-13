package com.encapsulados

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EntityScan(basePackages = Array("com.encapsulados"))
@EnableJpaRepositories(basePackages = {
  Array("com.encapsulados")
})
@EnableTransactionManagement
class OrmPukeApplication

object OrmPukeApplication extends App {
  SpringApplication.run(classOf[OrmPukeApplication])
}
