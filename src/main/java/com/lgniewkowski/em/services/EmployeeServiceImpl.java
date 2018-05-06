package com.lgniewkowski.em.services;

import com.lgniewkowski.em.exceptions.CyclicInHierarchyException;
import com.lgniewkowski.em.models.Employee;
import com.lgniewkowski.em.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployeesTree() {
        return employeeRepository.findBySupervisorIsNull();
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.delete(id);
    }

    @Override
    public Employee findOne(Long id) {
        return employeeRepository.findOne(id);
    }

    public void updateEmployee(Employee employee) {
        if(employee.getId() != null) {
            updateExistingEmployee(employee);
        } else {
            addNewEmployee(employee);
        }
    }

    @Transactional
    private void updateExistingEmployee(Employee employee) {
        Employee fromDatabase = employeeRepository.findOne(employee.getId());
        if(!Objects.equals(fromDatabase.getSupervisor(), employee.getSupervisor()) && employee.getSupervisor() != null) {
            if(checkIfHasInSubordinates(fromDatabase, employee.getSupervisor())) {
                throw new CyclicInHierarchyException(employee.getId(), employee.getSupervisor().getId());
            }
        }
        fromDatabase.setFirstName(employee.getFirstName());
        fromDatabase.setLastName(employee.getLastName());
        fromDatabase.setAddress(employee.getAddress());
        fromDatabase.setSupervisor(employee.getSupervisor());
        employee.setSupervisor(null);
        employeeRepository.saveAndFlush(fromDatabase);
    }

    private boolean checkIfHasInSubordinates(Employee fromDatabase, Employee supervisor) {
        return fromDatabase.getSubordinates().contains(supervisor) || fromDatabase.getSubordinates().stream().anyMatch(sub -> checkIfHasInSubordinates(sub, supervisor));
    }

    private void addNewEmployee(Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }
}
