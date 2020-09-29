package com.praktikum.spapp.service;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.service.internal.ProjectServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectServiceImplTest extends AbstractTestBundle {

    /* Load services for different user roles. */
    ProjectService adminsService = new ProjectServiceImpl(adminSession);
    ProjectService usersService = new ProjectServiceImpl(userSession);

    @Test
    public void testFetchAllProjects() throws ResponseException {
        ArrayList<Project> list = adminsService.fetchAllProjects();
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFetchUserProjects() throws ResponseException {
        ArrayList<Project> list = usersService.fetchCurrentUserProjects();
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testCreateDeleteProject() throws ResponseException {
        int before = adminsService.fetchAllProjects().size();
        Project aProject = new Project();
        aProject.setName("TESTESTPROJECT");
        aProject.setDescription("This is a project description");
        Long aProjectId = adminsService.createProject(aProject);

        int after = adminsService.fetchAllProjects().size();

        assertEquals(before + 1, after);

        // cleanUp
        adminsService.deleteProject(aProjectId);
        assertEquals(before, adminsService.fetchAllProjects().size());
    }

    @Test
    public void testCreateFullProject() throws ResponseException {
        Project aProject = new Project();
        aProject.setName("FULLPROJECTCHIGGA");
        aProject.setDescription("A project for rich chiggas");
        //TODO
    }

    @Test
    public void testUpdateProject() throws ResponseException {

        Project aProject = adminsService.fetchAllProjects().get(0);
        final String DESC = "They see me rollin' the hatin'";
        final String handler = aProject.getHandlers().get(0).getEmail();
        JsonArray handlerArray = new JsonArray();
        handlerArray.add(handler);

        JsonObject data = new JsonObject();
        data.addProperty("description", DESC);
        data.add("handler", handlerArray);

        adminsService.updateProject(aProject.getId(), data);

        Project alteredAProject = adminsService.fetchAllProjects().get(0);
        assertTrue(alteredAProject.getDescription().contains(DESC));
        assertEquals(1, alteredAProject.getHandlers().size());

        // clean up
        data = new JsonObject();
        data.addProperty("description", aProject.getDescription());
        handlerArray = new JsonArray();
        JsonArray finalHandlerArray = handlerArray;
        aProject.getHandlers().stream().forEach(h -> finalHandlerArray.add(h.getEmail()));
        data.add("handler", finalHandlerArray);
        adminsService.updateProject(aProject.getId(), data);

    }


}
