package com.example.smartContactManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.smartContactManager.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("select u from User u where u.email = :email")
    public User getUserByUserName(@Param("email") String email); //find User by email;
}
