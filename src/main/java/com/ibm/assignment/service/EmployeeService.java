package com.ibm.assignment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.entity.Employee;
import com.ibm.assignment.mapper.EmployeeMapper;
import com.ibm.assignment.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

	public EmployeeDTO createEmployee(EmployeeDTO employee) {
		return mapper.toDTO(employeeRepository.save(mapper.toEntity(employee)));
	}

	public EmployeeDTO getEmployee(Long id) {
		return mapper.toDTO(employeeRepository.findById(id).orElseThrow());
	}

	public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable).map(emp -> mapper.toDTO(emp));
	}

	public List<EmployeeDTO> searchEmployee(String query) {
		List<Employee> empList = employeeRepository
				.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(query, query);
		return empList.stream().map(emp -> mapper.toDTO(emp)).collect(Collectors.toList());
	}

	public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, Long id) {
		Employee employee = employeeRepository.findById(id).orElseThrow();
		employee.setAge(employeeDTO.getAge());
		employee.setDepartment(employeeDTO.getDepartment());
		employee.setEmail(employeeDTO.getEmail());
		employee.setName(employeeDTO.getName());
		employee.setSalary(employeeDTO.getSalary());
		
		return mapper.toDTO(employeeRepository.save(employee));
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

}
