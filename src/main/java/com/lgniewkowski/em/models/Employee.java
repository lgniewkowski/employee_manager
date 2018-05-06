package com.lgniewkowski.em.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"supervisor", "subordinates"})
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="first_name")
    private String firstName;

    @NotNull
    @Column(name="last_name")
    private String lastName;

    @NotNull
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supervisor_id")
    private Employee supervisor;

    @OneToMany(mappedBy="supervisor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Employee> subordinates = new HashSet<>();

    public void setSupervisor(Employee supervisor) {
        if(this.supervisor != null) {
            this.supervisor.getSubordinates().remove(this);
        }
        this.supervisor = supervisor;
        if(supervisor != null) {
            supervisor.getSubordinates().add(this);
        }
    }
}
