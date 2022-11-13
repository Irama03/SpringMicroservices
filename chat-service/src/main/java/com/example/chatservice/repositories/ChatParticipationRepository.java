package com.example.chatservice.repositories;

import com.example.chatservice.models.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {

    @Query(value =
            "SELECT chat_id\n" +
            "FROM chat_participation\n" +
            "WHERE user_id = :userId", nativeQuery = true)
    Set<Long> findChatParticipationsByUserId(Long userId);
}
