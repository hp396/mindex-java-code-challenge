package com.mindex.challenge.validator;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CompensationValidatorTest.class)
public class CompensationValidatorTest {

    @InjectMocks
    private CompensationValidator compensationValidator;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    /**
     * validateRequiredFields:      I   All fields exist -> no error
     *                              II  salary does not exist -> error
     *                              III date does not exist -> error
     *                              IV  employee does not exist -> error
     *                              V   employee id does not exist -> error
     */

    @Test
    public void testValidateRequiredFields_I() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        compensation.setEmployee(employee);
        try {
            compensationValidator.validateRequiredFields(compensation);
        } catch (Exception e) {
            fail("Exception should not be thrown "+ e);
        }
    }

    @Test
    public void testValidateRequiredFields_II() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        compensation.setEmployee(employee);
        try {
            compensationValidator.validateRequiredFields(compensation);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("No Salary provided", e.getMessage());
        }
    }

    @Test
    public void testValidateRequiredFields_III() {
        Compensation compensation = new Compensation();
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        compensation.setEmployee(employee);
        try {
            compensationValidator.validateRequiredFields(compensation);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("No Effective Date provided", e.getMessage());
        }
    }

    @Test
    public void testValidateRequiredFields_IV() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        try {
            compensationValidator.validateRequiredFields(compensation);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("No Employee Information provided", e.getMessage());
        }
    }

    @Test
    public void testValidateRequiredFields_V() {
        Compensation compensation = new Compensation();
        compensation.setEffectiveDate(new Date());
        compensation.setSalary(BigDecimal.valueOf(234424.43));
        Employee employee = new Employee();
        employee.setFirstName("Firs-Name");
        compensation.setEmployee(employee);
        try {
            compensationValidator.validateRequiredFields(compensation);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("No Employee Id provided", e.getMessage());
        }
    }

}
