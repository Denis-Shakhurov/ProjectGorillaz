package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.config.Provider;
import com.javarush.shakhurov.service.GameService;
import com.javarush.shakhurov.service.UserService;
import com.javarush.shakhurov.model.User;
import io.javalin.http.Context;

public class BaseController {
    protected final UserService userService = new UserService();
    protected final GameService gameService = new GameService();

    protected final static String FLASH = "flash";
    protected final static String JWT = "jwt";
    protected final static String USER_ID = "userId";
    protected final static String ID = "id";
    protected final static String PAGE = "page";

    protected void setTokenForCookie(Context ctx, User user, Provider provider) {
        var token = provider.create().generateToken(user);
        ctx.cookie(JWT, token);
    }
}
