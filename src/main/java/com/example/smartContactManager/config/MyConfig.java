package com.example.smartContactManager.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean //Bean is used to create Object, we use it by @Autowired annotation;
    public UserDetailsService userDetailsService() { // UserDetailsService used to set user credentials into Spring Security;
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ // used to encode and decode user password;
        return new BCryptPasswordEncoder();
    }

    @Bean //Configures authentication provider to validate user credentials;
    public DaoAuthenticationProvider authenticationProvider() { //use above Beans hear;
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService()); // set UserDetailsService;
        provider.setPasswordEncoder(this.passwordEncoder()); // set BCryptPasswordEncoder;
        return provider;
    }

    @Bean // set DaoAuthenticationProvider in Spring Security;
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return new ProviderManager(List.of(authenticationProvider())); 
    }

    @Bean // Defines security rules and configurations for handling requests;
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //view access configuration;
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth //config for authentication;
                        .requestMatchers("/", "/about", "/home", "/signup", "/process-signup-otp", "/login", "/forgot-password", "/process-forgot-password", "/process-otp", "/process-reset-password", "/css/**", "/js/**", "/img/**", "/do_register")
                        .permitAll() //all requestMatchers permit;
                        .requestMatchers("/admin/**").hasRole("ADMIN_ROLE") // Only ADMIN can access /admin/**
                        .requestMatchers("/normal/**").hasRole("USER_ROLE") // Only USER can access /user/**
                        .anyRequest().authenticated()) //remains secure;
                .formLogin(form -> form //config for loginForm;
                        .loginPage("/login") //login form path;
                        .loginProcessingUrl("/login") //login form action;
                        .defaultSuccessUrl("/user/dashboard", true) //if login success then goto here;
                        .permitAll()) //Allows all users to access the login page;
                .logout(logout -> logout //config for logout;
                        .logoutUrl("/logout") //URL for logout action;
                        .logoutSuccessUrl("/login?logout") //Redirect to login page after logout;
                        .permitAll()); //Allows all users to access logout URL

        return http.build();
    }
}
