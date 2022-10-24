package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.bookings.BookingGetDto;
import com.example.businessLogic.dtos.bookings.BookingPostDto;
import com.example.businessLogic.dtos.bookings.BookingPutDto;
import com.example.businessLogic.dtos.mappers.BookingMapper;
import com.example.businessLogic.services.interfaces.BookingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;

    public BookingController(BookingService bookingService, BookingMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @GetMapping
    public Iterable<BookingGetDto> getAll() {
        return mapper.bookingsToBookingsGetDto(bookingService.getAll());
    }

    @PostMapping
    public BookingGetDto addBooking(@Valid @RequestBody BookingPostDto dto) {
        return mapper.bookingToBookingGetDto(bookingService.createBooking(mapper.bookingPostDtoToBooking(dto)));
    }

    @GetMapping("/{id}")
    public BookingGetDto getBooking(@PathVariable(name = "id") Long id) {
        return mapper.bookingToBookingGetDto(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}")
    public BookingGetDto updateStatus(@PathVariable(name = "id") Long id, @Valid @RequestBody BookingPutDto dto) {
        return mapper.bookingToBookingGetDto(bookingService.updateBooking(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable(name = "id") Long id) {
        this.bookingService.deleteBooking(id);
    }
}
