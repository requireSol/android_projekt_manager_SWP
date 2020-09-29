package com.praktikum.spapp.model.enums;

public enum AppointmentType {
    DEADLINE,
    MEETING,
    GROUP,
    TEMPLATE;

    @Override
    public String toString() {
        switch (this)
        {
            case DEADLINE:
                return "Deadline";
            case MEETING:
                return "Meeting";
            case GROUP:
                return "Group";
            case TEMPLATE:
                return "Template";
            default:
                return "Asshole";
        }
    }
}
