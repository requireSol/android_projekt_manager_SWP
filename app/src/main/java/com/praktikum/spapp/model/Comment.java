package com.praktikum.spapp.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Comment implements Serializable, Comparable<Comment> {

    private Long id;
    private String content;
    private boolean restricted;
    private boolean wasEdited;
    private String creationTime;
    private User author;
    private Date ctAsDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCtAsDate() {
        return ctAsDate;
    }

    public void setCtAsDate() {
        DateTime dateTime = new DateTime(getCreationTime());
        this.ctAsDate = dateTime.toDate();
    }

    @Override
    public int compareTo(Comment o) {
        return this.getCtAsDate().compareTo(o.getCtAsDate());
    }
}
