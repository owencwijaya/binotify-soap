package com.binotify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    static final String DB_URL = "jdbc:mysql://host.docker.internal:3312/binotify-soap?";
    static final String USER = "root";
    static final String PASS = "root";

    public void Execute(String query) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e){
            throw e;
        }

        try{
            Connection conn = DriverManager.getConnection(DB_URL + "user=root&password=root");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw e;
        }
    }
}