package com.spring.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String id;
    String name;
    String password;
    String email;
    Level level;
    int login;
    int recommend;

    public User() {}

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + ": x");
        }
        else {
            this.level = nextLevel;
        }
    }
}
