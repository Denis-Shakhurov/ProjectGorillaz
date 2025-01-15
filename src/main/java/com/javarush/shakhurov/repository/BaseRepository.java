package com.javarush.shakhurov.repository;

import com.javarush.shakhurov.config.MyDataSource;
import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    private final MyDataSource myDataSource = new MyDataSource();
    protected HikariDataSource dataSource = myDataSource.getHikariDataSource();
}
