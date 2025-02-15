package com.SpringSecuritysec1.Springsecurity.model;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="customer")
    private int id;

    @Column(name="email")
    private String email;

    @Column(name="pwd")
    private String password;

    @Column(name="role")
    private String role;

}
