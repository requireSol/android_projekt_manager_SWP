package com.praktikum.spapp.service;

import com.google.gson.JsonObject;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;

import java.util.ArrayList;

public interface ProjectService {

    Long createProject(Project project) throws ResponseException;

    Long createProjectFull(Project project) throws ResponseException;

    ArrayList<Project> fetchAllProjects() throws ResponseException;

    ArrayList<Project> fetchCurrentUserProjects() throws ResponseException;

    void updateProject(Long id, JsonObject data) throws ResponseException;

    void deleteProject(Long id) throws ResponseException;

}
