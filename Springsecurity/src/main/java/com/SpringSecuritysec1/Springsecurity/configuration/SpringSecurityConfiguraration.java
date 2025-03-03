package com.SpringSecuritysec1.Springsecurity.configuration;

import com.SpringSecuritysec1.Springsecurity.filter.LoggingInfoFilter;
import com.SpringSecuritysec1.Springsecurity.filter.RequestingValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
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
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/account").hasAuthority("VIEW_ACCOUNT")
                        .requestMatchers("/balance" ).hasAuthority("VIEW_BALANCE")
                        .requestMatchers("/card").hasAuthority("VIEW_CARD")
                        .requestMatchers("/loans").hasAuthority("VIEW_LOANS")
                        .requestMatchers("/secure").authenticated()
                        .requestMatchers("/customer/{customerId}").authenticated()
                        .requestMatchers("/contact", "/error", "/notices").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/apiLogin").permitAll()
                );
        http.oauth2Login(withDefaults());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration github = clientRegistrationRepositoryGithub();
        ClientRegistration google = clientRegistrationRepositoryGoogle();
        return new InMemoryClientRegistrationRepository(github,google);
    }

    private ClientRegistration clientRegistrationRepositoryGithub() {
        ClientRegistration github = CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("Ov23liZvBwVQ1ZLGbOgc").clientSecret("df850838caaeeba422f47746180fb78afce4d7ee").build();
        return github;
    }

    private ClientRegistration clientRegistrationRepositoryGoogle() {
        ClientRegistration google = CommonOAuth2Provider.GOOGLE.getBuilder("google").clientId("446605117599-lp6t46rjhmn5lhvsdtd7kdstnjj8gqmi.apps.googleusercontent.com").clientSecret("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").build();
        return google;
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
