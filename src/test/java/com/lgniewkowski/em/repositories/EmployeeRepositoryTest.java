package com.lgniewkowski.em.repositories;

import com.lgniewkowski.em.models.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.lgniewkowski.em.utils.EntityFactory.createEmployee;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee currentSupervisor;
    private Employee subordinate;
    private Employee previousSupervisor;

    @Before
    public void initData() {
        currentSupervisor = createEmployee("Sheev", "Palpatine", "unknown", "Theed");
        subordinate = createEmployee("Anakin", "Skywalker", "unknown", "Mos Espa");
        previousSupervisor = createEmployee("Obi-Wan", "Kenobi", "unknown", "Stewjon");
        subordinate.setSupervisor(previousSupervisor);
        subordinate.setSupervisor(currentSupervisor);
        testEntityManager.persist(currentSupervisor);
        testEntityManager.persist(subordinate);
        testEntityManager.persist(previousSupervisor);
        testEntityManager.flush();
    }

    @Test
    public void findBySupervisorIsNull() throws Exception {
        List<Employee> employees = employeeRepository.findBySupervisorIsNull();
        assertEquals(2, employees.size());
        assertSupervisor(employees.get(0), currentSupervisor, Collections.singleton(subordinate));
        assertSupervisor(employees.get(1), previousSupervisor, Collections.emptySet());
    }

    private void assertSupervisor(Employee givenSupervisor, Employee expectedSupervisor, Set<Employee> expectedSubordinates) {
        assertEquals(expectedSupervisor, givenSupervisor);
        assertEquals(expectedSubordinates, givenSupervisor.getSubordinates());
        givenSupervisor.getSubordinates().forEach(so -> assertEquals(expectedSupervisor, so.getSupervisor()));
    }
}