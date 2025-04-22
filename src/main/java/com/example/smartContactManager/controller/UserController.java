package com.example.smartContactManager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.smartContactManager.dao.UserRepository;
import com.example.smartContactManager.dao.ContactRepository;
import com.example.smartContactManager.entities.Contact;
import com.example.smartContactManager.entities.User;
import com.example.smartContactManager.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String username = principal.getName();

        User user = userRepository.getUserByUserName(username);

        System.out.println(user);
        
        model.addAttribute("user", user);
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title", "Dashboard");
        return "normal/userDashboard";
    }

    @GetMapping("/addContact")
    public String addContact(Model model){
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/addContact";
    }

    @PostMapping("/process_contact")
    public String processContact(@ModelAttribute("contact") Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession httpSession){
        
        try {   
            String username = principal.getName(); //get current username;
            User user = userRepository.getUserByUserName(username); //find user by username;
            contact.setUser(user); //multidirectional mapping, so set user in contact;
            user.getContacts().add(contact); //add contact in contact list;


            //process profile image;
            if(file.isEmpty()){
                System.out.println("Image file is empty"); //log;
                contact.setImage("default.png");
            }
            else {
                contact.setImage(file.getOriginalFilename()); //set image name into contact;

                String uploadedImagePath = new ClassPathResource("/static/uploads/").getFile().getAbsolutePath(); //get path to store image;

                Files.copy(file.getInputStream(), Path.of(uploadedImagePath + "/" + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING); //copy input image into given path;

            }
            

            userRepository.save(user); //save user with contact in DB;
            System.out.println(contact); //log;

            //success message;
            httpSession.setAttribute("message", new Message("Contact Add Successfully", "alert-success"));

        } catch (Exception e) {
            //error message;
            httpSession.setAttribute("message", new Message("Error while adding Contact", "alert-danger"));

            e.printStackTrace();
            System.out.println("Exception occur when saving contact in DB");
        }


        return "normal/addContact";
    }

    //apply pagination using pathVariable;
    //we need per page 5 contact;
    //and current page;
    @GetMapping("/view_contact/{currentPage}")
    public String viewContact(@PathVariable("currentPage") Integer currentPage, Model model, Principal principal){
        model.addAttribute("title", "View-Contact");

        String currentUserName = principal.getName(); //find current username by principal;
        User currentUser = userRepository.getUserByUserName(currentUserName); //find current user by username;
        int currentUserId = currentUser.getUserId(); //find user id form current user;

        //Pageable hold two parameters | PageRequest.of take two values which holds by Pageable; 
        //1. pageNumber zero-based page number, must not be negative.
        //2. pageSize the size of the page to be returned, must be greater than 0.
        Pageable pageable = PageRequest.of(currentPage, 5);

        //page is the subclass of List, used to implement pagination;
        Page<Contact> contactList = contactRepository.findByUserUserId(currentUserId, pageable); //find list of contact of current user id;

        model.addAttribute("contactList", contactList); //set contact list into contact;

        //set attributes to use page navigation in view_contact.html;
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", contactList.getTotalPages()); //Page has a method which return total number of pages, we no need to divide it manually;
        
        return "normal/view_contact";
    }

    //display contact details;
    @GetMapping("/contact/{contactId}")
    public String displayContact(@PathVariable("contactId") int contactId, Model model, Principal principal){

        model.addAttribute("title", "Contact-Details");

        //fetch contact form db;
        Contact contact = contactRepository.findByContactId(contactId); //find contact by given contactId;

        //fix security bug: bug(access other contact);
        String currentUserName = principal.getName(); //find current username by principal;
        User currentUser = userRepository.getUserByUserName(currentUserName); //find current user by username;
        int currentUserId = currentUser.getUserId(); //find user id form current user;

        if(currentUserId == contact.getUser().getUserId())
            model.addAttribute("contact", contact); //add contact into template;

        return "normal/contact";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") int contactId, Model model, Principal principal, HttpSession httpSession){
        //delete specific contact;
        System.out.println("DELETE: " + contactId); //log;

        Contact contact = contactRepository.findByContactId(contactId);

        //fix security bug: bug(access delete contact);
        String currentUserName = principal.getName(); //find current username by principal;
        User currentUser = userRepository.getUserByUserName(currentUserName); //find current user by username;
        int currentUserId = currentUser.getUserId(); //find user id form current user;

        if(currentUserId == contact.getUser().getUserId()){
            //this method only unlink user form contact but not delete the contact;
            // contact.setUser(null); //unlink user with contact;
            // contactRepository.delete(contact);

            //here we remove the contact from user, and save update user;
            currentUser.getContacts().remove(contact);
            userRepository.save(currentUser);

            httpSession.setAttribute("message", new Message("Contact delete Successfully", "alert-success"));
            System.out.println("Contact delete successfully");
        }

        //profile image delete pending;

        return "redirect:/user/view_contact/0";
    }

    //display update contact form;
    @PostMapping("/update_contact/{contactId}")
    public String updateForm(Model model, @PathVariable("contactId") int contactId){
        model.addAttribute("title", "Update-Contact");

        Contact contact = contactRepository.findByContactId(contactId);

        model.addAttribute("contact", contact);
        model.addAttribute("profileImage", contact.getImage());

        return "normal/update_form";
    }

    //process update contact form;
    @PostMapping("/process_update_contact")
    public String processUpdateContact(@ModelAttribute("contact") Contact contact, @RequestParam("profileImage") MultipartFile profileImageFile, Principal principal, HttpSession httpSession){

        System.out.println("Process contact working"); //log;
        
        //old contact details;
        Contact oldContact = contactRepository.findByContactId(contact.getContactId());

        try {
            //image;
            if (!profileImageFile.isEmpty()) {
                System.out.println("Image accept"); //log;
                //re-write image file

                //1. delete old photo
                File deleteImagePath = new ClassPathResource("/static/uploads/").getFile(); //get path to store image;
                File file = new File(deleteImagePath, oldContact.getImage());
                file.delete();


                //2. add new photo
                contact.setImage(profileImageFile.getOriginalFilename()); //set image name into contact;
                String uploadedImagePath = new ClassPathResource("/static/uploads/").getFile().getAbsolutePath(); //get path to store image;
                Files.copy(profileImageFile.getInputStream(), Path.of(uploadedImagePath + "/" + profileImageFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING); //copy input image into given path; 
            }else {
                contact.setImage(oldContact.getImage());
            }

            //update contact;
            User user = userRepository.getUserByUserName(principal.getName()); //find user by principal username;
            contact.setUser(user); //link contact with user;
            contactRepository.save(contact);

            //display success message;
            httpSession.setAttribute("message", new Message("Contact Updated Successfully", "alert-success"));
            
        } catch (Exception e) {
            //display error message 
            httpSession.setAttribute("message", new Message("Error while Updating Contact", "alert-danger"));
            e.printStackTrace();
        }

        return "redirect:/user/contact/"+contact.getContactId();
    }

    //profile;
    @GetMapping("/profile")
    public String yourProfile(Model model, Principal principal){
        model.addAttribute("title", "Your-Profile");

        User user = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);

        return "normal/profile";
    }

    @GetMapping("/settings")
    public String viewSettings(Model model){
        model.addAttribute("title", "Settings");

        return "normal/settings";
    }

    @PostMapping("/process-settings")
    public String processContact(@ModelAttribute("oldPassword") String oldPassword, @ModelAttribute("newPassword") String newPassword, Principal principal, HttpSession httpSession){

        System.out.println(oldPassword); //log;
        System.out.println(newPassword); //log;

        User currentUser = userRepository.getUserByUserName(principal.getName());

        if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())){
            //update password;

            //set encoded password into currentUser
            currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            //then save currentUser into DB:
            userRepository.save(currentUser);

            httpSession.setAttribute("message", new Message("Password Changed Successfully", "alert-success"));

            return "redirect:/user/dashboard";
        }else {
            //error;
            System.out.println("You enter wrong Password"); //log;
            httpSession.setAttribute("message", new Message("You Enter Wrong Password", "alert-danger"));
        }

        return "normal/settings";
    }
}
