package com.example.businessLogic.services.implementations;

import com.example.businessLogic.models.*;
import com.example.businessLogic.services.interfaces.DataForStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class DataForStatisticsServiceImpl implements DataForStatisticsService {

    @Autowired
    private LessorServiceImpl lessorService;

    @Override
    public Iterable<StatisticsValues> getDataForStatistics() {
        Set<StatisticsValues> statisticsValues = new HashSet<>();
        Iterable<Lessor> lessors = lessorService.getAll();
        for (Lessor lessor: lessors) {
            Iterable<Booking> lessorBookings = getBookingsOfRooms(lessor.getRooms());
            Integer quantityOfFinishedBookings = 0;
            Integer quantityOfInProgressBookings = 0;
            Double income = 0.0;

            Set<Long> clientsIds = new HashSet<>();
            Date now = new Date();
            for (Booking booking: lessorBookings) {
                if (booking.getStatus() == BookingStatus.APPROVED) {
                    if (booking.getEndDate().before(now)) {
                        quantityOfFinishedBookings++;
                        income = increaseValues(booking, income, clientsIds);
                    }
                    else if ((booking.getStartDate().before(now) || isSameDay(booking.getStartDate(), now)) &&
                            (booking.getEndDate().after(now) || isSameDay(booking.getEndDate(), now))) {
                        quantityOfInProgressBookings++;
                        income = increaseValues(booking, income, clientsIds);
                    }
                }

            }
            Integer quantityOfClients = clientsIds.size();

            statisticsValues.add(
                    new StatisticsValues(lessor.getId(), lessor.getName(),
                            quantityOfFinishedBookings, quantityOfInProgressBookings,
                            income, quantityOfClients));
        }
        return statisticsValues;
    }

    private Iterable<Booking> getBookingsOfRooms(Iterable<Room> rooms) {
        Set<Booking> bookings = new HashSet<>();
        for (Room room: rooms) {
            bookings.addAll(room.getBookings());
        }
        return bookings;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private Double increaseValues(Booking booking, Double income, Set<Long> clientsIds) {
        income += booking.getPrice();
        clientsIds.add(booking.getClient().getId());
        return income;
    }

}
