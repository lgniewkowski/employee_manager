package com.lgniewkowski.em.controllers;

import com.lgniewkowski.em.dto.EmployeeTreeDTO;
import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method= RequestMethod.GET, value="/tree")
    public List<EmployeeTreeDTO> getEmployeesTree() {
        return employeeService.getEmployeesTree().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private EmployeeTreeDTO convertToDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeTreeDTO.class);
    }
}
