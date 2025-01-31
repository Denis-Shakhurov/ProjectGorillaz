package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.config.Provider;
import com.javarush.shakhurov.dto.BasePage;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.model.UserInfo;
import com.javarush.shakhurov.utils.NamedRoutes;
import io.javalin.http.Context;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseController {
    protected NamedRoutes namedRoutes = new NamedRoutes();

    protected final String PAGE = "page";
    protected final String ID = "id";
    protected final String USER_ID = "userId";
    protected final String JWT = "jwt";
    protected final String FLASH = "flash";

    protected void addUserInfoInBasePage(BasePage basePage, User user) {
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        basePage.setUserInfo(userInfo);
    }

    protected void addTokenInCookie(Context ctx, User user, Provider provider) {
        String token = provider.create().generateToken(user);
        ctx.cookie(JWT, token);
    }
}
