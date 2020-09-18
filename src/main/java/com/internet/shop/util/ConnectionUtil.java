package com.internet.shop.util;

import com.internet.shop.exception.DataProcessingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static final String URL
            = "jdbc:postgresql://localhost:5432/bmw_shop?user=postgres&password=212069";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataProcessingException("PostgreSQL JDBC Driver is not found."
                                              + " Include it in your library path", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new DataProcessingException("Connection to DB failed", e);
        }
    }
}
