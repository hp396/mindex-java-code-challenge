package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.validator.CompensationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private CompensationValidator compensationValidator;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Create compensation, check for required fields, check employee exists. If all good then save the compensation
     * @param compensation
     * @return
     */
    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating employee compensation [{}]", compensation);
        //Validate all fields in compensation
        compensationValidator.validateRequiredFields(compensation);
        String employeeId = compensation.getEmployee().getEmployeeId();
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        //Check employee exists
        if(employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }
        compensation.setEmployee(employee);
        compensationRepository.insert(compensation);
        return compensation;
    }

    /**
     * Retrieves compensation from db
     * @param employeeId
     * @return
     */
    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Retrieving compensation for employee with id [{}]", employeeId);
        Compensation compensation = compensationRepository.findCompensationByEmployeeEmployeeId(employeeId);
        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }
        return compensation;
    }
}
