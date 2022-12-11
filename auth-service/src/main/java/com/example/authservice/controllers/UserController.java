package com.example.authservice.controllers;

import com.example.authservice.dtos.UserPostDto;
import com.example.authservice.models.User;
import com.example.authservice.proto.UserProto;
import com.example.authservice.services.UserService;
import com.example.authservice.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtGenerator generator;

    @PostMapping(value = "/login")
    public ResponseEntity<String> logIn(@NotBlank @RequestParam String email, @NotBlank @RequestParam String password) {
        //testing jms logging-service
        if(email.equals("exception")) throw new RuntimeException();
        User user = userService.getByCredentials(email, password);
        String token = generator.generateToken(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<UserPostDto> signUp(@Valid @RequestBody UserPostDto dto) {
        userService.create(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{id}", produces = "application/x-protobuf")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public UserProto.User getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return UserProto.User.newBuilder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setId(user.getId())
                .setRole(UserProto.User.UserRole.forNumber(user.getRole().ordinal()))
                .build();
    }


}
