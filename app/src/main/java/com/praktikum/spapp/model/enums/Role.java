package com.praktikum.spapp.model.enums;

import java.io.Serializable;

public enum Role implements Serializable {

    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String toString() {
        switch (this) {
            case ROLE_USER:
                return "ROLE_USER";
            case ROLE_ADMIN:
                return "ROLE_ADMIN";
            default:
                return "ROLE_USER";
        }
    }

    public String prettyPrint() {
        switch (this) {
            case ROLE_USER:
                return "User";
            case ROLE_ADMIN:
                return "Admin";
            default:
                return "User";
        }

    }
}


