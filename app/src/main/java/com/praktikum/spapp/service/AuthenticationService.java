package com.praktikum.spapp.service;

import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;

public interface AuthenticationService {

    void logonServer(String nameOrEmail, String password) throws ResponseException;
    
}
