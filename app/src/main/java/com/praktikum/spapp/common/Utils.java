package com.praktikum.spapp.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.service.internal.AuthenticationServiceImpl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    // parse a string to check if the response is a json with a 'success' key and which value it has
//    public static boolean silentTokenRefresh(String responseString) throws Exception {
//
//        // create a parser instance
//        JsonParser parser = new JsonParser();
//        // string to jsonelement, then
//        JsonElement element = parser.parse(responseString);
//        System.out.println(responseString);
//
//        // jsonelement to jsonobject
//        JsonObject resultAsJsonObject = element.getAsJsonObject();
//        // finally jsonobject can use .get method and check success
//
//        try {
//            String status = resultAsJsonObject.get("status").getAsString();
//            switch (status) {
//
//                case "401":
//                    AuthenticationServiceImpl.loginOnServer(AuthenticationServiceImpl.getSession().getCurrentUser().getUsername(), AuthenticationServiceImpl.getSession().getPassword());
//                    return true;
//
//                case "403":
//            }
//        } catch (NullPointerException | IOException e) {
//            //do nothing
//        }
//        String isSuccess = resultAsJsonObject.get("success").getAsString();
//
//        return false;
//    }

    public static String parseForJsonObject(String responseString, String jsonObject) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(responseString);
        JsonObject resultAsJsonObject = element.getAsJsonObject();
        JsonElement bElement = resultAsJsonObject.get(jsonObject);
        if (bElement.isJsonArray()) {
            return bElement.toString();
        } else {
            try {
                return bElement.getAsString();
            } catch (UnsupportedOperationException e) {
                return bElement.toString();
            }
        }
    }

    public static boolean isSuccess(String responseString) throws ResponseException {

        try {
            String isSuccess = parseForJsonObject(responseString, "success");
            if (isSuccess.contains("1")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (parseForJsonObject(responseString, "status").contains("403")) {
                throw new ResponseException("Your are not authorized for this.");
            }
            return false;
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isEmail(String string) {
        return Utils.validate(string);
    }

}
