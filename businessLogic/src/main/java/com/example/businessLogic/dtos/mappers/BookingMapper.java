package com.example.businessLogic.dtos.mappers;

import com.example.businessLogic.dtos.bookings.BookingGetDto;
import com.example.businessLogic.dtos.bookings.BookingPostDto;
import com.example.businessLogic.models.Booking;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.models.Room;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Iterable<BookingGetDto> bookingsToBookingsGetDto(Iterable<Booking> bookings);
    @Mapping(target="room", source="roomId" ,qualifiedByName="roomIdToRoom")
    @Mapping(target="client", source="clientId" ,qualifiedByName="clientIdToClient")
    Booking bookingPostDtoToBooking(BookingPostDto dto);

    BookingGetDto bookingToBookingGetDto(Booking booking);

    @Named("roomIdToRoom")
    public static Room roomIdToRoom(Long roomId) {
        return new Room(roomId);
    }

    @Named("clientIdToClient")
    public static Client clientIdToClient(Long clientId) {
        return new Client(clientId);
    }
}
