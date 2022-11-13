package com.example.authservice.controllers;

import com.example.authservice.models.User;
import com.example.authservice.services.UserService;
import com.example.authservice.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/api/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtGenerator generator;

    @PostMapping
    public ResponseEntity<String> logIn(@NotBlank @RequestParam String email, @NotBlank @RequestParam String password) {
        //testing jms logging-service
        if(email.equals("exception")) throw new RuntimeException();
        User user = userService.getByCredentials(email, password);
        String token = generator.generateToken(user);
        return ResponseEntity.ok(token);
    }

}
