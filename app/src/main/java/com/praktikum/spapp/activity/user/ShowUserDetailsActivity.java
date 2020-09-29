package com.praktikum.spapp.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Session;
import com.praktikum.spapp.service.UserService;
import com.praktikum.spapp.service.internal.UserServiceImpl;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.model.User;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.parseInt;

public class ShowUserDetailsActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText vorname;
    private EditText nachname;
    private EditText matrikelnummer;
    private EditText studiengang;
    private EditText pruefungsordnung;

    UserService service = new UserServiceImpl(SessionManager.getSession());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_details);
        AtomicBoolean editMode = new AtomicBoolean(false);


        User user = (User) getIntent().getSerializableExtra("user");
//        user = service.getUserByUsername(user.getUsername());

        //set button
        Button buttonEaC = findViewById(R.id.button_edit_user_and_cancel);
        Button buttonEditSave = findViewById(R.id.button_edit_user_save);

        //set bind ETs and set values
        username = findViewById(R.id.et_username);
        username.setText(user.getUsername());

        email = findViewById(R.id.et_email);
        email.setText(user.getEmail());

        vorname = findViewById(R.id.et_first_name);
        vorname.setText(user.getUserInfo().getForename());

        nachname = findViewById(R.id.et_last_name);
        nachname.setText(user.getUserInfo().getSurname());

        matrikelnummer = findViewById(R.id.et_student_number);
        matrikelnummer.setText("" + user.getUserInfo().getStudentNumber());

        studiengang = findViewById(R.id.et_course);
        studiengang.setText(user.getUserInfo().getCourseOfStudy());

        pruefungsordnung = findViewById(R.id.et_ex_regulations);
        pruefungsordnung.setText(user.getUserInfo().getExaminationRegulations());

        // on clicklisteners
        buttonEaC.setOnClickListener((View view) -> {

            if (!editMode.get()) {
                editMode.set(true);
                username.setEnabled(true);
                email.setEnabled(true);
                vorname.setEnabled(true);
                nachname.setEnabled(true);
                matrikelnummer.setEnabled(true);

                buttonEaC.setText("Cancel");
                buttonEditSave.setVisibility(View.VISIBLE);


                buttonEditSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JsonObject data = new JsonObject();
                        data.addProperty("userToEditByEmail", email.getText().toString());
                        data.addProperty("email", email.getText().toString());
                        data.addProperty("username", username.getText().toString());
                        data.addProperty("forename", vorname.getText().toString());
                        data.addProperty("surname", nachname.getText().toString());
                        data.addProperty("studentNumber", parseInt(matrikelnummer.getText().toString()));
                        data.addProperty("courseOfStudy", studiengang.getText().toString());
                        data.addProperty("examinationRegulations", pruefungsordnung.getText().toString());

                        new Thread(() -> {
                            try {
                                service.editUser(data);

                                runOnUiThread(() -> {
                                    editMode.set(false);
                                    username.setEnabled(false);
                                    email.setEnabled(false);
                                    vorname.setEnabled(false);
                                    nachname.setEnabled(false);
                                    matrikelnummer.setEnabled(false);
                                    buttonEaC.setText("Edit");
                                    buttonEditSave.setVisibility(View.GONE);

                                    Snackbar.make(view, "Your changes have been saved.", Snackbar.LENGTH_LONG).show();
                                });
                            } catch (ResponseException e) {
                                runOnUiThread(() -> {
                                    Snackbar.make(view, "Whoops, something went wrong.", Snackbar.LENGTH_LONG).show();
                                });
                            }
                        }).
                                start();
                    }
                });
            } else {
                editMode.set(false);
                username.setEnabled(false);
                email.setEnabled(false);
                vorname.setEnabled(false);
                nachname.setEnabled(false);
                matrikelnummer.setEnabled(false);
                buttonEaC.setText("Edit");
                buttonEditSave.setVisibility(View.GONE);
            }
        });
    }
}
