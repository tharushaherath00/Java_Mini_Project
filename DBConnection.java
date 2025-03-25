package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/java";
    private static final String USER = "root";
    private static final String PASS = "Nilakshana_123@";

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL,USER,PASS);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}
