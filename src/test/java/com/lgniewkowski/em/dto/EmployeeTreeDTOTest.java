package com.lgniewkowski.em.dto;

import com.lgniewkowski.em.models.Address;
import com.lgniewkowski.em.models.Employee;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

public class EmployeeTreeDTOTest {
    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
    }

    @Test
    public void mapFromEntity() {
        Employee supervisor = createEmployee(1L, "Hope", "Pym", "5th Avenue", "New York");
        Employee subordinate = createEmployee(2L, "Janet", "Van Dyne", "5th Avenue", "New York");
        subordinate.setSupervisor(supervisor);
        EmployeeTreeDTO dto = modelMapper.map(supervisor, EmployeeTreeDTO.class);
        assertEmployee(supervisor, dto);
        assertEquals(1, dto.getSubordinates().size());
        assertEmployee(subordinate, dto.getSubordinates().get(0));
    }

    private void assertEmployee(Employee employee, EmployeeTreeDTO dto) {
        assertEquals(employee.getId(), dto.getId());
        assertEquals(employee.getFirstName(), dto.getFirstName());
        assertEquals(employee.getLastName(), dto.getLastName());
        assertEquals(employee.getAddress(), dto.getAddress());
    }

    private Employee createEmployee(Long id, String firstName, String secondName, String street, String city) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(secondName);
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        employee.setAddress(address);
        return employee;
    }

}