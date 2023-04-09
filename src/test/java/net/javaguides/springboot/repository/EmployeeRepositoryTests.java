package net.javaguides.springboot.repository;

import net.javaguides.springboot.integration.AbstractionBaseTest;
import net.javaguides.springboot.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("testrepoz")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTests extends AbstractionBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee e;

    @BeforeEach
    public void setup()
    {
         e = Employee.builder()
                .firstName("Ramess")
                .lastName("fa")
                .email("rr@rr.com")
                .build();
    }

    @Test
    @DisplayName("Junit test for save employee operations")
    public void givenEmployeeObject_whenEmployeeSaved_ReturnNewEmployee()
    {


        Employee saved = employeeRepository.saveAndFlush(e);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);

    }

    @Test
    @DisplayName("Junit test for get all employees")
    public void getAllEmps()
    {


        Employee e1 = Employee.builder()
                .firstName("Ramess")
                .lastName("fa")
                .email("rr@rr.com")
                .build();

        employeeRepository.save(e);
        employeeRepository.save(e1);

        List<Employee> emps=employeeRepository.findAll();

        assertThat(emps).isNotNull();
        assertThat(emps.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("get by id")
    public void getById()
    {


        Employee e2 = employeeRepository.save(e);

        Employee ret = employeeRepository.findById(e.getId()).get();

        assertThat(ret).isNotNull();
        assertThat(ret.getId()).isEqualTo(e2.getId());
    }

    @Test
    public void getByEmail()
    {


        Employee e2 = employeeRepository.save(e);

        Employee e3=employeeRepository.findByEmail("rr@rr.com").get();

        assertThat(e3.getEmail()).isEqualTo(e2.getEmail());
    }

    @Test
    public void updateOp()
    {


        Employee e2=employeeRepository.save(e);
        long id = e2.getId();
        e2.setEmail("rrr@rrr.com");
        e2=employeeRepository.save(e2);
        assertThat(e2.getEmail()).isEqualTo("rrr@rrr.com");
        assertThat(id).isEqualTo(e2.getId());
    }

    @Test
    public void del()
    {

        employeeRepository.save(e);

        employeeRepository.delete(e);

        Optional<Employee> o = employeeRepository.findByEmail("rr@rr.com");
        assertThat(o).isEmpty();
    }

    @Test
    public void testcustom()
    {

        employeeRepository.save(e);

        Employee ee = employeeRepository.findByJPQL("Ramess","fa");

        assertThat(ee.getFirstName()).isEqualTo("Ramess");
    }

    @Test
    public void testNamed()
    {

        employeeRepository.save(e);

        Employee e = employeeRepository.findByJPQLNamed("Ramess","fa");
        assertThat(e.getFirstName()).isEqualTo("Ramess");
    }

    @Test
    public void nativesq()
    {

        employeeRepository.save(e);

        Employee ee = employeeRepository.findByNativeSQL("Ramess","fa");

        assertThat(ee.getFirstName()).isEqualTo("Ramess");
    }

    @Test
    public void testNamedNative()
    {

        employeeRepository.save(e);

        Employee e = employeeRepository.findByNativeSqlNamed("Ramess","fa");
        assertThat(e.getFirstName()).isEqualTo("Ramess");
    }

    @Test
    public void testUpdated()
    {

    }
}
