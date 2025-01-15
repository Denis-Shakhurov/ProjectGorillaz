package com.javarush.shakhurov.model;

import com.javarush.shakhurov.model.game.CalcGame;
import com.javarush.shakhurov.model.game.EvenGame;
import com.javarush.shakhurov.model.game.Game;
import com.javarush.shakhurov.model.game.ProgressionGame;

import java.util.HashMap;
import java.util.Map;

public class FactoryGame {

    public Game getGame(String nameGame) {
        Map<String, Game> map = new HashMap<>();

        map.put("CalcGame", new CalcGame());
        map.put("Калькулятор", new CalcGame());
        map.put("EvenGame", new EvenGame());
        map.put("Чётное/нечётное", new EvenGame());
        map.put("ProgressionGame", new ProgressionGame());
        map.put("Прогрессия", new ProgressionGame());

        return map.getOrDefault(nameGame, null);
    }
}
