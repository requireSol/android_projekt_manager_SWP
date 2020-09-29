package com.praktikum.spapp.model;

import com.praktikum.spapp.model.enums.AppointmentType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Appointment implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private AppointmentType type;
    private ArrayList<Reminder> reminders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void prettyPrint(){}

}
