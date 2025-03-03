package com.SpringSecuritysec1.Springsecurity.rest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure")
    public String secure(Authentication authentication){
        if(authentication instanceof UsernamePasswordAuthenticationToken ) {
            return "Hello " + authentication.getName();
        }
        else{
            return "Social login";
        }
    }

}
