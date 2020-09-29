package com.praktikum.spapp.dao.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.dao.CommentDao;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Comment;
import com.praktikum.spapp.model.Session;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

public class CommentDaoImpl extends AbstractDaoImpl implements CommentDao {

    public CommentDaoImpl(Session session) {
        this.session = session;
    }

    /**
     *
     * @param projectId
     * @param restricted
     * @param message
     * @return a new Comment with message content, project ID and restricted status
     * @throws ResponseException
     */

    @Override
    public Comment createComment(Long projectId, boolean restricted, String message) throws ResponseException {
        JsonObject data = new JsonObject();
        data.addProperty("restricted", restricted);
        data.addProperty("content", message);
        try {
            Response response = httpRequestMaker("/api/projects/" + projectId.toString() + "/comments", requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "comment");
            return new Gson().fromJson(resultString, new TypeToken<Comment>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param projectId
     * @return a list of all comments in a project
     * @throws ResponseException
     */
    @Override
    public ArrayList<Comment> getComments(Long projectId) throws ResponseException {
        try {
            Response response = httpRequestMaker("/api/projects/" + projectId.toString() + "/comments", requestTypes.GET);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "comments");
            return new Gson().fromJson(resultString, new TypeToken<ArrayList<Comment>>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param commentId
     * @param restricted
     * @param message
     * @return updated Comment
     * @throws ResponseException
     */
    @Override
    public Comment updateComment(Long commentId, boolean restricted, String message) throws ResponseException {
        JsonObject data = new JsonObject();
        data.addProperty("restricted", restricted);
        data.addProperty("content", message);
        try {
            Response response = httpRequestMaker("/api/comments/" + commentId.toString(), requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "comment");
            return new Gson().fromJson(resultString, new TypeToken<Comment>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param commentId
     * @throws ResponseException
     * @return comment is deleted from project
     */
    @Override
    public void deleteComment(Long commentId) throws ResponseException {
        try {
            Response response = httpRequestMaker("/api/comments/" + commentId.toString(), requestTypes.DELETE);
            String responseString = response.body().string();
            responseCheck(responseString);
        } catch (IOException e) {
            throw new ResponseException(e);
        }

    }
}
