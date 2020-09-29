package com.praktikum.spapp.dao;

import com.google.gson.JsonObject;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.InviteForm;
import com.praktikum.spapp.model.RegisterForm;
import com.praktikum.spapp.model.User;

import java.util.ArrayList;

public interface UserDao {

    String inviteUser(InviteForm form) throws ResponseException;

    void acceptInvite(RegisterForm form, String invitationLinkUrl) throws ResponseException;

    ArrayList<User> fetchAll() throws ResponseException;

    void editUser(JsonObject data) throws ResponseException;

    String getUsernameByEmail(String email) throws ResponseException;

    User getUserByEmail(String email) throws ResponseException;

    String getUserEmailByUsername(String username) throws ResponseException;

    User getUserByUsername(String username) throws ResponseException;

    void deleteUserByEmailHard(String email) throws ResponseException;

    void deleteUserByEmail(String email) throws ResponseException;

    void deleteUserSelf() throws ResponseException;



    





}
