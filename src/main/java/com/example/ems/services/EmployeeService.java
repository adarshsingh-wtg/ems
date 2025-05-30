package com.example.ems.services;

import com.example.ems.dtos.EmployeeDTO;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import com.example.ems.mappers.DtoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByNameIgnoreCase(employee.getName())) {
            throw new IllegalArgumentException("Employee name already exists");
        }

        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        mandatoryDepartments.forEach(department -> {employee.getDepartments().add(department);});

        if (employee.getDepartments().stream().noneMatch(Department::isReadOnly)) {
            throw new IllegalArgumentException("Employee must belong to at least one read-only department");
        }
        return employeeRepository.save(employee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees =  employeeRepository.findAll();
        return employees.stream()
                .map(DtoMapper::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Employee updateEmployee(UUID id, Employee updaatedEmployee) {
        Employee employee = getEmployeeById(id);
        if (!employee.getName().equals(updaatedEmployee.getName()) &&
        employeeRepository.existsByNameIgnoreCase(updaatedEmployee.getName())) {
            throw new IllegalArgumentException("New Employee name already exists");
        }
        employee.setName(updaatedEmployee.getName());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    public void addDepartmentToEmployee(UUID employeeId, UUID departmentId) {
        Employee employee = getEmployeeById(employeeId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        employee.getDepartments().add(department);
        department.getEmployees().add(employee);
        employeeRepository.save(employee);
    }

    public void removeDepartmentFromEmployee(UUID employeeId, UUID departmentId) {
        Employee employee = getEmployeeById(employeeId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        employee.getDepartments().remove(department);
        department.getEmployees().remove(employee);
        employeeRepository.save(employee);
    }
}
