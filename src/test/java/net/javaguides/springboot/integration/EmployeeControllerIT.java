package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Testcontainers
public class EmployeeControllerIT /*extends AbstractionBaseTest*/{

    //@Container
    //private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

    //change the values in application-test.properties dynamically

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.datasource.username}")
    private String username;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void createEmp() throws Exception {


        System.out.println(username+"yui");
        //System.out.println(mySQLContainer.getUsername() + "nish");
        //System.out.println(mySQLContainer.getJdbcUrl());

        Employee e = Employee.builder().firstName("E").lastName("R").email("pandoan@he.com").build();
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e)));
        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(e.getLastName())));
    }

    @Test
    public void testGet() throws Exception {
        Employee e = Employee.builder().firstName("E").lastName("R").email("he@he.com").build();
        Employee e1 = Employee.builder().firstName("E").lastName("R").email("he@he.com").build();
        employeeRepository.saveAndFlush(e);
        employeeRepository.saveAndFlush(e1);
        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    public void testGetOne() throws Exception {
        Employee e = Employee.builder().firstName("E").lastName("R").email("he@he.com").build();
        Employee e1 = employeeRepository.saveAndFlush(e);

        ResultActions response =
                mockMvc.perform(get("/api/employees/{id}", e1.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(e1.getFirstName())));
    }

    @Test
    public void testGetNone() throws Exception {


        ResultActions response =
                mockMvc.perform(get("/api/employees/{id}", 2L));

        response.andExpect(status().isNotFound());

    }

    @Test
    public void testUpdateEmp() throws Exception {
        Employee e = Employee.builder().firstName("E").lastName("R").email("he@he.com").build();
        Employee e1 = employeeRepository.saveAndFlush(e);
        long employeeId = e1.getId();

        e1.setFirstName("mogamba");
        ResultActions response =
                mockMvc.perform(put("/api/employees/{id}", e1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e1)));

        response.andExpect(jsonPath("$.firstName", CoreMatchers.is("mogamba")));


    }

    @Test
    public void cannotUpdate() throws Exception {
        Employee e = Employee.builder().firstName("E").lastName("R").email("he@he.com").build();
        Employee e1= employeeRepository.saveAndFlush(e);

        e1.setFirstName("hey");

        ResultActions response
                = mockMvc.perform(put("/api/employees/{id}",992)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e1)));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void deldel() throws Exception {
        Employee e = Employee.builder().firstName("heu").lastName("yo").email("2@2.com").build();
        Employee e1= employeeRepository.saveAndFlush(e);

        ResultActions response
                = mockMvc.perform(delete("/api/employees/{id}",e1.getId()));

        response.andExpect(status().isOk());


    }


}
