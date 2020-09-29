package com.praktikum.spapp.activity.project;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.comment.FragmentProjectComments;
import com.praktikum.spapp.activity.appointment.FragmentProjectAppointments;

import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.service.CommentService;
import com.praktikum.spapp.service.ProjectService;
import com.praktikum.spapp.service.internal.CommentServiceImpl;
import com.praktikum.spapp.service.internal.ProjectServiceImpl;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ProjectDetailActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    Button appointmentFragmentButton;
    boolean changed = false;
    boolean createdComment = false;
    ArrayList<Project> projectArrayList;
    ProjectService service = new ProjectServiceImpl(SessionManager.getSession());
    CommentService commentService = new CommentServiceImpl(SessionManager.getSession());
    Project project;
    Fragment beginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);


        new Thread(() -> {
            beginActivity = new FragmentProjectOverview();
            project = (Project) getIntent().getSerializableExtra("project");
            BottomNavigationView botNav = findViewById(R.id.bottom_navigation);

            botNav.setOnNavigationItemSelectedListener(navListener);
            bundle.putSerializable("project", project);

            beginActivity.setArguments(bundle);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        beginActivity).commit();
            }

        }).start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {

            case R.id.nav_project_overview:
                selectedFragment = new FragmentProjectOverview();
                bundle.putSerializable("createdComment", false);
                bundle.putSerializable("changed", false);
                selectedFragment.setArguments(bundle);
                break;
            case R.id.nav_project_comments:
                selectedFragment = new FragmentProjectComments();
                bundle.putSerializable("createdComment", false);
                bundle.putSerializable("changed", false);
                selectedFragment.setArguments(bundle);
                break;
            case R.id.nav_project_appointments:
                selectedFragment = new FragmentProjectAppointments();
                bundle.putSerializable("createdComment", false);
                bundle.putSerializable("changed", false);
                selectedFragment.setArguments(bundle);
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };

}
