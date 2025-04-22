package com.example.smartContactManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int contactId;

    private String name;
    private String nicName;
    private String email;
    private String image;

    @Column(length = 500)
    private String description;
    private String phone;
    private String work;

    @ManyToOne //append user(foreign key) column in contact table; eg: | contact1 | user1 |, means contact1 is a contact of user1;
    @JsonIgnore //use this annotation to ignore loop in SearchController;
    private User user;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        //if contactIds are equal = contact are equal;
        return this.contactId == ((Contact) obj).getContactId();
    }

    // @Override
    // public String toString() {
    //     return "Contact [contactId=" + contactId + ", name=" + name + ", nicName=" + nicName + ", email=" + email
    //             + ", image=" + image + ", description=" + description + ", phone=" + phone + ", work=" + work
    //             + ", user=" + user + "]";
    // }

}
