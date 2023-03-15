package com.the.sample.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EntityScan(basePackages = Array("com.the.sample.app"))
@EnableJpaRepositories(basePackages = {
  Array("com.the.sample.app")
})
@EnableTransactionManagement
class SpringHibernateCrudApplication

object SpringHibernateCrudApplication extends App{
  SpringApplication.run(classOf[SpringHibernateCrudApplication])
}
