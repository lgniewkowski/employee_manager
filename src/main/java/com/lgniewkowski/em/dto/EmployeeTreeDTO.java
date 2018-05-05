package com.lgniewkowski.em.dto;

import com.lgniewkowski.em.models.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeTreeDTO {
    private long id;
    private String firstName;
    private String lastName;
    private Address address;
    private List<EmployeeTreeDTO> subordinates;
}
