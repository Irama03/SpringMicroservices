package com.example.chatservice.repositories;

import com.example.chatservice.models.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
}
