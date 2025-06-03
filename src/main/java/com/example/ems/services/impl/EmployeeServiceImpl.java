package com.example.ems.services.impl;

import com.example.ems.exceptions.DepartmentNotFoundException;
import com.example.ems.exceptions.EmployeeNotFoundException;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import com.example.ems.services.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        employee.getDepartments().addAll(mandatoryDepartments);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        Employee employee = getEmployeeById(updatedEmployee.getId());
        employee.setName(updatedEmployee.getName());

        if(updatedEmployee.getDepartments() != null) {
            Set<Long> currentDepartmentsIds = employee.getDepartments()
                    .stream()
                    .map(Department::getId)
                    .collect(Collectors.toSet());

            Set<Long> newDepartmentIds = updatedEmployee.getDepartments()
                    .stream()
                    .map(Department::getId)
                    .collect(Collectors.toSet());

            Set<Long> departmentsToAdd = newDepartmentIds.stream()
                    .filter(departmentId -> !currentDepartmentsIds.contains(departmentId))
                    .collect(Collectors.toSet());

            Set<Long> departmentsToConsiderForRemoval = currentDepartmentsIds.stream()
                    .filter(departmentId -> !newDepartmentIds.contains(departmentId))
                    .collect(Collectors.toSet());

            Set<Long> departmentsToRemove = departmentsToConsiderForRemoval.stream()
                    .filter(this::isNonMandatoryDepartment)
                    .collect(Collectors.toSet());

            for (Long departmentId: departmentsToAdd) {
                Department department = departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + departmentId + " not found"));
                employee.getDepartments().add(department);
                department.getEmployees().add(employee);
            }

            for (Long departmentId: departmentsToRemove) {
                Department department = departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + departmentId + " not found"));
                employee.getDepartments().remove(department);
                department.getEmployees().remove(employee);
            }

        }
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        if (employee.getDepartments().stream().anyMatch(Department::isMandatory)) {
            throw new IllegalArgumentException("Cannot delete employee present in Mandatory Department");
        }
        employeeRepository.delete(employee);
    }

    private Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    private boolean isNonMandatoryDepartment(Long departmentId) {
        Department d = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + departmentId + " not found"));
        return !d.isMandatory();
    }
}
