package com.example.ems.services.impl;

import com.example.ems.exceptions.DepartmentNotFoundException;
import com.example.ems.exceptions.EmployeeNotFoundException;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import com.example.ems.services.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        mandatoryDepartments.forEach(department -> {employee.getDepartments().add(department);});

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = getEmployeeById(id);
        employee.setName(updatedEmployee.getName());
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    @Override
    public void addDepartmentToEmployee(Long employeeId, Long departmentId) {
        Employee employee = getEmployeeById(employeeId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + departmentId + " not found"));

        employee.getDepartments().add(department);
        department.getEmployees().add(employee);

        employeeRepository.save(employee);
    }

    @Override
    public void removeDepartmentFromEmployee(Long employeeId, Long departmentId) {
        Employee employee = getEmployeeById(employeeId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + departmentId + " not found"));

        employee.getDepartments().remove(department);
        department.getEmployees().remove(employee);

        employeeRepository.save(employee);
    }
}
