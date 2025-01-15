package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.config.Provider;
import com.javarush.shakhurov.dto.UserPage;
import com.javarush.shakhurov.dto.UsersPage;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.utils.NamedRoutes;
import com.lambdaworks.crypto.SCryptUtil;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.util.Objects;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UserController extends BaseController{
    private final Provider provider = new Provider();

    public void index(Context ctx) {
        var users = userService.getAll();
        var page = new UsersPage(users);
        ctx.status(HttpStatus.OK);
        ctx.render("users/index.jte", model(PAGE, page));
    }

    public void show(Context ctx) {
        var id = Long.parseLong(ctx.pathParam("id"));
        var user = userService.findById(id)
                .orElseThrow(() -> new NotFoundResponse("User with id = " + id + " not found"));
        var games = gameService.getAllGameForUser(id);
        var page = new UserPage(user, games);
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        ctx.status(HttpStatus.OK);
        ctx.render("users/show.jte", model(PAGE, page));
    }

    public void create(Context ctx) {
        var name = ctx.formParam("name");
        var email = ctx.formParam("email");
        var password = ctx.formParam("password");
        var role = ctx.formParam("role") == null ? "user" : ctx.formParam("role");

        if (isValidName(name)
                && isValidEmail(email)
                && isValidPassword(password)
                && userService.existByEmail(email)) {
            var passwordHash = SCryptUtil.scrypt(password, 2, 2, 2);
            var user = new User(name, email, passwordHash, role);
            var id = userService.create(user);

            setTokenForCookie(ctx, user, provider);
            ctx.cookie(USER_ID, String.valueOf(id));

            ctx.sessionAttribute(FLASH, "Игрок создан");
            ctx.status(HttpStatus.CREATED);
            ctx.redirect(NamedRoutes.startPath());
        } else if (!userService.existByEmail(email)) {
            ctx.sessionAttribute(FLASH, "Игрок с таким " + email + " уже существует");
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.redirect(NamedRoutes.registrationPath());
        } else {
            ctx.sessionAttribute(FLASH, "Неккоректные данные");
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.redirect(NamedRoutes.registrationPath());
        }
    }

    public void login(Context ctx) {
        try {
            var email = ctx.formParam("email");
            var password = ctx.formParam("password");
            var user = userService.findByEmail(email)
                    .orElseThrow(() -> new NotFoundResponse("User with email = " + email + " not found"));
            if (user.getPassword() != null && SCryptUtil.check(password, user.getPassword())) {

                setTokenForCookie(ctx, user, provider);
                ctx.cookie(USER_ID, String.valueOf(user.getId()));

                ctx.sessionAttribute(FLASH, "Привет " + user.getName() + " !");
                ctx.status(HttpStatus.OK);
                ctx.redirect(NamedRoutes.startPath());
            } else if (user.getEmail() == null) {
                ctx.sessionAttribute(FLASH, "Игрок с email - \"" + ctx.formParam("email") + "\" не существует");
                ctx.status(HttpStatus.BAD_REQUEST);
                ctx.redirect(NamedRoutes.loginPath());
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
                ctx.sessionAttribute(FLASH, "Некорректные логин или пароль");
                ctx.redirect(NamedRoutes.loginPath());
            }
        } catch (NotFoundResponse e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.redirect(NamedRoutes.loginPath());
        }
    }

    public void logout(Context ctx) {
        ctx.sessionAttribute(FLASH, null);
        ctx.cookie(JWT, "");
        ctx.cookie(USER_ID, "");
        ctx.redirect(NamedRoutes.startPath());
    }

    public void destroy(Context ctx) {
        var id = Long.parseLong(Objects.requireNonNull(ctx.formParam(ID)));
        gameService.destroy(id);
        userService.delete(id);
        ctx.status(HttpStatus.OK);
        ctx.redirect(NamedRoutes.usersPath());
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("(\\w+|[а-яА-Я0-9]+)") && name.length() >= 4;
    }

    private boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            return true;
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() > 5;
    }
}
