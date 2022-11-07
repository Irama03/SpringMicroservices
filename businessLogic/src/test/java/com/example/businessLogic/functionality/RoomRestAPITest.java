package com.example.businessLogic.functionality;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.models.RoomType;
import com.example.businessLogic.services.interfaces.LessorService;
import com.example.businessLogic.services.interfaces.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoomRestAPITest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RoomService roomService;
    @Autowired
    private LessorService lessorService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void getAllRooms_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        roomService.addRoom(new Room("Room 2", RoomType.ROOM, "description", lessor, "Address2", "City", 300.0, 3));
        mockMvc.perform(get("/api/rooms").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void getRoomsOfCity_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        roomService.addRoom(new Room("Room 2", RoomType.ROOM, "description", lessor, "Address2", "City", 300.0, 3));
        roomService.addRoom(new Room("Room 3", RoomType.ROOM, "description", lessor, "Address3", "CityAnother", 350.0, 3));
        mockMvc.perform(get("/api/rooms/of_city/City").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].city").value("City"))
                .andExpect(jsonPath("$[1].city").value("City"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void getRoomById_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        mockMvc.perform(get("/api/rooms/" + room.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(room.getId()))
                .andExpect(jsonPath("$.name").value("Room 1"))
                .andExpect(jsonPath("$.city").value("City"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void getRoomByNotExistingId_shouldSucceedWith404() throws Exception {
        mockMvc.perform(get("/api/rooms/111").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void addRoom_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(room);
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Room 1"))
                .andExpect(jsonPath("$.city").value("City"));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void addRoomWithExistingName_shouldSucceedWith400() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Room room = new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(room);
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void updateRoom_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Room upd = new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City1", 200.0, 2);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/rooms/" + room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(room.getId()))
                .andExpect(jsonPath("$.name").value("Room 1"))
                .andExpect(jsonPath("$.city").value("City1"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void updateRoomByNotExistingId_shouldSucceedWith404() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room upd = new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City1", 200.0, 2);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/rooms/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void updateRoomWithExistingName_shouldSucceedWith400() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        Room room = roomService.addRoom(new Room("Room 2", RoomType.ROOM, "description", lessor, "Address2", "City", 200.0, 2));
        Room upd = new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/rooms/" + room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void deleteRoom_shouldSucceedWith200() throws Exception {
        Lessor lessor = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654562"));
        Room room = roomService.addRoom(new Room("Room 1", RoomType.ROOM, "description", lessor, "Address1", "City", 200.0, 2));
        mockMvc.perform(delete("/api/rooms/" + room.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void deleteRoomByNotExistingId_shouldSucceedWith404() throws Exception {
        mockMvc.perform(delete("/api/rooms/111").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

}
