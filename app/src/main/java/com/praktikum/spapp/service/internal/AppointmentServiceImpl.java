package com.praktikum.spapp.service.internal;

import com.google.gson.JsonObject;
import com.praktikum.spapp.dao.AppointmentDao;
import com.praktikum.spapp.dao.internal.AppointmentDaoImpl;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Appointment;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.AppointmentService;

import java.util.ArrayList;


public class AppointmentServiceImpl extends Service implements AppointmentService {
    AppointmentDao dao;

    public AppointmentServiceImpl(Session session) {
        this.session = session;
        this.dao = new AppointmentDaoImpl(session);
    }

    @Override
    public Appointment createAppointment(Appointment appointment, Long projectId) throws ResponseException {
        return dao.createAppointment(appointment, projectId);
    }

    @Override
    public ArrayList<Appointment> fetchAppointments(Long projectId) throws ResponseException {
        return dao.fetchAppointments(projectId);
    }

    @Override
    public void updateAppointment(JsonObject data, Long appointmentId) throws ResponseException {
        dao.updateAppointment(data, appointmentId);
    }

    @Override
    public void deleteAppointment(Long appointmentId) throws ResponseException {
        dao.deleteAppointment(appointmentId);
    }

    @Override
    public void toCalendar(Appointment appointment) {
        // just add to calendar LULW
    }

}
