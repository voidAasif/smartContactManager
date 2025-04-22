package com.example.smartContactManager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.smartContactManager.dao.ContactRepository;
import com.example.smartContactManager.dao.UserRepository;
import com.example.smartContactManager.entities.Contact;
import com.example.smartContactManager.entities.User;

@RestController
public class SearchController { //used for search input in view contact;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{keyword}") //call from JS;
    public ResponseEntity<?> searchContact(@PathVariable("keyword") String keyword, Principal principal){ //return ResponseEntity of all type;

        User user = userRepository.getUserByUserName(principal.getName()); //fetch current user;

        List<Contact> matchContact = contactRepository.findByNameContainingAndUser(keyword, user); //call contactRepo;
        return ResponseEntity.ok(matchContact); //return ok(200) response with data which contains matched keywords;
    }
}
