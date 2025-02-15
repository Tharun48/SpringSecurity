package com.SpringSecuritysec1.Springsecurity.configuration;

import com.SpringSecuritysec1.Springsecurity.model.Customer;
import com.SpringSecuritysec1.Springsecurity.repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EazyBankUserDetailsService implements UserDetailsService {

    CustomerRespository customerRespository;

    @Autowired
    EazyBankUserDetailsService(CustomerRespository customerRespository) {
        this.customerRespository = customerRespository;
    }


    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRespository.findByEmail(username);
        if(customer==null) {
            throw new UsernameNotFoundException("User not found for the username: " + username);
        }
        List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(),customer.getPassword(),grantedAuthorityList);
    }
}
