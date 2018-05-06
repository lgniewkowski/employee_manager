package com.lgniewkowski.em.dto;

import com.lgniewkowski.em.models.Employee;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static com.lgniewkowski.em.utils.EntityFactory.createEmployee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmployeeDTOTest {

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
    }

    @Test
    public void mapFromEntity() {
        Employee employee = createEmployee(1L, "Robert", "Neville",
                "11 Washington Square Park North", "Manhattan");
        EmployeeDTO dto = modelMapper.map(employee, EmployeeDTO.class);
        assertEquals(employee.getId(), dto.getId());
        assertEquals(employee.getFirstName(), dto.getFirstName());
        assertEquals(employee.getLastName(), dto.getLastName());
        assertEquals(employee.getAddress(), dto.getAddress());
        assertNull(dto.getSupervisorId());
    }
}