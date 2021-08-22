package com.spring.domain.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class A_ConnectionMaker implements ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:url", "userName", "userPassword");

        return c;
    }
}
