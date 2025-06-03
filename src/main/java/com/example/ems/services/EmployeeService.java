package com.example.ems.services;

import com.example.ems.models.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee updateEmployeeDepartment(Employee employee);
    void deleteEmployee(Long id);
}
