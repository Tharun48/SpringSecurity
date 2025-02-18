package com.SpringSecuritysec1.Springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="email")
    private String email;

    @Column(name="pwd")
    private String password;

    @Column(name="role")
    private String role;

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER)
    public Set<Authorities> authoritiesSet;


}
