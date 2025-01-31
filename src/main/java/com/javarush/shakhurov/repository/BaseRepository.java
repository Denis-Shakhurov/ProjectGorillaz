package com.javarush.shakhurov.repository;

import com.javarush.shakhurov.config.MySessionFactory;
import org.hibernate.SessionFactory;

public class BaseRepository {
    private final MySessionFactory mySessionFactory = new MySessionFactory();
    protected SessionFactory sessionFactory = mySessionFactory.getSessionFactory();
}
