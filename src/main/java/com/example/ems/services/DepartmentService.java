package com.example.ems.services;

import com.example.ems.dtos.DepartmentDTO;
import com.example.ems.mappers.DtoMapper;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        return departmentRepository.save(department);
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments =  departmentRepository.findAll();
        return departments.stream()
                .map(DtoMapper::toDepartmentDTO)
                .collect(Collectors.toList());
    }

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    }


    public Department updateDepartment(UUID id, Department updatedDepartment) {
        Department department = getDepartmentById(id);
        if (!department.getName().equals(updatedDepartment.getName()) &&
                departmentRepository.existsByName(updatedDepartment.getName())) {
            throw new IllegalArgumentException("New department name already exists");
        }
        department.setName(updatedDepartment.getName());
        department.setReadOnly(updatedDepartment.isReadOnly());
        department.setMandatory(updatedDepartment.isMandatory());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(UUID id) {
        Department department = getDepartmentById(id);
        List<Employee> employees = employeeRepository.findAllByDepartmentsContains(department);
        for (Employee e : employees) {
            e.getDepartments().remove(department);
        }
        employeeRepository.saveAll(employees);
        departmentRepository.delete(department);
    }
}