package com.example.businessLogic.services.implementations;

import com.example.businessLogic.dtos.bookings.BookingPutDto;
import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.booking.IllegalDateException;
import com.example.businessLogic.models.Booking;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.repositories.BookingRepository;
import com.example.businessLogic.services.interfaces.BookingService;
import com.example.businessLogic.services.interfaces.ClientService;
import com.example.businessLogic.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository repository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientService clientService;

    @Override
    public Iterable<Booking> getAll() {
        return repository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Booking.class, "id", id));
    }

    @Override
    public Booking createBooking(Booking booking) {
        checkDates(booking);
        Room room = roomService.getRoomById(booking.getRoom().getId());
        Client client = clientService.getById(booking.getClient().getId());
        booking.setRoom(room);
        booking.setClient(client);
        return repository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        repository
                .findById(id)
                .ifPresentOrElse(b -> repository.deleteById(id),
                        () -> {throw new RecordNotFoundException(Booking.class, "id", id);});
    }

    @Override
    public Booking updateBooking(Long id, BookingPutDto dto) {
        return repository
                .findById(id)
                .map(b -> {b.setStatus(dto.getStatus()); return repository.save(b);})
                .orElseThrow(() -> {throw new RecordNotFoundException(Booking.class, "id", id);});
    }

    private void checkDates(Booking booking) {
        if(booking.getEndDate().before(booking.getStartDate()))
            throw new IllegalDateException(booking.getEndDate());
    }
}
