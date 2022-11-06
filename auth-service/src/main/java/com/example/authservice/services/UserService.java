package com.example.authservice.services;

import com.example.authservice.models.User;

public interface UserService {

    public User getByCredentials(String email, String password);

}
