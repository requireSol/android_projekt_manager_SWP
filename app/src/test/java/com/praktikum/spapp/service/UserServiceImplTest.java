package com.praktikum.spapp.service;

import com.google.gson.JsonObject;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.InviteForm;
import com.praktikum.spapp.model.RegisterForm;
import com.praktikum.spapp.model.User;
import com.praktikum.spapp.model.enums.Role;
import com.praktikum.spapp.service.internal.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserServiceImplTest extends AbstractTestBundle {

    UserService adminService = new UserServiceImpl(adminSession);

    @Test
    public void testFetchAllUsers() throws ResponseException {
        ArrayList<User> adminsList = adminService.fetchAll();
        assertTrue(adminsList.size() == 7);
    }

    @Test
    @Disabled
    public void testEditUser() throws ResponseException {
        String newName = "Gorgeous Cow";
        String dbModEmail = "test_mod@email.com";

        JsonObject data = new JsonObject();
        data.addProperty("username", newName);
        data.addProperty("userToEditByEmail", dbModEmail);
        adminService.editUser(data);

        User result = adminService.getUserByEmail(dbModEmail);

        assertEquals(newName, result.getUsername());

        // cleanup
        data = new JsonObject();
        data.addProperty("username", "t_mod");
        data.addProperty("userToEditByEmail", dbModEmail);

        adminService.editUser(data);
        result = adminService.getUserByEmail(dbModEmail);

        assertEquals("t_mod", result.getUsername());
    }

    @Test
    public void testInviteUser() throws ResponseException {
        InviteForm form = new InviteForm();
        form.setEmail("haha@ausgedachte.email");
        /* Do not set this. If no Processor is set then the new User will be a Processor */
//        form.setProjectRights(InviteForm.projectRights.processor);
        /* Do not set this. If no ID is set then the new User will be in no Project */
//        form.setProjectId(1L);
        form.setRole(Role.ROLE_USER);
        String link = adminService.inviteUser(form);
        assertNotNull(link);
        assertTrue(link.length() > 0);
        System.out.println(link);
    }

    @Test
    public void testAcceptInvite() throws ResponseException {
        int before = adminService.fetchAll().size();
        // ramp up
        InviteForm iForm = new InviteForm();
        iForm.setEmail("haha@ausgedachte.email");
        iForm.setRole(Role.ROLE_USER);
        String link = adminService.inviteUser(iForm);

        RegisterForm rForm = new RegisterForm();
        rForm.setUsername("HomoDeus");
        rForm.setForename("Yuval");
        rForm.setSurname("Harrari");
        rForm.setStudentNumber(123456L);
        rForm.setPassword("21Lessons");
        rForm.setCourseOfStudy("Sith Marauder Academy");
        rForm.setExaminationRegulations("The Rule of Two");

        UserService sessionlessService = new UserServiceImpl();
        sessionlessService.acceptInvite(rForm, link);

        assertEquals(before + 1, adminService.fetchAll().size());
        assertNotNull(adminService.getUserByUsername("HomoDeus"));

        // clean up
        adminService.deleteUserByEmailHard("haha@ausgedachte.email");
    }

}
