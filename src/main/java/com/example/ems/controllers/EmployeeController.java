package com.example.ems.controllers;

import com.example.ems.dtos.EmployeeDTO;
import com.example.ems.models.Employee;
import com.example.ems.services.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable UUID id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable UUID id, @RequestBody Employee employee) {
        try{
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updatedEmployee);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{employeeId}/departments/{departmentId}")
    public ResponseEntity<Void> addDepartment(@PathVariable UUID employeeId, @PathVariable UUID departmentId) {
        try {
            employeeService.addDepartmentToEmployee(employeeId, departmentId);
            return ResponseEntity.ok().build();
        }
        catch (EntityNotFoundException e){
            return  ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{employeeId}/departments/{departmentId}")
    public ResponseEntity<Void> removeDepartment(@PathVariable UUID employeeId, @PathVariable UUID departmentId) {
        try {
            employeeService.removeDepartmentFromEmployee(employeeId, departmentId);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException e){
            return  ResponseEntity.notFound().build();
        }
    }
}
