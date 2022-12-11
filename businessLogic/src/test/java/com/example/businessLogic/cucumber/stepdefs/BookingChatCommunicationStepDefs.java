package com.example.businessLogic.cucumber.stepdefs;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.*;
import com.example.businessLogic.services.interfaces.BookingService;
import com.example.businessLogic.services.interfaces.ClientService;
import com.example.businessLogic.services.interfaces.LessorService;
import com.example.businessLogic.services.interfaces.RoomService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class BookingChatCommunicationStepDefs {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkFETUlOIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgyL2FwaS9sZXNzb3JzIiwiZXhwIjoxNjk5MjE2NDk3fQ.YJxdKh8th1uFcTAH11oCDaaeJRSElbSYgwWZS75VP54";

    private final ClientService clientService;
    private final LessorService lessorService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    private Client client1;
    private Lessor lessor1;
    private Room room1;

    @When("I create client")
    public void iCreateClient() {
        Client client = new Client("Client", "client1@gmail.com", "+380675574099");
        client1 = clientService.create(client);
    }

    @And("I create lessor")
    public void iCreateLessor() {
        LessorPostDto lessor = new LessorPostDto("Lessor", "lessor1@gmail.com", "+380375574099");
        lessor1 = lessorService.create(lessor);
    }

    @And("I create room for this lessor")
    public void iCreateRoomForThisLessor() {
        Room room = new Room("Room1", RoomType.ROOM, "desc1", lessor1, "address1", "city1",
                111.0, 2);
        room1 = roomService.addRoom(room);
    }


    @And("I create booking with this client and this room")
    public void iCreateBookingWithThisClientAndThisRoom() {
        Booking booking = new Booking(null, room1, client1, 10.2, new Date(), new Date(), new Date(),
                BookingStatus.NEW);
        bookingService.createBooking(booking);
    }

    @Then("chat with client and lessor is created")
    public void chatWithClientAndLessorIsCreated() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/api/chats/with-users/" + client1.getId() + "," + lessor1.getId());
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", ADMIN_TOKEN);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        Assertions.assertThat(jsonResponse.has("id")).isEqualTo(true);
        List<Long> userIds = objectMapper.readValue(jsonResponse.get("users").toString(), new TypeReference<>(){});
        Assertions.assertThat(userIds).hasSize(2);
        Assertions.assertThat(userIds).contains(client1.getId(), lessor1.getId());
    }
}
