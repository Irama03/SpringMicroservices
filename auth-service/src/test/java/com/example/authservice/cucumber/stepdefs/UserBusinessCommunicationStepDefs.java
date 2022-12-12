package com.example.authservice.cucumber.stepdefs;

import com.example.authservice.dtos.UserPostDto;
import com.example.authservice.models.User;
import com.example.authservice.models.UserRole;
import com.example.authservice.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class UserBusinessCommunicationStepDefs {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkFETUlOIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgyL2FwaS9sZXNzb3JzIiwiZXhwIjoxNjk5MjE2NDk3fQ.YJxdKh8th1uFcTAH11oCDaaeJRSElbSYgwWZS75VP54";
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private User client;
    private User lessor;

    @And("I create user with role Client")
    public void iCreateUserWithRoleClient() {
        UserPostDto postDto = new UserPostDto("Client user", "clientuser@gmail.com", "+380978243212", "password",
                UserRole.CLIENT);
        client = userService.create(postDto);
    }

    @And("I create user with role Lessor")
    public void iCreateUserWithRoleLessor() {
        UserPostDto postDto = new UserPostDto("Lessor user", "lessoruser@gmail.com", "+380978221212", "password",
                UserRole.LESSOR);
        lessor = userService.create(postDto);
    }

    @Then("Client is created on BusinessLogic")
    public void clientIsCreatedOnBusinessLogic() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/api/clients/" + client.getId());
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", ADMIN_TOKEN);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        Assertions.assertThat(jsonResponse.get("id").asLong()).isEqualTo(client.getId());
        Assertions.assertThat(jsonResponse.get("name").toString()).isEqualTo(client.getName());
    }

    @And("Lessor is created on BusinessLogic")
    public void lessorIsCreatedOnBusinessLogic() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/api/lessors/" + lessor.getId());
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", ADMIN_TOKEN);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        Assertions.assertThat(jsonResponse.get("id").asLong()).isEqualTo(lessor.getId());
        Assertions.assertThat(jsonResponse.get("name").toString()).isEqualTo(lessor.getName());
    }
}