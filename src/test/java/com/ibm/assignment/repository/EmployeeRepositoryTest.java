package com.ibm.assignment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ibm.assignment.entity.Employee;


@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1, employee2;

    @BeforeEach
    void setUp() {
        // Creating employees for testing
        employee1 = new Employee(null, "Test User1", "IT", 28, "test1@example.com", new BigDecimal("50000"), LocalDateTime.now(), LocalDateTime.now());
        employee2 = new Employee(null, "Test User2", "Finance", 35, "test2@example.com", new BigDecimal("70000"),  LocalDateTime.now(), LocalDateTime.now());

        // Save initial data
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    void testSaveEmployee() {
        Employee newEmployee = new Employee(null, "Test User3", "HR", 30, "test3@example.com", new BigDecimal("55000"),  LocalDateTime.now(), LocalDateTime.now());
        Employee savedEmployee = employeeRepository.save(newEmployee);

        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo("Test User3");
    }

    @Test
    void testFindById() {
        Optional<Employee> foundEmployee = employeeRepository.findById(employee1.getId());

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo("Test User1");
    }

    @Test
    void testFindAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        assertThat(employees).hasSize(2);
    }

    @Test
    void testFindByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase() {
        List<Employee> result = employeeRepository.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase("user1", "user1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test User1");
    }

    @Test
    void testDeleteEmployee() {
        employeeRepository.deleteById(employee1.getId());

        Optional<Employee> deletedEmployee = employeeRepository.findById(employee1.getId());
        assertThat(deletedEmployee).isEmpty();
    }
}

