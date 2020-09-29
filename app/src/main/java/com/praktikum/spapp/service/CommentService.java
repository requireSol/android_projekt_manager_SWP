package com.praktikum.spapp.service;

import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Comment;

import java.util.ArrayList;

public interface CommentService {
    Comment createComment(Long projectId, boolean restricted, String message) throws ResponseException;

    ArrayList<Comment> getAllComments(Long projectId) throws ResponseException;

    ArrayList<Comment> getPublicComments(Long projectId) throws ResponseException;

    ArrayList<Comment> getRestrictedComments(Long projectId) throws ResponseException;

    Comment updateComment(Long commentId, boolean restricted, String message) throws ResponseException;

    void deleteComment(Long commentId) throws ResponseException;

}
