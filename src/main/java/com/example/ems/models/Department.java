package com.example.ems.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "DEPARTMENT")
@ToString(exclude = "employees")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_SEQ")
    @SequenceGenerator(name = "DEPARTMENT_SEQ", sequenceName = "DEPARTMENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "READ_ONLY")
    private boolean readOnly = false;

    @Column(name = "MANDATORY")
    private boolean mandatory = false;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

}
