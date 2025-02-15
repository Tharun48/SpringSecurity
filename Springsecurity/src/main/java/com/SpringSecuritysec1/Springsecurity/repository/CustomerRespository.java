package com.SpringSecuritysec1.Springsecurity.repository;

import com.SpringSecuritysec1.Springsecurity.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRespository extends JpaRepository<Customer,Integer> {
    Customer findByEmail(String email);
}
