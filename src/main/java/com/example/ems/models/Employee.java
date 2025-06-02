package com.example.ems.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "EMPLOYEE")
@ToString(exclude = "departments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_SEQ")
    @SequenceGenerator(name = "EMPLOYEE_SEQ", sequenceName = "EMPLOYEE_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "MAP_EMPLOYEE_DEPARTMENT",
            joinColumns = @JoinColumn(name = "ID_EMPLOYEE"),
            inverseJoinColumns = @JoinColumn(name = "ID_DEPARTMENT")
    )
    private Set<Department> departments = new HashSet<>();
}
