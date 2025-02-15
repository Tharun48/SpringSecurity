package com.SpringSecuritysec1.Springsecurity.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping("/contact")
    public String getContactDetails() {
        return "Contact details of user";
    }

}
