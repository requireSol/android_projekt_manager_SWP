package com.praktikum.spapp.common;

import com.praktikum.spapp.model.Session;

public class SessionManager {

    private static Session session;

    public static void createSession(Session session) {
        SessionManager.session = session;
    }

    public static void deleteSession() {
        SessionManager.session = null;
    }

    public static Session getSession() {
        return session;
    }

}
