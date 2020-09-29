package com.praktikum.spapp.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.praktikum.spapp.R;

public class RegisterWithInviteActivity extends AppCompatActivity implements View.OnClickListener{

    EditText textFieldEnterSurname;
    EditText textFieldEnterForename;
    EditText textFieldEnterPassword;
    EditText textFieldEnterUsername;

    Button sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        textFieldEnterForename = (EditText) findViewById(R.id.forename);
        textFieldEnterSurname = (EditText) findViewById(R.id.surname);
        textFieldEnterPassword = (EditText)findViewById(R.id.password);
        textFieldEnterUsername = (EditText)findViewById(R.id.username);
        sendData = (Button) findViewById(R.id.buttonSendData);

    }


    @Override
    public void onClick(View v) {



    }
}
