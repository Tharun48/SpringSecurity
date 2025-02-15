package com.SpringSecuritysec1.Springsecurity.rest;

import com.SpringSecuritysec1.Springsecurity.model.Customer;
import com.SpringSecuritysec1.Springsecurity.repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final CustomerRespository customerRespository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(CustomerRespository customerRespository, PasswordEncoder passwordEncoder) {
        this.customerRespository = customerRespository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try{
            String hashedPassword = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(hashedPassword);
            Customer savedCustomer = customerRespository.save(customer);
            if(savedCustomer.getId()>0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed");
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an exception occurred due to " + e.getMessage());
        }
    }
}
