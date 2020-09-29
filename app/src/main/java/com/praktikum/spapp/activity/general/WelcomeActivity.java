package com.praktikum.spapp.activity.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.project.OpenCurrentUserProjectsActivity;
import com.praktikum.spapp.activity.project.CreateProjectActivity2;
import com.praktikum.spapp.activity.project.OpenAllProjectsActivity;
import com.praktikum.spapp.activity.user.CheckForInviteActivity;
import com.praktikum.spapp.activity.user.InviteActivity;
import com.praktikum.spapp.activity.user.ShowFetchedUsersActivity;
import com.praktikum.spapp.activity.user.ShowUserDetailsActivity;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.model.User;
import com.praktikum.spapp.service.internal.UserServiceImpl;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Welcome Activity";

    // USER CORNER
    Button buttonViewProfile;
    Button buttonInviteUser;
    Button CheckForInvite;
    Button buttonShowAllUsers;
    //PROJECT CORNER
    Button buttonOpenProject;
    Button buttonCreateProject;
    Button buttonProjectDetails;
    Button buttonViewProjects;
    Button buttonAppointment;
    Context wcontext;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(wcontext, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        wcontext = WelcomeActivity.this;
        buttonViewProfile = findViewById(R.id.button_viewprofile);
        buttonViewProfile.setOnClickListener(this);

        buttonShowAllUsers = findViewById(R.id.button_show_all_user);
        buttonShowAllUsers.setOnClickListener(this);

        buttonCreateProject = findViewById(R.id.createFullProject_buttonCreate);
        buttonCreateProject.setOnClickListener(this);

        buttonInviteUser = findViewById(R.id.button_invite);
        buttonInviteUser.setOnClickListener(this);

        buttonOpenProject = findViewById(R.id.button_openproject);
        buttonOpenProject.setOnClickListener(this);

        buttonViewProjects = findViewById(R.id.button_view_current_user_projects);
        buttonViewProjects.setOnClickListener(this);

        CheckForInvite = findViewById(R.id.button_acceptInvite);
        CheckForInvite.setOnClickListener(this);


    }

    //    @Override
    public void onClick(View view) {
        //toast checking pw field is not null

        switch (view.getId()) {

            case R.id.button_viewprofile:
                startViewProfile();
                break;
            case R.id.createFullProject_buttonCreate:
                startActivityCreateProject(view);
                break;
            case R.id.button_invite:
                startActivityInvite();
                break;
            case R.id.button_openproject:
                startActivityOpenProject();
                break;
            case R.id.button_acceptInvite:
                startActivityCheckForInvite();
                break;
            case R.id.button_view_current_user_projects:
                startActivityCurrentUserProjects();
                break;
            case R.id.button_show_all_user:
                new Thread(() -> {
                    try {
                        startActivityShowAllUsers();
                    } catch (ResponseException e) {
                        Snackbar.make(view, "Your profile could not be loaded. Please try again later!", BaseTransientBottomBar.LENGTH_LONG);
                    }
                }).start();
        }
    }


    //     on click of toolbar item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
        return true;
    }

    // create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // start Activities
    public void startActivityCreateProject(View view) {
        Intent intent = new Intent(this, CreateProjectActivity2.class);
        startActivity(intent);
    }

    private void startViewProfile() {
        startActivity(new Intent(this, ShowFetchedUsersActivity.class));
    }

    private void startActivityCheckForInvite() {
        Intent intent = new Intent(this, CheckForInviteActivity.class);
        startActivity(intent);
    }

    private void startActivityOpenProject() {
        Intent intent = new Intent(this, OpenAllProjectsActivity.class);
        startActivity(intent);
    }

    private void startActivityInvite() {
        Intent intent = new Intent(this, InviteActivity.class);
        startActivity(intent);
    }

    private void startActivityCurrentUserProjects() {
        startActivity(new Intent(this, OpenCurrentUserProjectsActivity.class));
    }

    private void startActivityShowAllUsers() throws ResponseException {

        Intent intent = new Intent(this, ShowUserDetailsActivity.class);
        User curUser;
        String nameOrEmail = SessionManager.getSession().getCurrentUsername();
        if (Utils.isEmail(nameOrEmail)) {
            curUser = new UserServiceImpl(SessionManager.getSession()).getUserByEmail(nameOrEmail);
        } else {
            curUser = new UserServiceImpl(SessionManager.getSession()).getUserByUsername(nameOrEmail);
        }
        intent.putExtra("user", curUser);
        startActivity(intent);
    }


}

