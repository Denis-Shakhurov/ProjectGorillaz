package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.dto.BasePage;
import com.javarush.shakhurov.dto.UserPage;
import com.javarush.shakhurov.model.User;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class StartController extends BaseController {

    public void index(Context ctx) throws SQLException {
        String userId = ctx.cookie(USER_ID);
        var id = userId != null && !userId.equals("") ? Long.parseLong(userId) : null;
        var user = id != null ? userService.findById(id).orElse(null) : null;
        var page = new UserPage(user);

        BasePage basePage = new BasePage();
        basePage.setUserInfo(addUserInfo(user));

        page.setFlash(ctx.consumeSessionAttribute(FLASH));

        ctx.render("start.jte", model(PAGE, page));
    }

    private Map<String, String> addUserInfo(User user) {
        return user == null ? new HashMap<>() : Map.of(
                "id", String.valueOf(user.getId()),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole());
    }
}
