package com.chandan.employeemanagement.repository;

import com.chandan.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}