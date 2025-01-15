package com.javarush.shakhurov.config;

import com.javarush.shakhurov.App;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class MyDataSource {

    public HikariDataSource getHikariDataSource() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        var dataSource =  new HikariDataSource(config);

        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
