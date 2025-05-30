package com.example.ems.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDTO {
    private UUID id;
    private String name;
    private List<DepartmentDTO> departments;
}
