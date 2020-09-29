package com.praktikum.spapp.activity.appointment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Appointment;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.adapters.RecyclerViewAdapterAppointment;
import com.praktikum.spapp.service.AppointmentService;
import com.praktikum.spapp.service.internal.AppointmentServiceImpl;

import java.util.ArrayList;

public class FragmentProjectAppointments extends Fragment {
    RecyclerViewAdapterAppointment adapter;
    ArrayList<Appointment> appointments;
    ArrayList<Appointment> newAppointments;
    View view;
    Button button_create_appointment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppointmentService appointmentService = new AppointmentServiceImpl(SessionManager.getSession());
        view = inflater.inflate(R.layout.fragment_project_appointments, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.appointment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        button_create_appointment = view.findViewById(R.id.button_create_appointment);
        Project project = (Project) getArguments().getSerializable("project");
        boolean changed = (boolean) getArguments().getSerializable("changed");
        boolean createdComment = (boolean) getArguments().getSerializable("createdComment");
        button_create_appointment.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CreateAppointmentActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        });


        new Thread(() -> {
            try {
                newAppointments = appointmentService.fetchAppointments(project.getId());
            } catch (ResponseException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(() -> {
                adapter = new RecyclerViewAdapterAppointment(newAppointments, view.getContext());
                recyclerView.setAdapter(adapter);
                if(changed) {
                    Snackbar.make(view, "Your changes have been saved.", Snackbar.LENGTH_LONG).show();
                }
            });
        }).start();



        return view;
    }

}
