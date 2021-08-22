package com.spring.domain.user;

import com.spring.domain.connection.ConnectionMaker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface UserDao {

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}