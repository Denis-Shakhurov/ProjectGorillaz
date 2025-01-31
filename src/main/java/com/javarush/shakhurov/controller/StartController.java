package com.javarush.shakhurov.controller;

import com.javarush.shakhurov.dto.BasePage;
import com.javarush.shakhurov.model.Order;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.service.OrderService;
import com.javarush.shakhurov.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class StartController extends BaseController{
    private final UserService userService;
    private final OrderService orderService;

    public StartController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    public void index(Context ctx) {
        BasePage basePage = new BasePage();

        String userId = ctx.cookie(USER_ID);
        Integer id = userId != null && !userId.equals("") ? Integer.parseInt(userId) : null;

        List<Order> orders = new ArrayList<>();

        if (id != null) {
            userService.findById(id).ifPresent(user -> addUserInfoInBasePage(basePage, user));
            orders.addAll(orderService.getAllByServicesByUserId(id));
        }

        List<User> masters = userService.getAllByRole("master");

        basePage.setMasters(masters);
        basePage.setFlash(ctx.consumeSessionAttribute(FLASH));
        basePage.setOrders(orders);

        ctx.status(HttpStatus.OK);
        ctx.render("start.jte", model(PAGE, basePage));
    }
}
