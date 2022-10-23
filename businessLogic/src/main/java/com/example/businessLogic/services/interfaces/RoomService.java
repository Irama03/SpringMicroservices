package com.example.businessLogic.services.interfaces;

import com.example.businessLogic.models.Room;

public interface RoomService {

    Iterable<Room> getAll();
    // TODO: when Booking is implemented,
    //  add there checking availability of room from start_date to end_date
    Iterable<Room> getRoomsOfCity(String city);
    Room getRoomById(Long id);

    Room addRoom(Room room);
    Room updateRoom(Long id, Room room);
    void deleteRoom(Long id);

}
