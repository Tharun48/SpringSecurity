package com.SpringSecuritysec1.Springsecurity.rest;

import com.SpringSecuritysec1.Springsecurity.constants.ApplicationConstants;
import com.SpringSecuritysec1.Springsecurity.model.Customer;
import com.SpringSecuritysec1.Springsecurity.model.LoginRequestDTO;
import com.SpringSecuritysec1.Springsecurity.model.LoginResponseDTO;
import com.SpringSecuritysec1.Springsecurity.repository.CustomerRespository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final CustomerRespository customerRespository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;

    @Autowired
    public UserController(CustomerRespository customerRespository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, Environment env) {
        this.customerRespository = customerRespository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.env = env;
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

    //generating jwt token based on the api body
    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(),
                loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_HEADER_DEFAULT);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("Eazy Bank").subject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 900000000))
                        .signWith(secretKey).compact();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }


    //Method level security
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable int customerId){
        return customerRespository.findById(customerId);
    }

    /*
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable int customerId){
        return customerRespository.findById(customerId);
    }
     */


}
