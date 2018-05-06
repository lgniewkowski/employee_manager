package com.lgniewkowski.em.controllers;

import com.lgniewkowski.em.dto.EmployeeDTO;
import com.lgniewkowski.em.dto.EmployeeTreeDTO;
import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
public class EmployeeController {
    private static final String EMPLOYEE_DELETE_RESPONSE = "Player deleted";
    private static final String EMPLOYEE_UPDATE_RESPONSE = "Player updated";

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method= RequestMethod.GET, value="/all")
    public List<EmployeeDTO> getAll() {
        return employeeService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @RequestMapping(method= RequestMethod.GET, value="/tree")
    public List<EmployeeTreeDTO> getEmployeesTree() {
        return employeeService.getEmployeesTree().stream().map(this::convertToTreeDTO).collect(Collectors.toList());
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/employee/{id}")
    public String deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return EMPLOYEE_DELETE_RESPONSE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/employee")
    public String updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(convertFromDTO(employeeDTO));
        return EMPLOYEE_UPDATE_RESPONSE;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = modelMapper.map(employee, EmployeeDTO.class);
        Employee supervisor = employee.getSupervisor();
        dto.setSupervisorId(supervisor != null ? supervisor.getId() : null);
        return dto;
    }

    private Employee convertFromDTO(EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setSupervisor(employeeDTO.getSupervisorId() != null ? employeeService.findOne(employeeDTO.getSupervisorId()) : null);
        return employee;
    }

    private EmployeeTreeDTO convertToTreeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeTreeDTO.class);
    }
}
