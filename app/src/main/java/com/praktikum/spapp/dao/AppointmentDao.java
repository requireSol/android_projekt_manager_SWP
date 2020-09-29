package com.praktikum.spapp.dao;

import com.google.gson.JsonObject;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Appointment;

import java.util.ArrayList;

public interface AppointmentDao {

    Appointment createAppointment(Appointment appointment, Long projectId) throws ResponseException;

    ArrayList<Appointment> fetchAppointments(Long projectId) throws ResponseException;

    Appointment updateAppointment(JsonObject data, Long projectId) throws ResponseException;

    void deleteAppointment(Long projectId) throws ResponseException;

}
