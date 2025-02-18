package com.SpringSecuritysec1.Springsecurity.repository;

import com.SpringSecuritysec1.Springsecurity.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
    List<Authorities> findByCustomerId(int customerId);
}
