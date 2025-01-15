package com.javarush.shakhurov;

import com.javarush.shakhurov.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    Javalin app;
    private static MockWebServer mockBackEnd;
    private final CreateApp createApp = new CreateApp();

    @BeforeAll
    static void setUpMock() throws IOException {
        mockBackEnd = new MockWebServer();
        var html = Files.readString(Paths.get("src/test/resources/pageForTest.html"));
        var serverResponse = new MockResponse()
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html);
        mockBackEnd.enqueue(serverResponse);
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    public final void setUp() throws Exception {
        app = createApp.getApp();
    }

    @Test
    public void startPageIT() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.startPath());

            assertEquals(HttpStatus.OK.getCode(), response.code());
            assertTrue(response.body().string().contains("QuestGame"));
        });
    }

    @Test
    public void statisticPageIT() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.statisticPath());

            assertEquals(HttpStatus.OK.getCode(), response.code());
            assertTrue(response.body().string().contains("Статистика игр"));
        });
    }
}
