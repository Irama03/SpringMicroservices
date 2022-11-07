package com.example.businessLogic.apiTests;

import com.example.businessLogic.dtos.clients.ClientPostDto;
import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.models.Room;
import com.example.businessLogic.models.RoomType;
import com.example.businessLogic.services.interfaces.ClientService;
import com.example.businessLogic.services.interfaces.LessorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void getAllClients_shouldSucceedWith200() throws Exception {
        Client client1 = new Client("client", "client1@gmail.com", "+380676676677");
        Client client2 = new Client("client", "client2@gmail.com", "+380676676688");
        clientService.create(client1);
        clientService.create(client2);
        mockMvc.perform(get("/api/clients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    @WithMockUser(authorities = {"ADMIN", "LESSOR"})
    public void getClientById_shouldSucceedWith200() throws Exception {
        Client client = new Client("client", "client1@gmail.com", "+380676676677");
        Client savedClient = clientService.create(client);
        mockMvc.perform(get("/api/clients/" + savedClient.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedClient.getId()))
                .andExpect(jsonPath("$.name").value("client"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "LESSOR"})
    public void getClientByNotExistingId_shouldSucceedWith404() throws Exception {
        mockMvc.perform(get("/api/clients/-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "CLIENT"})
    public void addClients_shouldSucceedWith200() throws Exception {
        ClientPostDto clientPostDto = new ClientPostDto("client", "client1@gmail.com", "+380676676677");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(clientPostDto);
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("client"))
                .andExpect(jsonPath("$.email").value("client1@gmail.com"))
                .andExpect(jsonPath("$.phone").value("+380676676677"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "CLIENT"})
    public void addClientWithExistingName_shouldSucceedWith400() throws Exception {
        Client client1 = new Client("client", "client1@gmail.com", "+380676676677");
        clientService.create(client1);
        ClientPostDto clientPostDto = new ClientPostDto("client", "client1@gmail.com", "+380676676677");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(clientPostDto);
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
