package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract class DataBasePool {

    protected final BasicDataSource pool = new BasicDataSource();

    public DataBasePool() {
        Properties properties = new Properties();
        try (InputStream inputStream = BasicDataSource.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
        }
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(properties.getProperty("driver"));
        pool.setUrl(properties.getProperty("url"));
        pool.setUsername(properties.getProperty("username"));
        pool.setPassword(properties.getProperty("password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }
}
