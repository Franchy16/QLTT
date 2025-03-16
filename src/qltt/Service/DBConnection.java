/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.Service;
import java.sql.*;
/**
 *
 * @author admin
 */
public class DBConnection {
    public static Connection createConnection() throws SQLException {      
        System.out.println("Creating SQL Server DataBase Connection");
        Connection connection = null; 
        try {
            String Connectionurl="jdbc:sqlserver://localhost:1433;DatabaseName=QLTT;user=sa;password=Hoang22aA@;encrypt=true;trustServerCertificate=true";
            connection = DriverManager.getConnection(Connectionurl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (connection != null) {
            System.out.println("Connection created successfully..");
        }
        return connection;
    }
    
    public static void main(String[] args) throws SQLException {
        Connection connection = null; 
        try {
            connection = createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
