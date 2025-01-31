package com.javarush.shakhurov.dto;

import com.javarush.shakhurov.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsersPage extends BasePage {
    private List<User> users;
}
