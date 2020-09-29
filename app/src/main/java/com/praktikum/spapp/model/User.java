package com.praktikum.spapp.model;

import com.praktikum.spapp.model.enums.Role;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private int id;
    private String username;
    private String email;
    private ArrayList<Role> roles;
    private UserInfo userInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}

