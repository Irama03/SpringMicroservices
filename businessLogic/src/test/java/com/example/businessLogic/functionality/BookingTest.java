package com.example.businessLogic.functionality;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.*;
import com.example.businessLogic.services.interfaces.BookingService;
import com.example.businessLogic.services.interfaces.ClientService;
import com.example.businessLogic.services.interfaces.LessorService;
import com.example.businessLogic.services.interfaces.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private LessorService lessorService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void getAllBookings_whenClientRole_thenReturn200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Client client = clientService.create(new Client("hlib", "example1@gmail.com", "+380666298353"));

        bookingService.createBooking(new Booking((long)1, room, client, 200.0, new Date(), new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-16"), new Date(), BookingStatus.NEW));
        bookingService.createBooking(new Booking((long)2, room, client, 200.0, new Date(), new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-16"), new Date(), BookingStatus.APPROVED));

        mockMvc.perform(get("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void addBooking_whenClient_thenBookingCreated() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Client client = clientService.create(new Client("hlib", "example1@gmail.com", "+380666298353"));
        String json = "{\n" +
                "    \"room_id\":" + room.getId() + ",\n" +
                "    \"client_id\":"+ client.getId() +",\n" +
                "    \"price\":\"200.0\",\n" +
                "    \"start_date\":\"2022-12-16\",\n" +
                "    \"end_date\":\"2022-12-20\"\n" +
                "}";
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.room.id").value(room.getId()))
                .andExpect(jsonPath("$.client.id").value(client.getId()));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void updateBookingStatus_whenLessor_thenStatusUpdated() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Client client = clientService.create(new Client("hlib", "example1@gmail.com", "+380666298353"));
        Booking booking = bookingService.createBooking(new Booking((long)1, room, client, 200.0, new Date(), new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-16"), new Date(), BookingStatus.NEW));

        String json = "{\n" +
                        "    \"status\":\"APPROVED\"\n" +
                        "}";
        mockMvc.perform(put("/api/bookings/" + booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void deleteBooking_whenAdmin_thenBookingDeleted() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Client client = clientService.create(new Client("hlib", "example1@gmail.com", "+380666298353"));
        Booking booking = bookingService.createBooking(new Booking((long)1, room, client, 200.0, new Date(), new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-16"), new Date(), BookingStatus.NEW));

        mockMvc.perform(delete("/api/bookings/" + booking.getId())).andExpect(status().isOk());
        mockMvc.perform(get("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }



}
