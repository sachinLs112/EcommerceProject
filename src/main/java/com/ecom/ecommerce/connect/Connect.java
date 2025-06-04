package com.ecom.ecommerce.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect {

    private static final Logger logger = LoggerFactory.getLogger(Connect.class);

    private final String url = "jdbc:postgresql://localhost:5432/ecommerce";
    private final String user = "root";
    private final String password = "postgres";

    public Connection getconnect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            logger.info("Database connection established successfully.");
            return conn;
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to the database.", e);
        } catch (Exception e) {
            logger.error("Unexpected error during database connection.", e);
        }
        return null;
    }
}
