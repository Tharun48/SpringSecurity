package com.SpringSecuritysec1.Springsecurity.filter;

import com.SpringSecuritysec1.Springsecurity.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(path.equals("/apiLogin")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader(ApplicationConstants.JWT_HEADER);
        header = header.trim();
        if(header.startsWith("Basic")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null){
                Environment env = getEnvironment();
                if(env!=null)   {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_HEADER_DEFAULT);
                    if(secret.length()<32) {
                        throw new IllegalStateException("secret key is null or less secure to generate token");
                    }
                    else{
                        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                        StringBuffer authorities=new StringBuffer();
                        authentication.getAuthorities().forEach(a->authorities.append(a.getAuthority()).append(","));
                        String jwt = Jwts.builder().issuer("Easy Bank").subject("Jwt Tokens")
                                .claim("username", authentication.getName())
                                .claim("authorities",authorities.toString())
                                .issuedAt(new Date())
                                .expiration(new Date(new Date().getTime() + 600000))
                                .signWith(secretKey).compact();
                        response.setHeader("Authorization",jwt);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/account");
    }
}
