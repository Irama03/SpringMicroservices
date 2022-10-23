package com.example.businessLogic.repositories;

import com.example.businessLogic.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsRoomByName(String name);
    Iterable<Room> findByCityIgnoreCase(String city);
}
