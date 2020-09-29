package com.praktikum.spapp.service.internal;

import com.google.gson.JsonObject;
import com.praktikum.spapp.dao.ProjectDao;
import com.praktikum.spapp.dao.internal.ProjectDaoImpl;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.ProjectService;

import java.util.ArrayList;

public class ProjectServiceImpl extends Service implements ProjectService {
    ProjectDao dao;

    public ProjectServiceImpl(Session session) {
        dao = new ProjectDaoImpl(session);
    }

    public Long createProject(Project project) throws ResponseException {
        return dao.createProject(project);
    }

    public Long createProjectFull(Project project) throws ResponseException {
        return dao.createProjectFull(project);
    }

    public void deleteProject(Long id) throws ResponseException {
        dao.deleteProject(id);
    }

    public ArrayList<Project> fetchAllProjects() throws ResponseException {
        return dao.fetchAllProjects();
    }

    public ArrayList<Project> fetchCurrentUserProjects() throws ResponseException {
        return dao.fetchCurrentUserProjects();
    }

    public void updateProject(Long id, JsonObject data) throws ResponseException {
        dao.updateProject(id, data);
    }
}
