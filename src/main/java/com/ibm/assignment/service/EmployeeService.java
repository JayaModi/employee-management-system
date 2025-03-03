package com.ibm.assignment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.entity.Employee;
import com.ibm.assignment.exception.ResourceNotFoundException;
import com.ibm.assignment.mapper.EmployeeMapper;
import com.ibm.assignment.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

	@CacheEvict(value = "employees", allEntries = true)
	public EmployeeDTO createEmployee(EmployeeDTO employee) {
		log.debug("Creating emp:{}",employee);
		return mapper.toDTO(employeeRepository.save(mapper.toEntity(employee)));
	}

	public EmployeeDTO getEmployee(Long id) {
		log.debug("fetching emp:{}",id);
		Employee emp = findById(id);
		log.debug("fetched emp:{}",emp);
		EmployeeDTO dto = mapper.toDTO(emp);
		log.debug("mapped emp:{}",dto);
		return dto;
	}

	@Cacheable(value = "employees")
	public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
		log.debug("page:{}",pageable);
		return employeeRepository.findAll(pageable).map(emp -> mapper.toDTO(emp));
	}

	public List<EmployeeDTO> searchEmployee(String query) {
		log.debug("Searching emp for query:{}",query);
		List<Employee> empList = employeeRepository
				.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(query, query);
		log.debug("No. of records found:{}",empList.size());
		return empList.stream().map(emp -> mapper.toDTO(emp)).collect(Collectors.toList());
	}

	
	public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, Long id) {
		log.debug("Emp to update id:{}, dto:{}",id,employeeDTO);
		Employee employee = findById(id);
		employee.setAge(employeeDTO.getAge());
		employee.setDepartment(employeeDTO.getDepartment());
		employee.setEmail(employeeDTO.getEmail());
		employee.setName(employeeDTO.getName());
		employee.setSalary(employeeDTO.getSalary());

		return mapper.toDTO(employeeRepository.save(employee));
	}

	@CacheEvict(value = "employees", allEntries = true)
	public void deleteEmployee(Long id) {
		log.debug("Emp to be deleted:{}",id);
		findById(id);
		log.debug("Emp is present, proceeding for deletion");
		employeeRepository.deleteById(id);
	}
	
	private Employee findById(Long id) {
		return employeeRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " not found"));
	}

}
