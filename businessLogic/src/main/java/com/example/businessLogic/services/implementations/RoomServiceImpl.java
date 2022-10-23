package com.example.businessLogic.services.implementations;

import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.ValueNotUniqueException;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.repositories.RoomRepository;
import com.example.businessLogic.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Iterable<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Iterable<Room> getRoomsOfCity(String city) {
        return roomRepository.findByCityIgnoreCase(processString(city));
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Room.class, "id", id));
    }

    // TODO: add processing of string values
    @Override
    public Room addRoom(Room room) {
        nameShouldBeUnique(room.getName());
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        nameShouldBeUnique(room.getName());
        return roomRepository
                .findById(id)
                .map(r -> roomRepository.save(new Room(id, room.getName(), room.getType(),
                        room.getDescription(), room.getLessor(), room.getAddress(), room.getCity(),
                        room.getPrice(), room.getCapacity())))
                .orElseThrow(() -> new RecordNotFoundException(Lessor.class, "id", id));
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.findById(id).ifPresentOrElse(r -> roomRepository.deleteById(id),
                        () -> {throw new RecordNotFoundException(Room.class, "id", id);});
    }

    private String processString(String str) {
        return str == null ? null : str.replaceAll("\\s+", " ").trim();
    }

    private void nameShouldBeUnique(String name) {
        if (!nameIsUnique(name)) {
            throw new ValueNotUniqueException("name", name);
        }
    }

    private boolean nameIsUnique(String name) {
        return !roomRepository.existsRoomByName(name);
    }


}
