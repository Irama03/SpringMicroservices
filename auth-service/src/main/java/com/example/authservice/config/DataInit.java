package com.example.authservice.config;

import com.example.authservice.models.User;
import com.example.authservice.models.UserRole;
import com.example.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        insertUsers();
    }
    private void insertUsers() {
        User u1 = new User("hlib", "example1@gmail.com", "12345", UserRole.ADMIN);
        User u2 = new User("vova", "example2@gmail.com", "11111", UserRole.CLIENT);
        userRepository.save(u1);
        userRepository.save(u2);
    }
}
