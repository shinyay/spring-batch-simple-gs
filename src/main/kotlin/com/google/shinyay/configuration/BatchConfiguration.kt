package com.google.shinyay.configuration

import com.google.shinyay.entity.Employee
import com.google.shinyay.processor.EmployeeItempProcessor
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource


@Configuration
class BatchConfiguration(val jobBuilderFactory: JobBuilderFactory,
                         val stepBuilderFactory: StepBuilderFactory) {

    @Bean
    fun reader(): FlatFileItemReader<Employee> = FlatFileItemReaderBuilder<Employee>()
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

    @Bean
    fun processor(): EmployeeItempProcessor = EmployeeItempProcessor()

    @Bean
    fun writer(dataSource: DataSource): JdbcBatchItemWriter<Employee> {
        return JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider<Employee>())
                .sql("INSERT INTO employee (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build()
    }

    @Bean
    fun step1(writer: JdbcBatchItemWriter<Employee?>): Step = stepBuilderFactory.get("step1")
                .chunk<Employee, Employee>(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build()
}