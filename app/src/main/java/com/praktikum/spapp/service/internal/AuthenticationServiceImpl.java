package com.praktikum.spapp.service.internal;

import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.dao.AuthenticationDao;
import com.praktikum.spapp.dao.internal.AuthenticationDaoImpl;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.service.AuthenticationService;

public class AuthenticationServiceImpl extends Service implements AuthenticationService {

    AuthenticationDao dao;

    public AuthenticationServiceImpl() {
        dao = new AuthenticationDaoImpl();
    }

    public void logonServer(String nameOrEmail, String password) throws ResponseException {
        SessionManager.createSession(dao.logon(nameOrEmail, password));
    }
}
