package com.infodat.library_borrow_springboot.backend;

import java.sql.*;

public class InitDatabase {
    // Database connection variables
    public static Connection conn = null;
    public static Statement stmt = null;
    static final String DB_URL = "jdbc:mysql://localhost:3306/lms?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
    static final String USER = "sqluser";
    static final String PASS = "password";

    public InitDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        testConnection();
    }

    public static void testConnection() {
        String QUERY = "SELECT * FROM users";

        try (ResultSet rs = stmt.executeQuery(QUERY);) {
            while (rs.next()) {
                // Retrieve by column name
                System.out.println("Connection Successful!");
                break;
            }
        } catch (Exception e) {
            System.out.println("Failed");
        }
    }
}
