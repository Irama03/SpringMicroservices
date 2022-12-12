package com.example.authservice.cucumber.stepdefs;

import com.example.authservice.dtos.UserPostDto;
import com.example.authservice.models.User;
import com.example.authservice.models.UserRole;
import com.example.authservice.services.UserService;
import com.example.authservice.utils.JwtGenerator;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class UserBusinessCommunicationStepDefs {

    private static final String BASE_URL = "http://localhost:8082";
    private String adminToken;
    private final UserService userService;

    private final JwtGenerator generator;

    private User client;
    private User lessor;

    @And("I create user with role Client")
    public void iCreateUserWithRoleClient() {
        UserPostDto postDto = new UserPostDto("Client user", "clientuser@gmail.com", "+380978243212", "password",
                UserRole.CLIENT);
        client = userService.create(postDto);
        adminToken = generator.generateToken(new User("Admin", "email1111@gmail.com", "11111111111", UserRole.ADMIN));
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
        //HttpGet request = new HttpGet(BASE_URL + "/api/clients/" + client.getId());
        HttpGet request = new HttpGet(BASE_URL + "/api/clients/");
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", adminToken);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("--------------------" + responseString);
        Assertions.assertThat(responseString.contains("\"name\":\"Client user\"")).isTrue();
    }

    @And("Lessor is created on BusinessLogic")
    public void lessorIsCreatedOnBusinessLogic() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/api/lessors/");
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", adminToken);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println("--------------------" + responseString);
        Assertions.assertThat(responseString.contains("\"name\":\"Lessor user\"")).isTrue();
    }
}