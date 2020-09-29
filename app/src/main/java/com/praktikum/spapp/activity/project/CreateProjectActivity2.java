package com.praktikum.spapp.activity.project;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.internal.ProjectServiceImpl;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.User;

import java.util.ArrayList;


public class CreateProjectActivity2 extends AppCompatActivity implements View.OnClickListener {

    EditText projectName, projectDesc;
    String givenPN, givenPD;
    Button confirm, cancel, createFullProject;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project2);


        //assign xml elems
        projectName = findViewById(R.id.inputProjectName);
        projectDesc = findViewById(R.id.inputProjectDescription);
        confirm = findViewById(R.id.createProject2_button_confirm);
        cancel = findViewById(R.id.createProject2_button_cancel);
        createFullProject = findViewById(R.id.createProject_buttonCreateExtendedProject);


        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        createFullProject.setOnClickListener(this);
    }

    public void onClick(View view) {
        ArrayList<User> handlerList = new ArrayList<>();
        givenPN = projectName.getText().toString();
        givenPD = projectDesc.getText().toString();


        Project project = new Project();
        project.setName(givenPN);
        project.setDescription(givenPD);


        switch (view.getId()) {
            case R.id.createProject2_button_confirm:
                ProjectServiceImpl projectServiceImpl = new ProjectServiceImpl(SessionManager.getSession());
                new Thread(() -> {
                    try {
                        projectServiceImpl.createProject(project);
                        runOnUiThread(() -> Snackbar.make(view, "Your project has been successfully created!", Snackbar.LENGTH_LONG).show());
                        mHandler.postDelayed(mUpdateTimeTask, 3000);
                    } catch (ResponseException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show());
                    }
                }).start();


                break;
            case R.id.createProject2_button_cancel:
                //return to the createProject window
                projectName.setText("");
                projectDesc.setText("");
                break;
            case R.id.createProject_buttonCreateExtendedProject:
                runOnUiThread(() -> {
                    startActivity(new Intent(this, CreateProjectActivity.class));
                });
        }

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            startActivity(new Intent(getApplicationContext(), OpenAllProjectsActivity.class));
        }
    };
}
