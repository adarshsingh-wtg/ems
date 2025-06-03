package com.example.ems.services;

import com.example.ems.models.Department;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    Department createDepartment(Department department);
    List<Department> getAllDepartments();
    Department updateDepartment(Department department);
    void deleteDepartment(Long id);
}
