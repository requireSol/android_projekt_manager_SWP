package com.praktikum.spapp.model;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private int id;

    private String forename;

    private String surname;

    private int studentNumber;

    private String courseOfStudy;

    private String examinationRegulations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourseOfStudy() {
        return courseOfStudy;
    }

    public void setCourseOfStudy(String courseOfStudy) {
        this.courseOfStudy = courseOfStudy;
    }

    public String getExaminationRegulations() {
        return examinationRegulations;
    }

    public void setExaminationRegulations(String examinationRegulations) {
        this.examinationRegulations = examinationRegulations;
    }
}
