package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    /**
     * Method retrieves the reporting structure
     *      Retrieves employee info, parses through the directReports and keeps parsing through all child nodes
     *      number of reports = number of child nodes
     * @param id
     * @return
     */
    @Override
    public ReportingStructure reporting(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        Set<Employee> totalNodes = new HashSet<>();
        if(!CollectionUtils.isEmpty(employee.getDirectReports())) {
            totalNodes.addAll(employee.getDirectReports());
        }
        for(Employee reportEmployee: totalNodes) {
            Employee employeeData = employeeRepository.findByEmployeeId(reportEmployee.getEmployeeId());
            if(!CollectionUtils.isEmpty(employeeData.getDirectReports())) totalNodes.addAll(employeeData.getDirectReports());
        }
        reportingStructure.setNumberOfReports(totalNodes.size());
        return reportingStructure;
    }
}
