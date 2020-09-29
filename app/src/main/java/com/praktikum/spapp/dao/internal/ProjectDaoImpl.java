package com.praktikum.spapp.dao.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.dao.ProjectDao;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.Session;

import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectDaoImpl extends AbstractDaoImpl implements ProjectDao {

    public ProjectDaoImpl(Session session) {
        this.session = session;
    }

    /**
     *
     * @param project
     * @return a Response String
     * @throws ResponseException
     */
    @Override
    public Long createProject(Project project) throws ResponseException {
        JsonObject data = (JsonObject) new JsonParser().parse(new Gson().toJson(project));
        try {
            Response response = httpRequestMaker("/api/project/init", requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            String result = Utils.parseForJsonObject(responseString.toString(), "projectId");
            return Long.parseLong(result);
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    @Override
    public Long createProjectFull(Project project) throws ResponseException {
        JsonObject data = (JsonObject) new JsonParser().parse(new Gson().toJson(project));
        try {
            Response response = httpRequestMaker("/api/project/initFull", requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            return Long.parseLong(Utils.parseForJsonObject(responseString, "projectId"));
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @return A list of All Projects
     * @throws ResponseException
     */
    @Override
    public ArrayList<Project> fetchAllProjects() throws ResponseException {
        try {
            Response response = this.httpRequestMaker("/api/project", requestTypes.GET);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "result");
            return new Gson().fromJson(resultString, new TypeToken<ArrayList<Project>>() {
            }.getType());

        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @return a list of project only available to user
     * @throws ResponseException
     */
    @Override
    public ArrayList<Project> fetchCurrentUserProjects() throws ResponseException {
        ArrayList<Project> list = null;
        try {
            Response response = this.httpRequestMaker("/api/project/", requestTypes.GET);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "result");
            return new Gson().fromJson(resultString, new TypeToken<ArrayList<Project>>() {
            }.getType());

        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param id project ID
     * @param data project contents to be updated
     * @throws ResponseException
     */
    @Override
    public void updateProject(Long id, JsonObject data) throws ResponseException {
        try {
            Response response = this.httpRequestMaker("/api/project/update/" + id.toString(), requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param id project ID to be deleted
     * @throws ResponseException
     */
    @Override
    public void deleteProject(Long id) throws ResponseException {
        try {
            Response response = this.httpRequestMaker("/api/project/delete/" + id.toString(), requestTypes.DELETE);
            String responseString = response.body().string();
            responseCheck(responseString);
        } catch (Exception e) {
            throw new ResponseException(e);
        }
    }
}
