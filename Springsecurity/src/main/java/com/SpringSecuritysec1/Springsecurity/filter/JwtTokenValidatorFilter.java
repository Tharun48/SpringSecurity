package com.SpringSecuritysec1.Springsecurity.filter;

import com.SpringSecuritysec1.Springsecurity.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(path.equals("/apiLogin")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader(ApplicationConstants.JWT_HEADER);
        try{
            Environment env = getEnvironment();
            if(env!=null)   {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_HEADER_DEFAULT);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                if(secretKey!=null){
                    Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(header).getPayload();
                    String username = String.valueOf(claims.get("username"));
                    String authorites = String.valueOf(claims.get("authorities"));
                    List<GrantedAuthority> authorityList = new ArrayList<>();
                    String[] str = authorites.split(",");
                    for(String sub : str ) {
                        authorityList.add(new SimpleGrantedAuthority(sub));
                    }
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username,"",authorityList);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (ExpiredJwtException e){
            throw new BadCredentialsException(e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/account");
    }
}
