package com.javarush.shakhurov.service;

import com.javarush.shakhurov.model.game.Game;
import com.javarush.shakhurov.repository.GameRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameService {
    private final GameRepository gameRepository = new GameRepository();

    public Long create(Game game) {
        try {
            return gameRepository.save(game);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Game> findById(Long id) {
        try {
            return gameRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Game> getAllGameForUser(Long userId) {
        try {
            return gameRepository.getAllGameForUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Game>> getAllUserNameWithGames() {
        try {
            return gameRepository.getAllUserNameWithGames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Game game) {
        try {
            gameRepository.update(game);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy(Long userId) {
        try {
            gameRepository.deleteAllGameForUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
