package com.google.shinyay.listener

import com.google.shinyay.entity.Employee
import com.google.shinyay.logger
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class JobNotificationListener(val jdbcTemplate: JdbcTemplate) : JobExecutionListenerSupport() {
    override fun afterJob(jobExecution: JobExecution) {
        if(jobExecution.status == BatchStatus.COMPLETED) {
            logger.info("JOB COMPLETED")
            jdbcTemplate.query("SELECT first_name, last_name FROM employee"){
                rs: ResultSet, rowNum: Int ->
                Employee(
                        rs.getString(1),
                        rs.getString(2)
                )
            }.forEach{ employee -> logger.info("Found $employee in DB")}
        }
    }
}