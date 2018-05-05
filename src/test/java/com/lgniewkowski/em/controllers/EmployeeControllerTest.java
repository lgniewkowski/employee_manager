package com.lgniewkowski.em.controllers;

import com.lgniewkowski.em.models.Address;
import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.services.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockBean
    private EmployeeService service;

    @Test
    public void getEmployeesTree() throws Exception {
        Employee supervisor = createEmployee("Frodo", "Baggins", createAddress("Bagshot Row", "Hobbiton"));
        Employee subordinate = createEmployee("Samwise", "Gamgee", createAddress("Bagshot Row", "Hobbiton"));
        subordinate.setSupervisor(supervisor);
        when(service.getEmployeesTree()).thenReturn(Collections.singletonList(supervisor));

        ResultActions resultActions = mvc.perform(get("/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        checkEmployeeJson(resultActions, "$[0]", supervisor);
        checkEmployeeJson(resultActions, "$[0].subordinates[0]", subordinate);
    }

    private void checkEmployeeJson(ResultActions resultActions, String pathPrefix, Employee employee) throws Exception {
        resultActions.andExpect(jsonPath(pathPrefix + ".firstName", is(employee.getFirstName())))
                .andExpect(jsonPath(pathPrefix + ".lastName", is(employee.getLastName())))
                .andExpect(jsonPath(pathPrefix + ".address.street", is(employee.getAddress().getStreet())))
                .andExpect(jsonPath(pathPrefix + ".address.city", is(employee.getAddress().getCity())))
                .andExpect(jsonPath(pathPrefix + ".subordinates", hasSize(employee.getSubordinates().size())));
    }

    private Employee createEmployee(String firstName, String lastName, Address address) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAddress(address);
        return employee;
    }

    private Address createAddress(String street, String city) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        return address;
    }

}