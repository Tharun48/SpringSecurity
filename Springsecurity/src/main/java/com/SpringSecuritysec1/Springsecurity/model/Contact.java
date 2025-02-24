package com.SpringSecuritysec1.Springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
@Entity(name="contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="contact_name")
    private String ContactName;

    @Column(name="subject")
    private String subject;

    @Column(name = "message")
    private String message;

}
