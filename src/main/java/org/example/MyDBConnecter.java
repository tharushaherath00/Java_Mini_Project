package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDBConnecter {
    String driver="com.mysql.cj.jdbc.Driver";
    String url="jdbc:mysql://localhost:3306/tec_lms";
    String user="root";
    String password = "root";

    private Connection myCon = null;
    private void driverRegistering(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Successfully registered...");
        } catch (ClassNotFoundException e) {
            System.out.println("Error in registering the drive class..."+ e.getMessage());
        }
    }

    public Connection getMyConnection(){
        driverRegistering();

        try {
            myCon = DriverManager.getConnection(url,user,password);
            System.out.println("Successfully in creating the connection...");
        } catch (SQLException e) {
            System.out.println("Error in getting connection..." + e.getMessage());
        }
        return myCon;
    }
}


