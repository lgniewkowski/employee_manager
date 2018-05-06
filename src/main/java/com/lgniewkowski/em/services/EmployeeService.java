package com.lgniewkowski.em.services;

import com.lgniewkowski.em.models.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployeesTree();

    List<Employee> findAll();

    void deleteEmployee(long id);

    void updateEmployee(Employee employee);

    Employee findOne(Long supervisorId);
}
