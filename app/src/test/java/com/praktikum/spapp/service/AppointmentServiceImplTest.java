package com.praktikum.spapp.service;

import com.google.gson.JsonObject;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Appointment;
import com.praktikum.spapp.service.internal.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

public class AppointmentServiceImplTest extends AbstractTestBundle {
    /* The project id. */
    private final Long PROJECT_ID = 1L;
    /* The appointment id. */
    private final Long APPOINTEMENT_ID = 1L;

    AppointmentService service = new AppointmentServiceImpl(SessionManager.getSession());

    @Test
    public void testGetAppointments() throws ResponseException {
        ArrayList<Appointment> list = service.fetchAppointments(PROJECT_ID);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testUpdateAppointment() throws ResponseException {
        JsonObject data = new JsonObject();
        data.addProperty("name", "Ein Treffen");
        data.addProperty("description", "Nichts besonderes");
        service.updateAppointment(data, APPOINTEMENT_ID);

        ArrayList<Appointment> list = service.fetchAppointments(PROJECT_ID);
        Appointment anAppointment = list.stream()
                .filter(s -> s.getId().equals(APPOINTEMENT_ID))
                .findFirst()
                .orElse(null);
        assertTrue(anAppointment.getName().contains("Ein Treffen"));

        // clean up
        data = new JsonObject();
        data.addProperty("name", "In3Days");
        data.addProperty("description", "j");
    }

    @Test
    public void testCreateDeleteAppointment() throws ResponseException {
        int before = service.fetchAppointments(PROJECT_ID).size();

        Appointment anAppointment = service.fetchAppointments(PROJECT_ID).get(0);
        anAppointment.setId(null);
        Appointment bAppointment = service.createAppointment(anAppointment, PROJECT_ID);
        assertEquals(before + 1, service.fetchAppointments(PROJECT_ID).size());

        service.deleteAppointment(bAppointment.getId());
        assertEquals(before, service.fetchAppointments(PROJECT_ID).size());
    }

    @Test
    public void testCreateThrowsException() throws ResponseException {
        Appointment anAppointment = service.fetchAppointments(PROJECT_ID).get(0);
        anAppointment.setId(null);
        anAppointment.setStartDate(null);

    }
}
