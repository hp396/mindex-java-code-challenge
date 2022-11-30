package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.util.Date;

public class Compensation {
    private Employee employee;
    private Date effectiveDate;
    private BigDecimal salary;

    public Compensation() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
