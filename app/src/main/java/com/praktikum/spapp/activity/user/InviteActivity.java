package com.praktikum.spapp.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.praktikum.spapp.R;
import com.praktikum.spapp.model.InviteForm;
import com.praktikum.spapp.model.enums.Role;


public class InviteActivity extends AppCompatActivity implements View.OnClickListener {


    //XML elems
    private EditText etInputEmail;
    private EditText etInputProjectId;
    private CheckBox cbIsHandler;
    private CheckBox cbIsProcessor;
    private Switch switchIsAdmin;
    private Button confirm;
    private Button cancel;
    private Switch isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        //assign XML elems
        etInputEmail = (EditText) findViewById(R.id.inputUserMail);
        etInputProjectId = (EditText) findViewById(R.id.inputProjectNo);
        cbIsHandler = (CheckBox) findViewById(R.id.isHandler);
        cbIsProcessor = (CheckBox) findViewById(R.id.isProcessor);

        confirm = (Button) findViewById(R.id.buttonConfirm);
        cancel = (Button) findViewById(R.id.buttonCancel);
        isAdmin = (Switch) findViewById(R.id.isAdmin);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }


    public void onClick(View view) {


//        switch (view.getId()) {
//
//            case R.id.buttonConfirm:
//                new Thread(() -> {
//                    InviteForm inviteForm = createInviteForm();
//                    UserServiceImpl userServiceImpl = new UserServiceImpl();
//
//                    try {
//                        String responseString = userServiceImpl.addUserInvitation(inviteForm);
//                        System.out.println(responseString);
//
//
//                        if (Utils.isSuccess(responseString)) {
//                        startOpenMailView(view);
//
//                        } else {
//                        //TODO cancel activity
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
//
//
//            case R.id.buttonCancel:
//
//        }
    }

    public InviteForm createInviteForm() {

        InviteForm inviteForm = new InviteForm();
        inviteForm.setEmail(etInputEmail.getText().toString());
        inviteForm.setProjectId(Long.parseLong(etInputProjectId.getText().toString()));
        if (cbIsHandler.isChecked()) {
            inviteForm.setProjectRights(InviteForm.projectRights.handler);
        }
        if(cbIsProcessor.isChecked()) {
            inviteForm.setProjectRights(InviteForm.projectRights.processor);
        }
        if(!isAdmin.isChecked()) {
            inviteForm.setRole(Role.ROLE_USER);
        } else {
            inviteForm.setRole(Role.ROLE_ADMIN);
        }

        return inviteForm;
    }

    public void startOpenMailView(View view) {

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:" + etInputEmail.getText().toString()));
        email.putExtra(Intent.EXTRA_SUBJECT, "Invite");
        email.putExtra(Intent.EXTRA_TEXT, "text");
        startActivity(email);

//        Intent intent = new Intent(Intent.ACTION_VIEW)
//                .setType("plain/text")
//                .setData(Uri.parse(etInputEmail.toString()));
//        startActivity(intent);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        Uri data = Uri.parse("mailto:?subject=" + etInputEmail + "&body"+ "hello");
//        intent.setData(data);
//        startActivity(intent);
    }
}
