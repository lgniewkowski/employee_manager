package com.lgniewkowski.em.services;

import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl();
        }
    }

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    private List<Employee> employeesTree;

    @Before
    public void init() throws Exception {
        Employee superVisor = new Employee();
        superVisor.setFirstName("firstName");
        employeesTree = Collections.singletonList(superVisor);
        when(employeeRepository.findBySupervisorIsNull()).thenReturn(employeesTree);
    }

    @Test
    public void getEmployeesTree() throws Exception {
        assertEquals(employeesTree, employeeService.getEmployeesTree());
    }

}