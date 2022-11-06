package com.example.reportservice;

import com.example.reportservice.apiCommunication.BusinessLogicWebClient;
import com.example.reportservice.models.StatisticsValues;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AsyncCallTest {

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
    void testAsyncCall() throws InterruptedException {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("[\n" +
                        "    {\n" +
                        "        \"lessorId\": 5,\n" +
                        "        \"name\": \"Lessor second\",\n" +
                        "        \"quantityOfFinishedBookings\": 1,\n" +
                        "        \"quantityOfBookingsInProgress\": 0,\n" +
                        "        \"lessorIncome\": 200.0,\n" +
                        "        \"quantityOfClients\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"lessorId\": 1,\n" +
                        "        \"name\": \"Lessor first\",\n" +
                        "        \"quantityOfFinishedBookings\": 0,\n" +
                        "        \"quantityOfBookingsInProgress\": 1,\n" +
                        "        \"lessorIncome\": 100.0,\n" +
                        "        \"quantityOfClients\": 1\n" +
                        "    }\n" +
                        "]");
        server.enqueue(response);
        //toStream().collect() - evidence of async call
        List<StatisticsValues> result = businessLogicWebClient.fetchDataForStatistics()
                .toStream().collect(Collectors.toList());

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Lessor second");

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).startsWith("/api/data_for_statistics");
    }

}

