package com.javarush.shakhurov.model;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ADMIN,
    USER,
    MASTER,
    GUEST
}
