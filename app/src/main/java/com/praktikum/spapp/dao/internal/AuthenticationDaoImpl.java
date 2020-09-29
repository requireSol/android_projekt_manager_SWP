package com.praktikum.spapp.dao.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.dao.AuthenticationDao;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationDaoImpl extends AbstractDaoImpl implements AuthenticationDao {

    /**
     * The logon method
     *
     * @param nameOrEmail
     * @param password
     * @return A session with session data
     * @throws ResponseException
     */
    public Session logon(String nameOrEmail, String password) throws ResponseException {
        Session session = null;

        // create jsonString GSON by map
        Map<String, String> map = new HashMap<>();
        // user can enter username or email
        if (Utils.isEmail(nameOrEmail)) {
            map.put("email", nameOrEmail);
        } else {
            map.put("username", nameOrEmail);
        }
        map.put("password", password);
        Gson gson = new GsonBuilder().create();
        String data = gson.toJson(map);

        // create request body
        RequestBody requestBody = RequestBody.create(data, JSON);
        // make request with data
        Request request = new Request.Builder()
                .url(api + "/api/auth/signin")
                .post(requestBody)
                .build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            // have to create variable, because stream is closed or so
            String responseString = response.body().string();
            responseCheck(responseString);
            session = new Gson().fromJson(responseString, Session.class);
            session.setCurrentUsername(nameOrEmail);
            return session;
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

}

