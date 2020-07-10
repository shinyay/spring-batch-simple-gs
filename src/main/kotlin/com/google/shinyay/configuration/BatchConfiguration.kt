package com.google.shinyay.configuration

import com.google.shinyay.entity.Employee
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource


@Configuration
class BatchConfiguration(jobBuilderFactory: JobBuilderFactory,
                         stepBuilderFactory: StepBuilderFactory) {

    @Bean
    fun reader(): FlatFileItemReader<Employee>? = FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .resource(ClassPathResource("employee-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(object : BeanWrapperFieldSetMapper<Employee?>() {
                    init {
                        setTargetType(Employee::class.java)
                    }
                })
                .build()
}