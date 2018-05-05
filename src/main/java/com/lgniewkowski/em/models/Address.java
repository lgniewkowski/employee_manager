package com.lgniewkowski.em.models;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class Address {

    @NotNull
    private String street;

    @NotNull
    private String city;
}
