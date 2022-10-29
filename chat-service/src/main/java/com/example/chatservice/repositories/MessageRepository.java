package com.example.chatservice.repositories;

import com.example.chatservice.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value =
            "SELECT *\n" +
            "FROM message\n" +
            "WHERE chat_id = :chatId", nativeQuery = true)
    Iterable<Message> getMessagesByChatId(Long chatId);
}
