package com.google.shinyay.configuration

import com.google.shinyay.entity.Employee
import com.google.shinyay.listener.JobNotificationListener
import com.google.shinyay.processor.EmployeeItempProcessor
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.support.DatabaseType
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
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

    @Bean
    fun job1(listener: JobNotificationListener, step1: Step): Job {
        return jobBuilderFactory["EmployeeJob"]
                .incrementer(RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build()
    }

//    @Bean
//    fun repository(): JobRepository {
//        val factory: JobRepositoryFactoryBean =
//                JobRepositoryFactoryBean().apply {
//                    setDatabaseType(DatabaseType.H2.productName)
//                    setDataSource(EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build())
//
//                    transactionManager = ResourcelessTransactionManager()
//        }
//        return factory.`object`
//    }
//
//    @Bean
//    fun launcher(repository: JobRepository): JobLauncher =
//            SimpleJobLauncher().apply {
//                setJobRepository(repository)
//            }
}