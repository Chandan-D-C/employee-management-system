package com.chandan.employeemanagement.service;


import com.chandan.employeemanagement.entity.Employee;
import com.chandan.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.chandan.employeemanagement.exception.EmployeeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
        public Employee addEmployee(Employee employee) {

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists: " + employee.getEmail()
            );
        }

        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));

    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(id);
        if (!existingEmployee.getEmail().equalsIgnoreCase(updatedEmployee.getEmail())
        && employeeRepository.existsByEmail(updatedEmployee.getEmail())) {
    throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Email already exists: " + updatedEmployee.getEmail()
    );
}

        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
    public List<Employee> searchEmployeesByFirstName(String firstName) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCase(firstName);
    }

    public List<Employee> searchEmployeesByDepartment(String department) {
        return employeeRepository
                .findByDepartmentIgnoreCase(department);
    }
}