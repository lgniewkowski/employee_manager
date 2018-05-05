package com.lgniewkowski.em.services;

import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployeesTree() {
        return employeeRepository.findBySupervisorIsNull();
    }
}
