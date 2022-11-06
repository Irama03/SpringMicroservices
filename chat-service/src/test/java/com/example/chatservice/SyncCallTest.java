package com.example.chatservice;

import com.example.chatservice.apiCommunication.BusinessLogicWebClient;
import com.example.chatservice.dtos.clients.ClientSlimGetDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SyncCallTest {

    private BusinessLogicWebClient businessLogicWebClient;
    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String rootUrl = server.url("/api/").toString();
        businessLogicWebClient = new BusinessLogicWebClient(WebClient.create(rootUrl));
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void testSyncCall() throws InterruptedException {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("{\n" +
                        "    \"id\": 3,\n" +
                        "    \"name\": \"Client first\"\n" +
                        "}");
        server.enqueue(response);
        //no need to call block - evidence of sync call
        ClientSlimGetDto result = businessLogicWebClient.fetchUser(3L);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Client first");

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).startsWith("/api/clients/3");
    }

}

