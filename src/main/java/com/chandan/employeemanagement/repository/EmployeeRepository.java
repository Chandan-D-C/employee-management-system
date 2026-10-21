package com.chandan.employeemanagement.repository;

import com.chandan.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByFirstNameContainingIgnoreCase(String firstName);
    List<Employee> findByDepartmentIgnoreCase(String department);
}