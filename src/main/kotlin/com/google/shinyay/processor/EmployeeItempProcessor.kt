package com.google.shinyay.processor

import com.google.shinyay.entity.Employee
import com.google.shinyay.logger
import org.springframework.batch.item.ItemProcessor

class EmployeeItempProcessor: ItemProcessor<Employee, Employee> {
    override fun process(employee: Employee): Employee? {
        val upperCasedEmployee = Employee(employee.firstName.toUpperCase(),
        employee.lastName.toUpperCase())
        logger.info("Upper cased [$employee] to [$upperCasedEmployee]")
        return upperCasedEmployee
    }
}