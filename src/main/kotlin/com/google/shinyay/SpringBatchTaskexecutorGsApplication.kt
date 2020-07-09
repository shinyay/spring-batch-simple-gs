package com.google.shinyay

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class SpringBatchTaskexecutorGsApplication

fun main(args: Array<String>) {
	runApplication<SpringBatchTaskexecutorGsApplication>(*args)
}
