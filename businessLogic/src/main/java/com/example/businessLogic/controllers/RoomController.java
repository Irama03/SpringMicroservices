package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.mappers.RoomMapper;
import com.example.businessLogic.dtos.rooms.RoomGetDto;
import com.example.businessLogic.dtos.rooms.RoomPostDto;
import com.example.businessLogic.dtos.rooms.RoomPutDto;
import com.example.businessLogic.models.LogMessage;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.services.implementations.JMSService;
import com.example.businessLogic.services.interfaces.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/rooms")
@Validated
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final JMSService jmsService;

    public RoomController(RoomService roomService, RoomMapper roomMapper, JMSService jmsService) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.jmsService = jmsService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR','CLIENT')")
    public Iterable<RoomGetDto> getRooms(){
        jmsService.sendMessageToTopic(new LogMessage("INFO", "get all rooms", new Date()));
        return roomMapper.roomsToRoomsGetDto(roomService.getAll());
    }

    @GetMapping("/of_city/{city}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR','CLIENT')")
    public Iterable<RoomGetDto> getRoomsOfCity(@PathVariable("city") String city){
        return roomMapper.roomsToRoomsGetDto(roomService.getRoomsOfCity(city));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR','CLIENT')")
    public RoomGetDto getRoom(@PathVariable("id") Long id) {
        return roomMapper.roomToRoomGetDto(roomService.getRoomById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public RoomGetDto addRoom(@Valid @RequestBody RoomPostDto roomPostDto){
        return roomMapper.roomToRoomGetDto(roomService.addRoom(roomMapper.roomPostDtoToRoom(roomPostDto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public RoomGetDto updateRoom(@PathVariable("id") Long id, @Valid @RequestBody RoomPutDto roomPutDto) {
        Room room = roomMapper.roomPutDtoToRoom(roomPutDto);
        room.setId(id);
        return roomMapper.roomToRoomGetDto(roomService.updateRoom(id, room));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public void deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
    }

}
