package com.praktikum.spapp.activity.general;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.AuthenticationService;
import com.praktikum.spapp.service.internal.AuthenticationServiceImpl;
import com.praktikum.spapp.activity.user.CheckForInviteActivity;
import com.praktikum.spapp.common.Utils;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    // xml elements
    EditText etLoginName;
    EditText etLoginPassword;
    Button butLogin;
    TextView tvClickWithInvite;

    AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // assign xml to variables
        etLoginName = findViewById(R.id.userName);
        etLoginPassword = findViewById(R.id.password);
        butLogin = findViewById(R.id.login);
        tvClickWithInvite = findViewById(R.id.inviteClick);


        butLogin.setOnClickListener((View view) -> {
            new Thread(() -> {
                try {
                    authService = new AuthenticationServiceImpl();
                    authService.logonServer(etLoginName.getText().toString(), etLoginPassword.getText().toString());

                    //Activity will be shown next Intent will be changed
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);

                    if (SessionManager.getSession() != null) {

                        runOnUiThread(() -> {
                            //Intent will be started
                            startActivity(intent);
                            System.out.println(SessionManager.getSession().getTokenType());
                            System.out.println(SessionManager.getSession().getAccessToken());
                        });
                    } else {
                        runOnUiThread(() -> {
                            Snackbar.make(view, "Login failed :(", Snackbar.LENGTH_SHORT).show();
                        });
                    }

                } catch (ResponseException e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }).start();

        });

        tvClickWithInvite.setOnClickListener((View view) -> {
            startActivity(new Intent(LoginActivity.this, CheckForInviteActivity.class));
        });
    }
}



