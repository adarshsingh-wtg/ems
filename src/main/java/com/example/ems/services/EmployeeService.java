package com.example.ems.services;

import com.example.ems.models.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
    void addDepartmentToEmployee(Long id, Long departmentId);
    void removeDepartmentFromEmployee(Long employeeId, Long departmentId);
}
