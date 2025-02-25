package com.ibm.assignment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
	private final EmployeeService employeeService;

	@PostMapping
	ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employee) {
		return ResponseEntity.ok(employeeService.createEmployee(employee));
	}

	@GetMapping("/{id}")
	ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
		return ResponseEntity.ok(employeeService.getEmployee(id));
	}

	@GetMapping
	ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
			@PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
	}

	@GetMapping("/search")
	ResponseEntity<List<EmployeeDTO>> searchEmployee(@RequestParam String query) {
		return ResponseEntity.ok(employeeService.searchEmployee(query));
	}

	@PutMapping("/{id}")
	ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employee, @PathVariable Long id) {
		return ResponseEntity.ok(employeeService.updateEmployee(employee, id));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.ok("Employee deleted successfully.");
	}

}
