package com.internet.shop.util;

import com.internet.shop.exception.DataProcessingException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/bmw_shop?";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataProcessingException("PostgreSQL JDBC Driver is not found."
                                              + " Include it in your library path", e);
        }
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        try (InputStream inputStream = ConnectionUtil.class
                .getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(inputStream);
            return DriverManager.getConnection(URL, properties);
        } catch (IOException e) {
            throw new DataProcessingException("Can not get file db.properties", e);
        } catch (SQLException e) {
            throw new DataProcessingException("Connection to DB failed", e);
        }
    }
}
