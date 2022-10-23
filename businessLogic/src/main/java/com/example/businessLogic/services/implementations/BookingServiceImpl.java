package com.example.businessLogic.services.implementations;

import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.booking.IllegalDateException;
import com.example.businessLogic.models.Booking;
import com.example.businessLogic.repositories.BookingRepository;
import com.example.businessLogic.services.interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository repository;

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
        return repository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        repository
                .findById(id)
                .ifPresentOrElse(b -> repository.deleteById(id),
                        () -> {throw new RecordNotFoundException(Booking.class, "id", id);});
    }

    private void checkDates(Booking booking) {
        if(booking.getEndDate().before(booking.getStartDate()))
            throw new IllegalDateException(booking.getEndDate());
    }
}
