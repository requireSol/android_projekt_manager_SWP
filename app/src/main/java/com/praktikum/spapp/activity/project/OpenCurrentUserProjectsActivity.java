package com.praktikum.spapp.activity.project;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.adapters.RecyclerViewAdapterProject;
import com.praktikum.spapp.service.ProjectService;
import com.praktikum.spapp.service.internal.ProjectServiceImpl;

import java.util.ArrayList;

public class OpenCurrentUserProjectsActivity extends AppCompatActivity {
    ArrayList<Project> projectArrayList;
    ProjectService service = new ProjectServiceImpl(SessionManager.getSession());
    CardView noProjectsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_all_projects);
        noProjectsMessage = findViewById(R.id.cardViewNoProjects);

        new Thread(() -> {
            try {
                this.projectArrayList = service.fetchCurrentUserProjects();
                if (projectArrayList.isEmpty()) {
                    runOnUiThread(() -> noProjectsMessage.setVisibility(View.VISIBLE));
                } else {
                    runOnUiThread(this::initRecyclerView);
                }
                runOnUiThread(this::initRecyclerView);
            } catch (ResponseException e) {
                runOnUiThread(Snackbar.make(findViewById(R.id.welcome), e.getMessage() + " Please return to the previous page.", Snackbar.LENGTH_LONG)::show);
            }
        }).start();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.project_recycler_view);
        RecyclerViewAdapterProject adapter = new RecyclerViewAdapterProject(projectArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
