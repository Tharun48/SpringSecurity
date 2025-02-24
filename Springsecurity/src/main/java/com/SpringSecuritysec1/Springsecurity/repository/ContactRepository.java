package com.SpringSecuritysec1.Springsecurity.repository;

import com.SpringSecuritysec1.Springsecurity.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

}
