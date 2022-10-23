package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.mappers.RoomMapper;
import com.example.businessLogic.dtos.rooms.RoomGetDto;
import com.example.businessLogic.dtos.rooms.RoomPostDto;
import com.example.businessLogic.dtos.rooms.RoomPutDto;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.services.interfaces.RoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/rooms")
@Validated
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping()
    public Iterable<RoomGetDto> getRooms(){
        return roomMapper.roomsToRoomsGetDto(roomService.getAll());
    }

    @GetMapping("/of_city/{city}")
    public Iterable<RoomGetDto> getRoomsOfCity(@PathVariable("city") String city){
        return roomMapper.roomsToRoomsGetDto(roomService.getRoomsOfCity(city));
    }

    @GetMapping("/{id}")
    public RoomGetDto getRoom(@PathVariable("id") Long id) {
        return roomMapper.roomToRoomGetDto(roomService.getRoomById(id));
    }

    @PostMapping
    public RoomGetDto addRoom(@Valid @RequestBody RoomPostDto roomPostDto){
        return roomMapper.roomToRoomGetDto(roomService.addRoom(roomMapper.roomPostDtoToRoom(roomPostDto)));
    }

    @PutMapping("/{id}")
    public RoomGetDto updateRoom(@PathVariable("id") Long id, @Valid @RequestBody RoomPutDto roomPutDto) {
        Room room = roomMapper.roomPutDtoToRoom(roomPutDto);
        room.setId(id);
        return roomMapper.roomToRoomGetDto(roomService.updateRoom(id, room));
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
    }

}
