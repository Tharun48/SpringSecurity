package com.SpringSecuritysec1.Springsecurity.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @GetMapping("/loans")
    public String getLoansDetails() {
        return "Loans details of user";
    }

}
