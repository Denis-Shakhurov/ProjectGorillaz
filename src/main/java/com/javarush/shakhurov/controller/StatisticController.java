package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.dto.StatisticPage;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import static io.javalin.rendering.template.TemplateUtil.model;

public class StatisticController extends BaseController {

    public void index(Context ctx) {
        var usersWithGames = gameService.getAllUserNameWithGames();
        var page = new StatisticPage(usersWithGames);
        ctx.render("statistic/index.jte", model(PAGE, page)).status(HttpStatus.OK);
    }
}
