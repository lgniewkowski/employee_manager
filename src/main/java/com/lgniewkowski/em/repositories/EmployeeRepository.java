package com.lgniewkowski.em.repositories;

import com.lgniewkowski.em.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    List<Employee> findBySupervisorIsNull();
}
