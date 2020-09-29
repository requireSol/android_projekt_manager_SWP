package com.praktikum.spapp.exception;

/**
 * Throw this exception when a http request fails. Encapsulate other thrown exceptions with the provided constructors.
 */

public class ResponseException extends Exception {

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Exception exception) {
        super(exception);
    }

}
