package com.mindex.challenge.validator;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CompensationValidator {
    /**
     * Validating that compensation object has salary, effective date, and employeeId
     * @param compensation
     */
    public void validateRequiredFields(Compensation compensation) {
        if(compensation.getSalary()==null) throw new RuntimeException("No Salary provided");
        if(compensation.getEffectiveDate()==null) throw new RuntimeException("No Effective Date provided");
        if(compensation.getEmployee()==null || compensation.getEmployee().equals(new Employee())) throw new RuntimeException("No Employee Information provided");
        if(StringUtils.isEmpty(StringUtils.trimAllWhitespace(compensation.getEmployee().getEmployeeId()))) throw new RuntimeException("No Employee Id provided");
    }
}
