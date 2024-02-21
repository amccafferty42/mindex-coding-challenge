package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // Employee e = calculateReports(employee);
        // int numberOfReports = calculateNumberOfReports(employee);

        //ReportingStructure reportingStructure = new ReportingStructure(e, numberOfReports);

        ReportingStructure reportingStructure = calculateReports(employee);
        if (reportingStructure == null) reportingStructure = new ReportingStructure(employee, 0);

        return reportingStructure;
    }

    // private int calculateNumberOfReports(Employee reportee) { //post order traversal
    //     int numNodes = 0;
    //     if (reportee == null || reportee.getDirectReports() == null) return 0;
    //     for (Employee reporter : reportee.getDirectReports()) {
    //         numNodes += 1 + calculateNumberOfReports(reporter);
    //     }
    //     return numNodes;
    // }

    // private Employee calculateReports(Employee reportee) { //post order traversal
    //     ArrayList<Employee> directReporterList = new ArrayList();
    //     if (reportee == null || reportee.getDirectReports() == null) return null;
    //     for (Employee reporter : reportee.getDirectReports()) {
    //         reporter = employeeRepository.findByEmployeeId(reporter.getEmployeeId());
    //         directReporterList.add(reporter);
    //         calculateReports(reporter);
    //     }
    //     reportee.setDirectReports(directReporterList);
    //     return reportee;
    // }

    private ReportingStructure calculateReports(Employee reportee) {
        ArrayList<Employee> directReporterList = new ArrayList();
        int numberOfReports = 0;

        if (reportee == null || reportee.getDirectReports() == null) return null;

        for (Employee reporter : reportee.getDirectReports()) {
            reporter = employeeRepository.findByEmployeeId(reporter.getEmployeeId());
            directReporterList.add(reporter);
            ReportingStructure reportingStructure = calculateReports(reporter);
            numberOfReports += (reportingStructure != null) ? reportingStructure.getNumberOfReports() + 1: 1;
        }
        
        reportee.setDirectReports(directReporterList);

        return new ReportingStructure(reportee, numberOfReports);
    }

}
