package com.example.ems.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Employee")
@ToString(exclude = "departments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "MAPEMPLOYEEDEPARTMENT",
            joinColumns = @JoinColumn(name = "IDEMPLOYEE"),
            inverseJoinColumns = @JoinColumn(name = "IDDEPARTMENT")
    )
    private Set<Department> departments = new HashSet<>();
}
