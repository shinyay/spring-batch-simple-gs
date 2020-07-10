package com.google.shinyay.configuration

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Configuration

@Configuration
class BatchConfiguration(jobBuilderFactory: JobBuilderFactory,
                         stepBuilderFactory: StepBuilderFactory) {
}