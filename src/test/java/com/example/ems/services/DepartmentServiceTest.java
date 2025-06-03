package com.example.ems.services;

import com.example.ems.exceptions.DepartmentNotFoundException;
import com.example.ems.exceptions.DuplicateDepartmentNameException;
import com.example.ems.exceptions.ReadOnlyDepartmentException;
import com.example.ems.models.Department;
import com.example.ems.models.Employee;
import com.example.ems.repos.DepartmentRepository;
import com.example.ems.repos.EmployeeRepository;
import com.example.ems.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department1;
    private Department department2;
    private Employee employee;

    @BeforeEach
    void setUp() {
        department1 = new Department();
        department1.setId(1L);
        department1.setName("Organisation");
        department1.setReadOnly(true);
        department1.setMandatory(true);

        department2 = new Department();
        department2.setId(2L);
        department2.setName("HR");
        department2.setReadOnly(false);
        department2.setMandatory(false);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Ayush");
        employee.setDepartments(new HashSet<>(Set.of(department1)));
    }

    @Test
    void testCreateDepartment_success() {
        Department dept = new Department();
        dept.setName("Finance");

        when(departmentRepository.existsByName("Finance")).thenReturn(false);
        when(departmentRepository.save(dept)).thenReturn(dept);

        Department result = departmentService.createDepartment(dept);

        assertNotNull(result);
        assertEquals("Finance", result.getName());
        verify(departmentRepository).existsByName("Finance");
        verify(departmentRepository).save(dept);
    }

    @Test
    void testCreateDepartment_duplicateName_throwsException() {
        Department dept = new Department();
        dept.setName("HR");

        when (departmentRepository.existsByName("HR")).thenReturn(true);

        assertThrows(DuplicateDepartmentNameException.class, () -> departmentService.createDepartment(dept));
        verify(departmentRepository).existsByName("HR");
        verify(departmentRepository, never()).save(dept);
    }

    @Test
    void testGetAllDepartments_success() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department1, department2));

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentRepository).findAll();
    }

    @Test
    void testUpdateDepartment_success() {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(2L);
        updatedDepartment.setName("Updated HR");
        updatedDepartment.setReadOnly(false);
        updatedDepartment.setMandatory(true);

        when(departmentRepository.findById(2L)).thenReturn(Optional.of(department2));
        when(departmentRepository.existsByName("Updated HR")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(updatedDepartment);

        Department result = departmentService.updateDepartment(updatedDepartment);

        assertNotNull(result);
        assertEquals("Updated HR", result.getName());
        assertFalse(result.isReadOnly());
        assertTrue(result.isMandatory());
        verify(departmentRepository).findById(2L);
        verify(departmentRepository).existsByName("Updated HR");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment_duplicateName_throwsException() {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(2L);
        updatedDepartment.setName("Updated HR");

        when(departmentRepository.findById(2L)).thenReturn(Optional.of(department2));
        when(departmentRepository.existsByName("Updated HR")).thenReturn(true);

        assertThrows(DuplicateDepartmentNameException.class, () -> departmentService.updateDepartment(updatedDepartment));

        verify(departmentRepository).findById(2L);
        verify(departmentRepository).existsByName("Updated HR");
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testUpdateDepartment_ReadOnly_throwsException() {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setName("Updated Organisation");
        updatedDepartment.setReadOnly(true);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));

        assertThrows(ReadOnlyDepartmentException.class, () -> departmentService.updateDepartment(updatedDepartment));

        verify(departmentRepository).findById(1L);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testDeleteDepartment_success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));
        when(employeeRepository.findAllByDepartmentsContains(department1)).thenReturn(Collections.singletonList(employee));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).findById(1L);
        verify(employeeRepository).findAllByDepartmentsContains(department1);
        verify(employeeRepository).saveAll(anyList());
        verify(departmentRepository).delete(department1);
    }

    @Test
    void testDeleteDepartment_NotFound_throwsException() {
        when(departmentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartment(10L));

        verify(departmentRepository).findById(10L);
        verify(employeeRepository, never()).findAllByDepartmentsContains(any());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testDeleteDepartment_RemovesDepartmentFromEmployees() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));
        when(employeeRepository.findAllByDepartmentsContains(department1)).thenReturn(Collections.singletonList(employee));

        departmentService.deleteDepartment(1L);

        assertTrue(employee.getDepartments().isEmpty());
        verify(employeeRepository).saveAll(anyList());
    }
}
