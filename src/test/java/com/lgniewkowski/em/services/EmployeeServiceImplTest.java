package com.lgniewkowski.em.services;

import com.lgniewkowski.em.exceptions.CyclicInHierarchyException;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.lgniewkowski.em.utils.EntityFactory.cloneEmployee;
import static com.lgniewkowski.em.utils.EntityFactory.createEmployee;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

    private Employee supervisor;
    private Employee subordinate;
    private Employee subsubordinate;

    @Before
    public void init() throws Exception {
        supervisor = createEmployee(1L, "", "", "", "");
        subordinate = createEmployee(2L, "", "", "", "", supervisor);
        subsubordinate = createEmployee(3L, "", "", "", "", subordinate);
    }

    @Test
    public void getEmployeesTree() throws Exception {
        List<Employee> employeesTree = Collections.singletonList(supervisor);
        when(employeeRepository.findBySupervisorIsNull()).thenReturn(employeesTree);
        assertEquals(employeesTree, employeeService.getEmployeesTree());
    }

    @Test
    public void findAll() throws Exception {
        List<Employee> allEmployees = Arrays.asList(supervisor, subordinate, subsubordinate);
        when(employeeRepository.findAll()).thenReturn(allEmployees);
        assertEquals(allEmployees, employeeService.findAll());
    }

    @Test
    public void delete() throws Exception {
        employeeService.deleteEmployee(2L);
        verify(employeeRepository, times(1)).delete(2L);
    }

    @Test
    public void findOne() throws Exception {
        when(employeeRepository.findOne(supervisor.getId())).thenReturn(supervisor);
        Employee currentEmployee = employeeService.findOne(supervisor.getId());
        assertEquals(supervisor, currentEmployee);
    }

    @Test
    public void createNew() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("testName");
        employeeService.updateEmployee(newEmployee);
        verify(employeeRepository, times(1)).saveAndFlush(newEmployee);
    }

    @Test
    public void update() throws Exception {
        supervisor.setFirstName("newName");
        when(employeeRepository.findOne(supervisor.getId())).thenReturn(supervisor);
        employeeService.updateEmployee(supervisor);
        verify(employeeRepository, times(1)).saveAndFlush(supervisor);
    }

    @Test(expected = CyclicInHierarchyException.class)
    public void testCyclic() throws Exception {
        when(employeeRepository.findOne(supervisor.getId())).thenReturn(supervisor);
        Employee supervisorClone = cloneEmployee(supervisor);
        supervisorClone.setSupervisor(subsubordinate);
        employeeService.updateEmployee(supervisorClone);
    }
}