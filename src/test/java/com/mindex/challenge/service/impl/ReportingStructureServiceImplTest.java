package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String reportingStructureIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureIdUrl = "http://localhost:" + port + "/reporting-structure/{id}";
    }

    @Test
    public void testRead() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setEmployeeId("1");
        testEmployee1.setFirstName("Tom");
        testEmployee1.setLastName("Hanks");
        testEmployee1.setDepartment("HR");
        testEmployee1.setPosition("Actor");

        Employee testEmployee2 = new Employee();
        testEmployee2.setEmployeeId("2");
        testEmployee2.setFirstName("Bob");
        testEmployee2.setLastName("Bruce");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer II");

        Employee testEmployee3 = new Employee();
        testEmployee3.setEmployeeId("3");
        testEmployee3.setFirstName("John");
        testEmployee3.setLastName("Doe");
        testEmployee3.setDepartment("Engineering");
        testEmployee3.setPosition("Developer");
        testEmployee3.setDirectReports(Arrays.asList(testEmployee1, testEmployee2));

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();

        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee3.getEmployeeId()).getBody();
        assertNotNull(readReportingStructure);
        assertEquals(2, readReportingStructure.getNumberOfReports());
    }

    @Test
    public void testReadNoReports() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setEmployeeId("1");
        testEmployee1.setFirstName("John");
        testEmployee1.setLastName("Doe");
        testEmployee1.setDepartment("Engineering");
        testEmployee1.setPosition("Developer");
        testEmployee1.setDirectReports(null);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        assertNotNull(readReportingStructure);
        assertEquals(0, readReportingStructure.getNumberOfReports());
    }
}
