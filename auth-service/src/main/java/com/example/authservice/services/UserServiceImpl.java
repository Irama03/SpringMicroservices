package com.example.authservice.services;

import com.example.authservice.exceptions.BadCredentialsException;
import com.example.authservice.models.User;
import com.example.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User getByCredentials(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty() || !user.get().getPassword().equals(password))
            throw new BadCredentialsException();
        return user.get();
    }
}
