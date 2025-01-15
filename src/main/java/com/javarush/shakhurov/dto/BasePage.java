package com.javarush.shakhurov.dto;

import java.util.HashMap;
import java.util.Map;

public class BasePage {
    private String flash;
    private String statusAnswer;
    private String question;
    private static Map<String, String> userInfo = new HashMap<>();

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStatusAnswer() {
        return statusAnswer;
    }

    public void setStatusAnswer(String statusAnswer) {
        this.statusAnswer = statusAnswer;
    }

    public static Map<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public String getTitle() {
        return "Приветствую!\n"
                + "В данном приложении ты можешь испытать свои силы в логических играх.\n"
                + "В каждой игре для победы нужно правильно ответить на 3 вопроса, "
                + "если не верно ответить на один вопрос игра считается проигранной.";
    }
}
