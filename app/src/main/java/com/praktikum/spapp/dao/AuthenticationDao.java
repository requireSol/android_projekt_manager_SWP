package com.praktikum.spapp.dao;

import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;

public interface AuthenticationDao {


    Session logon(final String username, final String password) throws ResponseException;

}
