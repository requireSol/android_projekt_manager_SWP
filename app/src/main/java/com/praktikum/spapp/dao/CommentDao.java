package com.praktikum.spapp.dao;

import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Comment;

import java.util.ArrayList;

public interface CommentDao {

    Comment createComment(Long projectId, boolean restricted, String message) throws ResponseException;

    ArrayList<Comment> getComments(Long projectId) throws ResponseException;

    Comment updateComment(Long commentId, boolean restricted, String message) throws ResponseException;

    void deleteComment(Long commentId) throws ResponseException;
}
