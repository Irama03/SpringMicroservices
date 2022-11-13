package com.example.chatservice.repositories;

import com.example.chatservice.models.Chat;
import com.example.chatservice.models.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.UnknownServiceException;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

}
