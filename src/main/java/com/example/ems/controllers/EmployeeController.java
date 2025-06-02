package com.example.ems.controllers;

import com.example.ems.models.Employee;
import com.example.ems.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{employeeId}/departments/{departmentId}")
    public ResponseEntity<Void> addDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        employeeService.addDepartmentToEmployee(employeeId, departmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{employeeId}/departments/{departmentId}")
    public ResponseEntity<Void> removeDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        employeeService.removeDepartmentFromEmployee(employeeId, departmentId);
        return ResponseEntity.noContent().build();
    }
}
