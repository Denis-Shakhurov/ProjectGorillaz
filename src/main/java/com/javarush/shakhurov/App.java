package com.javarush.shakhurov;

import io.javalin.Javalin;

public class App {

    public static void main(String[] args) throws Exception {
        CreateApp createApp = new CreateApp();
        // run app
        Javalin app = createApp.getApp();
        // run web server
        app.start(8080);
    }
}
