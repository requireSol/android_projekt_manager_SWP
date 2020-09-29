package com.praktikum.spapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Reminder implements Serializable {

    private int id;
    private User creator;
    private String remindTime;
    private ArrayList<User> reminderSubjects;

    public Reminder(int id, User creator, String remindTime, ArrayList<User> reminderSubjects) {
        this.id = id;
        this.creator = creator;
        this.remindTime = remindTime;
        this.reminderSubjects = reminderSubjects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public ArrayList<User> getReminderSubjects() {
        return reminderSubjects;
    }

    public void setReminderSubjects(ArrayList<User> reminderSubjects) {
        this.reminderSubjects = reminderSubjects;
    }
}
