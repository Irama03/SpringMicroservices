package com.example.businessLogic.services.interfaces;

import com.example.businessLogic.dtos.bookings.BookingPutDto;
import com.example.businessLogic.models.Booking;

public interface BookingService {

    Iterable<Booking> getAll();

    Booking getBookingById(Long id);

    Booking createBooking(Booking booking);

    void deleteBooking(Long id);

    Booking updateBooking(Long id, BookingPutDto dto);
}
