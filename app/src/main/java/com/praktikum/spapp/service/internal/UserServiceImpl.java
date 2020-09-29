package com.praktikum.spapp.service.internal;

import com.google.gson.*;
import com.praktikum.spapp.common.HttpClient;
import com.praktikum.spapp.dao.UserDao;
import com.praktikum.spapp.dao.internal.UserDaoImpl;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.*;

import com.praktikum.spapp.service.UserService;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class UserServiceImpl extends Service implements UserService {
    UserDao dao;

    public UserServiceImpl(Session session) {
        this.session = session;
        this.dao = new UserDaoImpl(session);
    }

    public UserServiceImpl() {
        this.dao = new UserDaoImpl();
    }

    @Override
    public String inviteUser(InviteForm form) throws ResponseException {
        return dao.inviteUser(form);
    }

    @Override
    public void acceptInvite(RegisterForm form, String invitationLinkUrl) throws ResponseException {
        dao.acceptInvite(form, invitationLinkUrl);
    }

    @Override
    public ArrayList<User> fetchAll() throws ResponseException {
        return dao.fetchAll();
    }

    @Override
    public void editUser(JsonObject data) throws ResponseException {
        dao.editUser(data);
    }

    @Override
    public String getUsernameByEmail(String email) throws ResponseException {
        return dao.getUsernameByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) throws ResponseException {
        return dao.getUserByEmail(email);
    }

    @Override
    public String getUserEmailByUsername(String username) throws ResponseException {
        return dao.getUserEmailByUsername(username);
    }

    @Override
    public User getUserByUsername(String username) throws ResponseException {
        return dao.getUserByUsername(username);
    }

    @Override
    public void deleteUserByEmailHard(String email) throws ResponseException {
        dao.deleteUserByEmailHard(email);
    }

    @Override
    public void deleteUserByEmail(String email) throws ResponseException {
        dao.deleteUserByEmail(email);
    }

    @Override
    public void deleteUserSelf() throws ResponseException {
        dao.deleteUserSelf();
    }
}
