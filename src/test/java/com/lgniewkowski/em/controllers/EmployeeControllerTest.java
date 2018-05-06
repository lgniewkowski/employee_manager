package com.lgniewkowski.em.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lgniewkowski.em.dto.EmployeeDTO;
import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.services.EmployeeService;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static com.lgniewkowski.em.utils.EntityFactory.createEmployee;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @TestConfiguration
    static class EmployeeControllerTestContextConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private EmployeeService service;

    private Employee supervisor;
    private Employee subordinate;

    @Before
    public void createEmployees() {
        supervisor = createEmployee(1L, "Frodo", "Baggins", "Bagshot Row", "Hobbiton");
        subordinate = createEmployee(2L, "Samwise", "Gamgee", "Bagshot Row", "Hobbiton");
        subordinate.setSupervisor(supervisor);
    }

    @Test
    public void getEmployeesTree() throws Exception {
        when(service.getEmployeesTree()).thenReturn(Collections.singletonList(supervisor));

        ResultActions resultActions = mvc.perform(get("/api/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        checkTreeEmployeeJson(resultActions, "$[0]", supervisor);
        checkTreeEmployeeJson(resultActions, "$[0].subordinates[0]", subordinate);
    }

    @Test
    public void findAll() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(supervisor, subordinate));
        ResultActions resultActions = mvc.perform(get("/api/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        checkEmployeeJson(resultActions, "$[0]", supervisor);
        checkEmployeeJson(resultActions, "$[1]", subordinate);
    }

    @Test
    public void deleteEmployee() throws Exception {
        mvc.perform(delete("/api/employee/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Player deleted"));
        verify(service, times(1)).deleteEmployee(2L);
    }

    @Test
    public void updateEmployee() throws Exception {
        EmployeeDTO employeeDTO = modelMapper.map(subordinate, EmployeeDTO.class);
        employeeDTO.setSupervisorId(supervisor.getId());
        when(service.findOne(supervisor.getId())).thenReturn(supervisor);

        mvc.perform(post("/api/employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(serializeToJSON(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Player updated"));
        verify(service, times(1)).findOne(supervisor.getId());
        verify(service, times(1)).updateEmployee(argThat(createEmployeeMatcher(subordinate)));
    }

    private Matcher<Employee> createEmployeeMatcher(Employee subordinate) {
        return new ArgumentMatcher<Employee>() {
            @Override
            public boolean matches(Object o) {
                Employee argument = (Employee) o;
                return argument != null && argument.equals(subordinate) &&
                        argument.getSubordinates().equals(subordinate.getSubordinates()) &&
                        argument.getSupervisor().equals(subordinate.getSupervisor());
            }
        };
    }

    private void checkEmployeeJson(ResultActions resultActions, String pathPrefix, Employee employee) throws Exception {
        checkCommonEmployeeJson(resultActions, pathPrefix, employee);
        Employee employeeSupervisor = employee.getSupervisor();
        resultActions.andExpect(jsonPath(pathPrefix + ".supervisorId", is(createEmployeeSupervisorIdMatcher(employeeSupervisor))));
    }

    private Matcher<Object> createEmployeeSupervisorIdMatcher(Employee employeeSupervisor) {
        return new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object o) {
                if(employeeSupervisor == null) {
                    return o == null;
                }
                Long value = o instanceof Long ? (Long)o : o instanceof Integer ? Long.valueOf((Integer) o) : null;
                return Objects.equals(value, employeeSupervisor.getId());
            }
        };
    }

    private void checkTreeEmployeeJson(ResultActions resultActions, String pathPrefix, Employee employee) throws Exception {
        checkCommonEmployeeJson(resultActions, pathPrefix, employee);
        resultActions.andExpect(jsonPath(pathPrefix + ".subordinates", hasSize(employee.getSubordinates().size())));
    }

    private void checkCommonEmployeeJson(ResultActions resultActions, String pathPrefix, Employee employee) throws Exception {
        resultActions.andExpect(jsonPath(pathPrefix + ".firstName", is(employee.getFirstName())))
                .andExpect(jsonPath(pathPrefix + ".lastName", is(employee.getLastName())))
                .andExpect(jsonPath(pathPrefix + ".address.street", is(employee.getAddress().getStreet())))
                .andExpect(jsonPath(pathPrefix + ".address.city", is(employee.getAddress().getCity())));
    }

    private String serializeToJSON(EmployeeDTO employeeDTO) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(employeeDTO);
    }
}