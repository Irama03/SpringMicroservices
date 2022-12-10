package com.example.authservice.services;

import com.example.authservice.models.User;

public interface UserService {

    User getByCredentials(String email, String password);
    User getById(Long id);

}
