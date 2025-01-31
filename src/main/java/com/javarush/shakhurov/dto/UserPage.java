package com.javarush.shakhurov.dto;

import lombok.Getter;
import lombok.Setter;
import com.javarush.shakhurov.model.Order;
import com.javarush.shakhurov.model.Service;
import com.javarush.shakhurov.model.User;

import java.util.List;

@Getter
@Setter
public class UserPage extends BasePage {
    private User user;
    private List<Service> services;
    private List<Order> orders;
}
