package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.dto.BasePage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RegistrationController extends BaseController {

    public void index(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        ctx.render("users/registration.jte", model(PAGE, page));
    }
}
