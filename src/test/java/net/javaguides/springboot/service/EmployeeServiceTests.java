package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee e;

    @BeforeEach
    public void setUp() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        e = Employee.builder()
                .id(1)
                .firstName("r")
                .lastName("rr")
                .email("hey@hey.com")
                .build();
    }

    @Test
    @DisplayName("junit test for save employee - success")
    public void saveEmployeeMethod() {

        given(employeeRepository.findByEmail(e.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(e))
                .willReturn(e);

        Employee savedEmp = employeeService.saveEmployee(e);

        assertThat(savedEmp.getId()).isEqualTo(1);

    }

    @Test
    @DisplayName("junit test for save employee - throws exception")
    public void throwsException() {
        given(employeeRepository.findByEmail(e.getEmail()))
                .willReturn(Optional.of(e));

        //given(employeeRepository.save(e)).willReturn(e);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(e));

        verify(employeeRepository, never()).save(any(Employee.class));

    }

    @Test
    public void testFindAll() {

        Employee e1 = Employee.builder()
                .firstName("hi")
                .lastName("uo")
                .email("hey@eh.com")
                .id(2L)
                .build();


        List<String> emails = new ArrayList();
        emails.add("hey@hey.com");
        emails.add("hey@eh.com");
        given(employeeRepository.findAll()).willReturn(List.of(e, e1));

        List<Employee> emps = this.employeeService.getAllEmployees();

        for (int i = 0; i < emps.size(); i++) {
            assertThat(emps.get(i).getEmail()).isEqualTo(emails.get(i));
        }


    }

    @Test
    public void findAllNegative()
    {
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        List<Employee> eee = employeeService.getAllEmployees();
        assertThat(eee.size()).isEqualTo(0);
    }

    @Test
    public void getEmpbyId()
    {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(e));
        e = employeeService.getEmployeeById(1L).get();
        assertThat(1L).isEqualTo(e.getId());
    }

    @Test
    public void updatedGet()
    {
        Employee e2 = Employee.builder()
                        .build();
        e2.setEmail("2@2.com");
        e2.setFirstName("lplp");
        given(employeeRepository.save(e)).willReturn(e2);

        Employee ee=employeeService.updateEmployee(e);

        assertThat(ee.getEmail()).isEqualTo("2@2.com");

    }

    @Test
    public void deleteEmp()
    {
        willDoNothing().given(employeeRepository).deleteById(1L);
        employeeService.deleteEmployee(1L);
        verify(employeeRepository,times(1)).deleteById(1L);

    }


}
