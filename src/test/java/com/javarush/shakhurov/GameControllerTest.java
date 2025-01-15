package com.javarush.shakhurov;

import com.javarush.shakhurov.controller.GameController;
import com.javarush.shakhurov.model.FactoryGame;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.model.game.Game;
import com.javarush.shakhurov.service.GameService;
import com.javarush.shakhurov.service.UserService;
import com.javarush.shakhurov.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameControllerTest {
    Javalin app;
    private static MockWebServer mockBackEnd;
    private Context ctx;
    private static final FactoryGame factoryGame = new FactoryGame();
    private final CreateApp createApp = new CreateApp();
    private static final GameService gameService = new GameService();
    private static final UserService userService = new UserService();
    private final GameController gameController = new GameController();
    private static Game gameTest;
    private static Long gameId;
    private static User userTest;
    private static Long userId;

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
    public final void setUp() {
        app = createApp.getApp();
        ctx = mock(Context.class);
        userTest = new User("John", "john@example.com", "password", "user");
        userId = userService.create(userTest);
        gameTest = factoryGame.getGame("CalcGame");
        gameTest.setUserId(userId);
        gameId = gameService.create(gameTest);
    }

    @Test
    @DisplayName("Show game state and process correct answer from user")
    public void showGameTest() {
        GameService gameServiceMock = mock(GameService.class);

        when(ctx.pathParam("id")).thenReturn(gameId.toString());
        when(gameServiceMock.findById(gameId)).thenReturn(Optional.of(gameTest));
        when(ctx.formParam("answer")).thenReturn("correctAnswer");

        gameController.show(ctx);

        verify(ctx).status(HttpStatus.OK);
    }

    @Test
    @DisplayName("Show game with not authorization user")
    public void showGameWithNoAuthorizationTest() {
        User user = new User("Ivan", "ivan@gmail.com", "wqerty", "user");
        userService.create(user);

        Game game = factoryGame.getGame("CalcGame");
        game.setUserId(user.getId());
        gameService.create(game);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.gamePath(game.getId()));

            assertEquals(HttpStatus.UNAUTHORIZED.getCode(), response.code());
        });
    }

    @Test
    @DisplayName("Show game no found")
    public void showGameNoFoundTest() {
        when(ctx.pathParam("id")).thenReturn("9999999");

        assertThrows(NotFoundResponse.class, () -> {
            gameController.show(ctx);
        });

        verify(ctx, never()).render(anyString(), any());
        verify(ctx, never()).status(any());
    }

    @Test
    @DisplayName("Create game success")
    public void createGameSuccessTest() {
        User user = new User("Ivan", "ivan@gmail.com", "wqerty", "user");
        Long userId = userService.create(user);

        Game gameExpected = new FactoryGame().getGame("CalcGame");
        gameExpected.setUserId(userId);
        Long gameId = gameService.create(gameExpected);

        JavalinTest.test(app, (server, client) -> {
            client.post(NamedRoutes.userPath(userId));

            var gameActual = gameService.findById(gameId).get();
            assertEquals(gameExpected.getName(), gameActual.getName());
        });
    }

    @Test
    @DisplayName("Create new game with valid user ID and game name")
    public void createGameWithValidParamsTest() {

        when(ctx.formParam("game")).thenReturn("CalcGame");
        when(ctx.cookie("userId")).thenReturn("2");

        Game mockGame = mock(Game.class);
        when(mockGame.getName()).thenReturn("CalcGame");

        gameController.create(ctx);

        verify(ctx).status(HttpStatus.CREATED);
        verify(ctx).redirect(NamedRoutes.gamePath("2"));
    }

    @Test
    @DisplayName("Delete all games for a specific user")
    public void deleteAllGamesForUserTest() {
        Context ctx = mock(Context.class);
        Long userId = 1L;

        when(ctx.pathParam("id")).thenReturn(String.valueOf(userId));

        gameController.destroy(ctx);

        verify(ctx).status(HttpStatus.OK);
        verify(ctx).redirect(NamedRoutes.userPath(userId));
    }
}
