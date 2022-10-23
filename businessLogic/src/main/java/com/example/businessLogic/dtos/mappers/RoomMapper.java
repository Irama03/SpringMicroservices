package com.example.businessLogic.dtos.mappers;

import com.example.businessLogic.dtos.rooms.RoomGetDto;
import com.example.businessLogic.dtos.rooms.RoomPostDto;
import com.example.businessLogic.dtos.rooms.RoomPutDto;
import com.example.businessLogic.models.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Iterable<RoomGetDto> roomsToRoomsGetDto(Iterable<Room>rooms);
    RoomGetDto roomToRoomGetDto(Room room);

    Room roomPostDtoToRoom(RoomPostDto roomPostDto);
    Room roomPutDtoToRoom(RoomPutDto roomPutDto);
}
