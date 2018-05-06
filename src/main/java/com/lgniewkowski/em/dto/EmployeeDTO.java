package com.lgniewkowski.em.dto;

import com.lgniewkowski.em.models.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Address address;
    private Long supervisorId;
}
