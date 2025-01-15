package com.javarush.shakhurov.service;

import com.javarush.shakhurov.model.User;
import com.javarush.shakhurov.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getAll() {
        try {
            return userRepository.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existByEmail(String email) {
        try {
            return userRepository.existByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long create(User user) {
        try {
            return userRepository.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        try {
            userRepository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
