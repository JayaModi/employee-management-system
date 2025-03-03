package com.ibm.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.entity.Employee;
import com.ibm.assignment.exception.ResourceNotFoundException;
import com.ibm.assignment.mapper.EmployeeMapper;
import com.ibm.assignment.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @MockitoBean
    private EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;
//    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John Doe", "Engineering", 30, "john@example.com", new BigDecimal("60000"), LocalDateTime.now(), LocalDateTime.now());
        employeeDTO = mapper.toDTO(employee);
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);

        assertNotNull(savedEmployee);
        assertEquals("John Doe", savedEmployee.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        EmployeeDTO result = employeeService.getEmployee(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployee_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployee(99L));
    }

    @Test
    void testGetAllEmployees() {
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.getAllEmployees(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testSearchEmployee() {
        when(employeeRepository.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase("John", "John"))
                .thenReturn(List.of(employee));

        List<EmployeeDTO> result = employeeService.searchEmployee("John");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeRepository, times(1))
                .findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase("John", "John");
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedDTO = new EmployeeDTO(1L, "John Doe Updated", "HR", 32, "john.updated@example.com", new BigDecimal("70000"));
        EmployeeDTO result = employeeService.updateEmployee(updatedDTO, 1L);

        assertNotNull(result);
        assertEquals("John Doe Updated", result.getName());
        assertEquals("HR", result.getDepartment());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(99L));
    }
}
