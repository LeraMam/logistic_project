package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/auth")
    public UserEntity getUserByAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        /*System.out.println("username from autentificate");
        System.out.println(username);*/
        Optional<UserEntity> loginUser = userService.getUserByLogin(username);
        UserEntity user = loginUser.get();
        /*System.out.println(user);*/
        return user;
    }

    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user){
        UserEntity savedUser = userService.addNewUser(user);
        return savedUser;
    }
}
