package com.SpringSecuritysec1.Springsecurity.configuration;

import com.SpringSecuritysec1.Springsecurity.filter.JwtTokenGeneratorFilter;
import com.SpringSecuritysec1.Springsecurity.filter.JwtTokenValidatorFilter;
import com.SpringSecuritysec1.Springsecurity.filter.LoggingInfoFilter;
import com.SpringSecuritysec1.Springsecurity.filter.RequestingValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfiguraration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new RequestingValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new LoggingInfoFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(),BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(),BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/account").hasAuthority("VIEW_ACCOUNT")
                        .requestMatchers("/balance" ).hasAuthority("VIEW_BALANCE")
                        .requestMatchers("/card").hasAuthority("VIEW_CARD")
                        .requestMatchers("/loans").hasAuthority("VIEW_LOANS")
                        .requestMatchers("/customer/{customerId}").authenticated()
                        .requestMatchers("/contact", "/error", "/notices").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/apiLogin").permitAll()
                );
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
    /*
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.builder().username("tharun").password("{noop}Tharun@1").roles("read").build();
        return new InMemoryUserDetailsManager(user);
    }
     */

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(EazyBankUserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        EasyBankAuthenticationProvider authenticationProvider =
                new EasyBankAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }



}
