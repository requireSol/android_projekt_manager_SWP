package com.praktikum.spapp.service.internal;


import com.praktikum.spapp.model.Session;

public abstract class Service {

    Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
