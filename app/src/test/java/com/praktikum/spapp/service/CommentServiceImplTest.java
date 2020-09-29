package com.praktikum.spapp.service;

import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Comment;
import com.praktikum.spapp.service.internal.CommentServiceImpl;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceImplTest extends AbstractTestBundle {

    /* The project id */
    final Long projectId = 1L;

    CommentService adminService = new CommentServiceImpl(adminSession);
    CommentService usersService = new CommentServiceImpl(userSession);

    @Test
    public void testGetAllProjectComments() throws ResponseException {
        ArrayList<Comment> list = adminService.getAllComments(1L);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testUpdateThrowsExceptionComment() throws ResponseException {
        int restricted = adminService.getRestrictedComments(projectId).size();
        Long commentId = adminService.getAllComments(projectId).get(1).getId();
        assertThrows(ResponseException.class, () -> usersService.updateComment(commentId, true, "This message won't happen!"));
    }

    @Test
    public void testCreateDeleteComment() throws ResponseException {
        int before = adminService.getAllComments(projectId).size();
        Comment aComment = adminService.createComment(projectId, false, "Ein TestComment!");

        assertEquals(before + 1, adminService.getAllComments(projectId).size());

        adminService.deleteComment(aComment.getId());

        assertEquals(before, adminService.getAllComments(projectId).size());

    }

    @Test
    public void testGetPublicComments() throws ResponseException {
        int before = adminService.getPublicComments(projectId).size();
        Comment aComment = adminService.createComment(projectId, false, "Ein PUBLIC TestComment!");
        assertEquals(before + 1, adminService.getPublicComments(projectId).size());
        // clean up
        adminService.deleteComment(aComment.getId());
    }

    @Test
    public void getRestrictedComments() throws ResponseException {
        int before = adminService.getRestrictedComments(projectId).size();
        Comment aComment = adminService.createComment(projectId, true, "Ein RESTRICTED TestComment!");
        assertEquals(before + 1, adminService.getRestrictedComments(projectId).size());
        // clean up
        adminService.deleteComment(aComment.getId());
    }

}
