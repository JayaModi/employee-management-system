package com.ibm.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.assignment.dto.EmployeeDTO;
import com.ibm.assignment.exception.ResourceNotFoundException;
import com.ibm.assignment.service.EmployeeService;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("Engineering");
        employeeDTO.setAge(30);
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setSalary(BigDecimal.valueOf(50000.00));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testGetEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testGetEmployee_NotFound() throws Exception {
        when(employeeService.getEmployee(99L)).thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testUpdateEmployee() throws Exception {
        employeeDTO.setSalary(BigDecimal.valueOf(55000.00));
        when(employeeService.updateEmployee(any(EmployeeDTO.class), eq(1L))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(55000.00));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void testDeleteEmployee_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Employee not found")).when(employeeService).deleteEmployee(99L);

        mockMvc.perform(delete("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }
}
