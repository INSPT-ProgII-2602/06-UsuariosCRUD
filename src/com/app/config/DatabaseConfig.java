/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.config;

/**
 *
 * @author Charly
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {
    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties props = new Properties();
    private static boolean initialized = false;
    
    static {
        try (InputStream is = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
                initialized = true;
            }
        } catch (IOException e) {
            System.err.println("Advertencia: No se encontró " + PROPERTIES_FILE 
                + ". Usando valores por defecto.");
        }
    }
    
    private DatabaseConfig() {}
    
    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.username");
        String pass = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
    
    public static void loadDriver() {
        try {
            Class.forName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver MySQL no encontrado", e);
        }
    }
}