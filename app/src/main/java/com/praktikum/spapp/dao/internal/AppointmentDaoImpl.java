package com.praktikum.spapp.dao.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.dao.AppointmentDao;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Appointment;
import com.praktikum.spapp.model.Session;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

public class AppointmentDaoImpl extends AbstractDaoImpl implements AppointmentDao {

    public AppointmentDaoImpl(Session session) {
        this.session = session;
    }

    /**
     *
     * @param appointment
     * @param projectId
     * @return new Appointment in project
     * @throws ResponseException
     */
    @Override
    public Appointment createAppointment(Appointment appointment, Long projectId) throws ResponseException {
        JsonObject data = (JsonObject) new JsonParser().parse(new Gson().toJson(appointment));
        try {
            Response response = httpRequestMaker("/api/projects/" + projectId.toString() + "/appointments", requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "appointment");
            return new Gson().fromJson(resultString, new TypeToken<Appointment>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     * get all available Appointment
     * @param projectId
     * @return
     * @throws ResponseException
     */
    @Override
    public ArrayList<Appointment> fetchAppointments(Long projectId) throws ResponseException {
        try {
            Response response = httpRequestMaker("/api/projects/" + projectId.toString() + "/appointments", requestTypes.GET);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "appointments");
            return new Gson().fromJson(resultString, new TypeToken<ArrayList<Appointment>>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     *
     * @param data
     * @param appointmentId
     * @return Appointment with updated content and matching appointmentID
     * @throws ResponseException
     */
    @Override
    public Appointment updateAppointment(JsonObject data, Long appointmentId) throws ResponseException {
        try {
            Response response = httpRequestMaker("/api/appointments/" + appointmentId.toString(), requestTypes.POST, data);
            String responseString = response.body().string();
            responseCheck(responseString);
            String resultString = Utils.parseForJsonObject(responseString, "appointment");
            return new Gson().fromJson(resultString, new TypeToken<Appointment>() {
            }.getType());
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    /**
     * Delete existing appointment
     * @param appointmentId
     * @throws ResponseException
     */
    @Override
    public void deleteAppointment(Long appointmentId) throws ResponseException {
        try {
            Response response = httpRequestMaker("/api/appointments/" + appointmentId.toString(), requestTypes.DELETE);
            String responseString = response.body().string();
            responseCheck(responseString);
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }
}
