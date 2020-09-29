package com.praktikum.spapp.model;

public class RegisterForm {

    private String forename;
    private String surname;
    private Long studentNumber;
    private String password;
    private String username;
    private String courseOfStudy;
    private String examinationRegulations;

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

    public Long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
