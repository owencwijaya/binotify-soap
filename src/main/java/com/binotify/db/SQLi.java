package com.binotify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import jakarta.servlet.http.HttpServletRequest;


public class SQLi {
    private Connection conn = null;

    static final String DB_URL = "jdbc:mysql://host.docker.internal:3312/binotify-soap?";
    static final String USER = "root";
    static final String PASS = "root";

    private SQLi(){
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
        private static final SQLi Instance = new SQLi();
    }

    public static Connection getConn() {
        return Singleton.Instance.conn;
    }

    public static SQLi getInstance(){
        return Singleton.Instance;
    }

    public void insertLog(HttpServletRequest req) throws Exception{
        try{
            String endpoint = req.getRequestURL().toString();

            String desc = req.getMethod() + " " + endpoint + ", User-Agent:" + req.getHeader("user-agent") + ", Host:" + req.getHeader("host");
            String ip_address = req.getRemoteAddr();
            Timestamp requested_at = new Timestamp(System.currentTimeMillis());

            String query = "INSERT INTO `logging` (`desc`, `ip_address`, `endpoint`, `requested_at`) VALUES (?, ?, ?, ?)";


            PreparedStatement statement = this.conn.prepareStatement(query);
            statement.setString(1, desc);
            statement.setString(2, ip_address);
            statement.setString(3, endpoint);
            statement.setTimestamp(4, requested_at);
            
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }


}