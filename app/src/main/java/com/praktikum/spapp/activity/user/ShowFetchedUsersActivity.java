package com.praktikum.spapp.activity.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.internal.UserServiceImpl;
import com.praktikum.spapp.model.User;
import com.praktikum.spapp.model.adapters.RecyclerViewAdapterUser;

import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;

public class ShowFetchedUsersActivity extends AppCompatActivity {
    ArrayList<User> userArrayList;
    RecyclerViewAdapterUser adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fetched_users);

        new Thread(() -> {
            try {
                UserServiceImpl userServiceImpl = new UserServiceImpl(SessionManager.getSession());
                this.userArrayList = userServiceImpl.fetchAll();
                runOnUiThread(this::initRecyclerView);
            } catch (ResponseException e) {
                runOnUiThread(Snackbar.make(findViewById(R.id.activity_show_fetched_users), e.getMessage() + " Please return to the previous page.", BaseTransientBottomBar.LENGTH_INDEFINITE)::show);
            }
        }).start();
    }


    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new RecyclerViewAdapterUser(userArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_filter_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}
