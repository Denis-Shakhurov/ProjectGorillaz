package com.javarush.shakhurov;

import com.javarush.shakhurov.controller.StartController;
import com.javarush.shakhurov.dto.UserPage;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.service.UserService;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StartControllerTest {
    private static Context ctx;
    private final StartController startController = new StartController();

    @BeforeEach
    public final void setUp() throws Exception {
        ctx = mock(Context.class);
    }

    @Test
    @DisplayName("Valid userId cookie returns correct user data and renders page")
    public void validUserIdCookieReturnsUserDataTest() throws SQLException {
        UserService userService = mock(UserService.class);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@test.com");
        testUser.setRole("user");
        UserPage page = new UserPage(testUser);

        when(ctx.cookie("userId")).thenReturn("1");
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(ctx.consumeSessionAttribute("flash")).thenReturn("test flash");

        startController.index(ctx);

        verify(ctx).render(eq("start.jte"), argThat(model -> {
                return page.getUser().getId().equals(1L) &&
                        page.getUser().getName().equals("Test User") &&
                        page.getUser().getEmail().equals("test@test.com");
            }));

        assertEquals(testUser, page.getUser());
    }

    @Test
    @DisplayName("Empty userId cookie returns null user")
    public void emptyUserIdCookieReturnsNullUserTest() throws SQLException {
        when(ctx.cookie("userId")).thenReturn("");

        startController.index(ctx);

        verify(ctx).render(eq("start.jte"), argThat(model -> {
            UserPage page = (UserPage) ((Map<String,Object>)model).get("page");
            return page.getUser() == null;
        }));
    }
}
