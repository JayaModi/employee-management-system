package com.ibm.assignment.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String department;
    private int age;
    private String email;
    private BigDecimal salary;
}
