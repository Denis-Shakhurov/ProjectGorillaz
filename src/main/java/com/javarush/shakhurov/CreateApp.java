package com.javarush.shakhurov;

import com.javarush.shakhurov.config.Provider;
import com.javarush.shakhurov.controller.*;
import com.javarush.shakhurov.model.Roles;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.service.UserService;
import com.javarush.shakhurov.utils.NamedRoutes;
import com.lambdaworks.crypto.SCryptUtil;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.RouteRole;
import javalinjwt.JWTAccessManager;
import javalinjwt.JWTProvider;
import javalinjwt.JavalinJWT;

import java.util.HashMap;
import java.util.Map;

public class CreateApp {
    private final UserService userService = new UserService();
    private final StartController startController = new StartController();
    private final GameController gameController = new GameController();
    private final LoginController loginController = new LoginController();
    private final RegistrationController registrationController = new RegistrationController();
    private final StatisticController statisticController = new StatisticController();
    private final UserController userController = new UserController();
    private final Provider provider = new Provider();

    private TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public Javalin getApp() {

        var app = Javalin.create(config -> {
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
            config.bundledPlugins.enableDevLogging();
        });

        // add "admin" pre run app
        userService.create(new User("Admin", "admin@mail.com", SCryptUtil.scrypt("password", 2, 2, 2), "admin"));

        // create the provider
        JWTProvider<User> providerUser = provider.create();

        Handler decodeHandler = JavalinJWT.createCookieDecodeHandler(providerUser);
        // create the access manager
        Map<String, RouteRole> rolesMapping = new HashMap<>() {{
            put("user", Roles.USER);
            put("admin", Roles.ADMIN);
        }};

        JWTAccessManager accessManager = new JWTAccessManager("role", rolesMapping, Roles.GUEST);

        // set the paths
        app.before(decodeHandler);
        app.beforeMatched(accessManager);

        app.get(NamedRoutes.startPath(), startController::index, Roles.GUEST, Roles.USER, Roles.ADMIN);

        app.post(NamedRoutes.startPath(), gameController::create, Roles.USER, Roles.ADMIN);
        app.get(NamedRoutes.gamePath("{id}"), gameController::show, Roles.USER, Roles.ADMIN);
        app.post(NamedRoutes.gamePath("{id}"), gameController::show, Roles.USER, Roles.ADMIN);
        app.post(NamedRoutes.userPath("{id}"), gameController::destroy, Roles.USER, Roles.ADMIN);

        app.get(NamedRoutes.userPath("{id}"), userController::show, Roles.USER, Roles.ADMIN);
        app.post(NamedRoutes.registrationPath(), userController::create, Roles.GUEST, Roles.USER, Roles.ADMIN);
        app.post(NamedRoutes.loginPath(), userController::login, Roles.GUEST, Roles.USER, Roles.ADMIN);
        app.get(NamedRoutes.logoutPath(), userController::logout, Roles.USER, Roles.ADMIN);
        app.get(NamedRoutes.usersPath(), userController::index, Roles.ADMIN);
        app.post(NamedRoutes.usersPath(), userController::destroy, Roles.ADMIN);

        app.get(NamedRoutes.statisticPath(), statisticController::index, Roles.GUEST, Roles.USER, Roles.ADMIN);

        app.get(NamedRoutes.registrationPath(), registrationController::index, Roles.GUEST, Roles.USER, Roles.ADMIN);

        app.get(NamedRoutes.loginPath(), loginController::index, Roles.GUEST, Roles.USER, Roles.ADMIN);

        return app;
    }
}
