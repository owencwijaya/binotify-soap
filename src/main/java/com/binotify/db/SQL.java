package com.binotify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQL {
    private Connection conn = null;

    static final String DB_URL = "jdbc:mysql://host.docker.internal:3312/binotify-soap?";
    static final String USER = "root";
    static final String PASS = "root";

    private SQL(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.conn = DriverManager.getConnection(DB_URL + "user=root&password=root");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static class Singleton {
        private static final SQL Instance = new SQL();
    }

    public static Connection getConn() {
        return Singleton.Instance.conn;
    }
}