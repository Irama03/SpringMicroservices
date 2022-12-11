package com.example.authservice.services;

import com.example.authservice.dtos.UserPostDto;
import com.example.authservice.exceptions.BadCredentialsException;
import com.example.authservice.exceptions.InvalidRequestBodyException;
import com.example.authservice.exceptions.RecordAlreadyExistsException;
import com.example.authservice.exceptions.RecordNotFoundException;
import com.example.authservice.models.User;
import com.example.authservice.models.UserRole;
import com.example.authservice.proto.BusinessLogicServiceGrpc;
import com.example.authservice.repositories.UserRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @GrpcClient("business-logic")
    private BusinessLogicServiceGrpc.BusinessLogicServiceBlockingStub serviceStub;

    @Override
    public User getByCredentials(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty() || !user.get().getPassword().equals(password))
            throw new BadCredentialsException();
        return user.get();
    }

    @Override
    public User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RecordNotFoundException(User.class, "id", id));
    }

    @Override
    @Transactional
    public User create(UserPostDto dto) {
        User user = userRepo.save(getUserFromDto(dto));
        if(!user.getRole().equals(UserRole.ADMIN)) {
            com.example.authservice.proto.ServiceReply reply = serviceStub.sendUser(com.example.authservice.proto.UserDto.newBuilder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .setPhone(dto.getPhone())
                    .setId(user.getId())
                    .setRole(com.example.authservice.proto.UserDto.UserRole.forNumber(user.getRole().ordinal() - 1))
                    .build());
            if(!reply.getSuccess())
                throw new RuntimeException("Service replied with 'false', transaction rollback..");
        }
        return user;
    }

    private User getUserFromDto(UserPostDto dto) {
        if(dto.getPhone() == null && !dto.getRole().equals(UserRole.ADMIN))
            throw new InvalidRequestBodyException("Phone number must be supplied during CLIENT or LESSOR registration");
        if(userRepo.findByEmail(dto.getEmail()).isPresent())
            throw new RecordAlreadyExistsException("email");
        return new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getRole());
    }

}
