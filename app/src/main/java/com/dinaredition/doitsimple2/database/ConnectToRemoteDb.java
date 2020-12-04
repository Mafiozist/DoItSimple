package com.dinaredition.doitsimple2.database;

import android.os.StrictMode;

import java.sql.*;

//Класс отвечающий за доступ к базе данных MySql через драйвер JDBC
public class ConnectToRemoteDb {
    private final static String USES_LIBRARY = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://109.195.75.138:3306/" + "task_manager";

    private final static String USER = "dinar";
    private final static String PASSWORD =  "dinar20001";

    public static Connection Connect(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionUrl = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
           // connection = DriverManager.getConnection(URL);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
