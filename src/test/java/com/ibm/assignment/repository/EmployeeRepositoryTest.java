package com.ibm.assignment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ibm.assignment.entity.Employee;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1, employee2;

    @BeforeEach
    void setUp() {
        // Creating employees for testing
        employee1 = new Employee(null, "Alice Brown", "IT", 28, "alice@example.com", new BigDecimal("50000"), LocalDateTime.now(), LocalDateTime.now());
        employee2 = new Employee(null, "Bob Smith", "Finance", 35, "bob@example.com", new BigDecimal("70000"),  LocalDateTime.now(), LocalDateTime.now());

        // Save initial data
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    void testSaveEmployee() {
        Employee newEmployee = new Employee(null, "Charlie Davis", "HR", 30, "charlie@example.com", new BigDecimal("55000"),  LocalDateTime.now(), LocalDateTime.now());
        Employee savedEmployee = employeeRepository.save(newEmployee);

        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo("Charlie Davis");
    }

    @Test
    void testFindById() {
        Optional<Employee> foundEmployee = employeeRepository.findById(employee1.getId());

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo("Alice Brown");
    }

    @Test
    void testFindAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        assertThat(employees).hasSize(2);
    }

    @Test
    void testFindByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase() {
        List<Employee> result = employeeRepository.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase("alice", "alice");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice Brown");
    }

    @Test
    void testDeleteEmployee() {
        employeeRepository.deleteById(employee1.getId());

        Optional<Employee> deletedEmployee = employeeRepository.findById(employee1.getId());
        assertThat(deletedEmployee).isEmpty();
    }
}

