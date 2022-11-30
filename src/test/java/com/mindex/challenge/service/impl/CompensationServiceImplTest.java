package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.validator.CompensationValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @MockBean
    private CompensationRepository compensationRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private CompensationValidator compensationValidator;

    @InjectMocks
    private CompensationServiceImpl compensationServiceImpl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup(){
        initMocks(this);
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    /**
     *  create: I   create, employee exists -> works
     *          II  create, employee doesn't exist -> error
     */
    @Test
    public void testCreateCompensation_I() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        compensation.setEmployee(employee);
        doNothing().when(compensationValidator).validateRequiredFields(nullable(Compensation.class));
        doReturn(employee).when(employeeRepository).findByEmployeeId(nullable(String.class));
        try {
            compensationServiceImpl.create(compensation);
        } catch (Exception e) {
            fail("Exception should not be thrown "+ e);
        }
    }

    @Test
    public void testCreateCompensation_II() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        String employeeId = UUID.randomUUID().toString();
        employee.setEmployeeId(employeeId);
        compensation.setEmployee(employee);
        doNothing().when(compensationValidator).validateRequiredFields(nullable(Compensation.class));
        doReturn(null).when(employeeRepository).findByEmployeeId(nullable(String.class));
        try {
            compensationServiceImpl.create(compensation);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("Invalid employeeId: " + employeeId, e.getMessage());
        }
    }



    /**
     *  read:   I   compensation for employee exists -> works
     *          II  compensation for employee doesn't exist -> error
     */

    @Test
    public void testReadCompensation_I() {
        String employeeId = UUID.randomUUID().toString();
        doReturn(new Compensation()).when(compensationRepository).findCompensationByEmployeeEmployeeId(nullable(String.class));
        try {
            compensationServiceImpl.read(employeeId);
        } catch (Exception e) {
            fail("Exception should not be thrown "+ e);
        }
    }

    @Test
    public void testReadCompensation_II() {
        String employeeId = UUID.randomUUID().toString();
        doReturn(null).when(compensationRepository).findCompensationByEmployeeEmployeeId(nullable(String.class));
        try {
            compensationServiceImpl.read(employeeId);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("Invalid employeeId: " + employeeId, e.getMessage());
        }
    }

    @Test
    public void testCreateReadCompensation() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(23534.32));
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");


        // Create employee
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        testEmployee.setEmployeeId(createdEmployee.getEmployeeId());
        compensation.setEmployee(testEmployee);
        // Create compensation
        doReturn(createdEmployee).when(employeeRepository).findByEmployeeId(nullable(String.class));
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
        assertNotNull(createdCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(compensation, createdCompensation);

        doReturn(createdCompensation).when(compensationRepository).findCompensationByEmployeeEmployeeId(nullable(String.class));
        // Read compensation
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        Assert.assertEquals(readCompensation.getEmployee().getEmployeeId(), readCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        Assert.assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        Assert.assertEquals(expected.getSalary(), actual.getSalary());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getDepartment(), actual.getDepartment());
        Assert.assertEquals(expected.getPosition(), actual.getPosition());
    }
}
