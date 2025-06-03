package com.example.ems.services.impl;


import com.example.ems.exceptions.DepartmentNotFoundException;
import com.example.ems.exceptions.DuplicateDepartmentNameException;
import com.example.ems.exceptions.ReadOnlyDepartmentException;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import com.example.ems.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateDepartmentNameException("Department name already exists");
        }

        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Department updatedDepartment) {
        Department department = getDepartmentById(updatedDepartment.getId());

        if (department.isReadOnly() && updatedDepartment.isReadOnly()) {
            throw new ReadOnlyDepartmentException("Cannot update a department marked as read-only");
        }

        String newName = updatedDepartment.getName();
        if (!department.getName().equals(newName) &&
                departmentRepository.existsByName(newName)) {
            throw new DuplicateDepartmentNameException("Department name already exists");
        }

        department.setName(newName);
        department.setReadOnly(updatedDepartment.isReadOnly());
        department.setMandatory(updatedDepartment.isMandatory());

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        List<Employee> employees = employeeRepository.findAllByDepartmentsContains(department);
        for (Employee e : employees) {
            e.getDepartments().remove(department);
        }
        employeeRepository.saveAll(employees);
        departmentRepository.delete(department);
    }

    private Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " not found"));
    }
}