package com.example.ems.mappers;

import com.example.ems.dtos.DepartmentDTO;
import com.example.ems.dtos.EmployeeDTO;
import com.example.ems.models.Employee;
import com.example.ems.models.Department;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {
    public static DepartmentDTO toDepartmentDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setReadOnly(department.isReadOnly());
        dto.setMandatory(department.isMandatory());
        return dto;
    }

    public static EmployeeDTO toEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        List<DepartmentDTO> departments = employee.getDepartments()
                .stream()
                .map(DtoMapper::toDepartmentDTO)
                .collect(Collectors.toList());
        dto.setDepartments(departments);
        return dto;
    }
}