package com.example.ems.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "Department")
@ToString(exclude = "employees")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "readonly")
    private boolean readOnly = false;

    @Column(name = "mandatory")
    private boolean mandatory = false;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

}
