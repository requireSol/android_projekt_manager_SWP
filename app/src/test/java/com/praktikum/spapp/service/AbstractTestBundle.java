package com.praktikum.spapp.service;

import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.internal.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTestBundle {

    /* The log in credentials of admin who has admin role*/
    static String USERNAME_ADMIN = "admin";
    static String PASSWORD_ADMIN = "password";
    /* The log in credentials of t_mod who has user role*/
    static String USERNAME_USER = "t_mod";
    static String PASSWORD_USER = "password_of_mod";
    /* The sessions required to inject the services with.
       Caution they use the static Session session field of  SessionManager */
    static Session adminSession;
    static Session userSession;

    @BeforeAll
    public static void auth() throws ResponseException {
        AuthenticationService adminAuth = new AuthenticationServiceImpl();
        adminAuth.logonServer(USERNAME_ADMIN, PASSWORD_ADMIN);
        adminSession = SessionManager.getSession();
    }

    @BeforeAll
    public static void auth2() throws ResponseException {
        AuthenticationService userAuth = new AuthenticationServiceImpl();
        userAuth.logonServer(USERNAME_USER, PASSWORD_USER);
        userSession = SessionManager.getSession();
    }
}
