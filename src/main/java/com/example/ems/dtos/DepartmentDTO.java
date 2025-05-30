package com.example.ems.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentDTO {
    private UUID id;
    private String name;
    private boolean readOnly;
    private boolean mandatory;
}
