package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure it's added to your project.");
            e.printStackTrace();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/push-notifi";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database successfully!");

            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                System.out.println("Tables in the database:");
                while (rs.next()) {
                    String tableName = rs.getString(3);
                    System.out.println(tableName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database or retrieve tables.");
            e.printStackTrace();
        }
    }
}