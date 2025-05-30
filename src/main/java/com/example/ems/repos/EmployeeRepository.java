package com.example.ems.repos;

import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByNameIgnoreCase(String name);
    List<Employee> findAllByDepartmentsContains(Department department);
}
