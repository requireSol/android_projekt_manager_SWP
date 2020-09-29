package com.praktikum.spapp.model;

import com.praktikum.spapp.model.enums.ProjectStatus;
import com.praktikum.spapp.model.enums.ProjectType;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    Long id;
    String name;
    String description;
    ArrayList<User> handlers;
    ArrayList<User> processors;
    ProjectType type;
    ProjectStatus projectStatus;
    ArrayList<Comment> comments;
    ArrayList<Appointment> appointments;

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

    public ProjectStatus getStatus() {
        return projectStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getHandlers() {
        return handlers;
    }

    public void setHandlers(ArrayList<User> handler) {
        this.handlers = handler;
    }

    public ArrayList<User> getProcessor() {
        return processors;
    }

    public void setProcessor(ArrayList<User> processor) {
        this.processors = processor;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

}


