package net.javaguides.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployeeCtrlmethod() throws Exception {
        Employee e = Employee.builder()
                .firstName("r")
                .lastName("b")
                .email("hi@hi.com")
                .id(3L)
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));


        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(e.getFirstName())));
    }

    @Test
    public void testGetAll() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("r").lastName("rr").email("2@2.com").build());
        listOfEmployees.add(Employee.builder().firstName("r1").lastName("rr1").email("1@1.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));


    }

    @Test
    public void testGetbyId() throws Exception {
        long employeeId = 1L;

        Employee e = Employee.builder()
                .firstName("heya")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(e));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(e.getFirstName())));
    }

    @Test
    public void testGetNegativeScenario() throws Exception {
        long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        ResultActions response
                = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isNotFound())
                .andDo(print());


    }

    @Test
    public void testUpdateEmpRestApi() throws Exception {
        long employeeId = 1L;

        Employee e = Employee.builder()
                .firstName("Ram")
                .lastName("Fad")
                .email("2@2.com")
                .build();

        Employee e1 = Employee.builder()
                .firstName("RamNaray")
                .lastName("Fad")
                .email("2@2.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(e));

        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response =
                mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e1)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(e1.getFirstName())));

    }

    @Test
    public void testNeg() throws Exception {
        long employeeId = 1L;
        Employee e = Employee.builder()
                .firstName("heya!")
                .lastName("uyo")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        ResultActions resultActions
                = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e)));


        resultActions.andExpect(status().isNotFound());

    }

    @Test
    public void del() throws Exception {
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/{id}",employeeId));

        resultActions.andExpect(status().isOk());
    }

}
