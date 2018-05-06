package com.lgniewkowski.em.utils;

import com.lgniewkowski.em.models.Address;
import com.lgniewkowski.em.models.Employee;

public class EntityFactory {
    public static Employee cloneEmployee(Employee employee) {
        return createEmployee(employee.getId(), employee.getFirstName(), employee.getLastName(),
                employee.getAddress().getStreet(), employee.getAddress().getCity());
    }

    public static Employee createEmployee(String firstName, String lastName, String street, String city) {
        return createEmployee(null, firstName, lastName, street, city);
    }

    public static Employee createEmployee(Long id, String firstName, String lastName, String street, String city) {
        return createEmployee(id, firstName, lastName, street, city, null);
    }

    public static Employee createEmployee(Long id, String firstName, String lastName, String street, String city, Employee supervisor) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAddress(createAddress(street, city));
        employee.setSupervisor(supervisor);
        return employee;
    }

    private static Address createAddress(String street, String city) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        return address;
    }
}
