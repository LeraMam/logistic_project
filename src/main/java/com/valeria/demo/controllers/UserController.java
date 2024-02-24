package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user, HttpServletResponse response){
        System.out.println(user);
        UserEntity result = userService.addNewUser(user);
        Cookie cookie = new Cookie("userId", result.getId().toString());
        cookie.setPath("/");
        response.addCookie(cookie);
        return result;
    }

}
