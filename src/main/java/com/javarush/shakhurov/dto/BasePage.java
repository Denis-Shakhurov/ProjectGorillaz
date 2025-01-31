package com.javarush.shakhurov.dto;

import com.javarush.shakhurov.model.Order;
import com.javarush.shakhurov.model.Service;
import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.model.UserInfo;
import com.javarush.shakhurov.utils.NamedRoutes;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasePage extends NamedRoutes {
    private UserInfo userInfo;
    private String flash;
    private List<User> masters;
    private List<Order> orders;
    private Service service;
}
