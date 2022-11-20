package com.example.businessLogic.services.implementations;

import com.example.businessLogic.dtos.bookings.BookingPutDto;
import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.booking.IllegalDateException;
import com.example.businessLogic.models.*;
import com.example.businessLogic.repositories.BookingRepository;
import com.example.businessLogic.services.interfaces.BookingService;
import com.example.businessLogic.services.interfaces.ClientService;
import com.example.businessLogic.services.interfaces.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final RoomService roomService;
    private final ClientService clientService;
    private final JMSService jmsService;

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
        //booking.setStartDate(new Date(4, Calendar.NOVEMBER, 2022));
        //booking.setEndDate(new Date(7, Calendar.NOVEMBER, 2022));
        checkDates(booking);
        Room room = roomService.getRoomById(booking.getRoom().getId());
        Lessor lessor = room.getLessor();
        Client client = clientService.getById(booking.getClient().getId());
        booking.setRoom(room);
        booking.setClient(client);
        Booking savedBooking = repository.save(booking);
        sendBookingMessage(lessor, client, room);
        return savedBooking;
    }

    public void sendBookingMessage(Lessor lessor, Client client, Room room) {
        String text = "Your booking for room '" + room.toString() + "' has been created.";
        BookingMessage message = new BookingMessage(text, lessor.getId(), client.getId());
//        jmsService.sendBookingMessage(message);
    }

    @Override
    public void deleteBooking(Long id) {
        repository
                .findById(id)
                .ifPresentOrElse(b -> repository.deleteById(id),
                        () -> {
                            throw new RecordNotFoundException(Booking.class, "id", id);
                        });
    }

    @Override
    public Booking updateBooking(Long id, BookingPutDto dto) {
        return repository
                .findById(id)
                .map(b -> {
                    b.setStatus(dto.getStatus());
                    return repository.save(b);
                })
                .orElseThrow(() -> {
                    throw new RecordNotFoundException(Booking.class, "id", id);
                });
    }

    private void checkDates(Booking booking) {
        if (booking.getEndDate().before(booking.getStartDate()))
            throw new IllegalDateException(booking.getEndDate());
    }
}
