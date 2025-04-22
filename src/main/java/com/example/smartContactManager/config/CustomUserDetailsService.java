package com.example.smartContactManager.config;

import com.example.smartContactManager.dao.UserRepository;
import com.example.smartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // user UserRepository to access User from DB;

    @Override //this method is automatically call by the Spring Security (DaoAuthenticationProvider), and this provider use UserDetail to get correct username;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username); // with the help of this UserName UserRepository fetch the User form DB;
        if (user == null) {
            System.out.println("exception: user not found in db");
            throw new UsernameNotFoundException("User not found!");
        }

        System.out.println("aasif log: " + user); //log;

        return new CustomUserDetails(user); // set User in UserDetails;
    }
}
