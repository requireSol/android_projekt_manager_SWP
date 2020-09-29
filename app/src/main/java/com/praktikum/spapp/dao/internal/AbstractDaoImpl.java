package com.praktikum.spapp.dao.internal;

import com.google.gson.JsonObject;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.model.User;
import okhttp3.*;

import java.io.IOException;


/**
 * The abstract dao implementation class.
 */
public abstract class AbstractDaoImpl {
    protected Session session;
    protected static final OkHttpClient client = new OkHttpClient();
    protected static final String api = "http://api.solaimani.de:8081";
    //protected static final String api = "http://192.168.1.160:8081";
    // need this for okhttp
    protected static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");


    /**
     * Creates a Request and returns a Response.
     *
     * @param path the URI needs to be extended with String concatenation
     * @param type inner enum class type
     * @param json the Gson JsonObject
     * @return a response object
     * @throws IOException if the request fails
     */
    public Response httpRequestMaker(String path, requestTypes type, JsonObject json) throws IOException {

        RequestBody body = RequestBody.create(json.toString(), JSON);

        switch (type) {
            case PUT:
                Request request = new Request.Builder()
                        .url(api + path)
                        .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                        .put(body)
                        .build();
                return client.newCall(request).execute();

            case POST:
                request = new Request.Builder()
                        .url(api + path)
                        .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                        .post(body)
                        .build();
                return client.newCall(request).execute();

            case GET:
                request = new Request.Builder()
                        .url(api + path)
                        .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                        .get()
                        .build();
                return client.newCall(request).execute();

            case DELETE:
                request = new Request.Builder()
                        .url(api + path)
                        .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                        .delete(body)
                        .build();
                return client.newCall(request).execute();
        }
        throw new IOException("Something went wrong :(");
    }


    public Response httpRequestMaker(String path, requestTypes type) throws IOException {
        return httpRequestMaker(path, type, new JsonObject());
    }

    /**
     * If the request is successful (e.g. status 200) the returned json still might be an error. In this case the error message hopefully is extracted
     * and thrown within a new ResponseException.
     *
     * @param responseString
     * @throws ResponseException
     */
    public static void responseCheck(String responseString) throws ResponseException {
        if (!Utils.isSuccess(responseString.toLowerCase())) {
            throw new ResponseException(Utils.parseForJsonObject(responseString, "Error"));
        }
    }

    /**
     * The http request types
     */
    enum requestTypes {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected static User createDummyUser() {
        return null;
    }
}
