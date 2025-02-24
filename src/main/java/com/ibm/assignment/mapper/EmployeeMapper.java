package com.ibm.assignment.mapper;

import org.mapstruct.Mapper;

import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.entity.Employee;

@Mapper
public interface EmployeeMapper {
	Employee toEntity(EmployeeDTO employeeDTO);
	EmployeeDTO toDTO(Employee employee);
}
