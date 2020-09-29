package com.praktikum.spapp.activity.comment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.DateStringSplitter;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;

import com.praktikum.spapp.model.Comment;
import com.praktikum.spapp.service.CommentService;
import com.praktikum.spapp.common.Utils;
import com.praktikum.spapp.service.internal.CommentServiceImpl;


import java.util.concurrent.atomic.AtomicBoolean;


public class CommentDetailsActivity extends AppCompatActivity {

    CommentService service = new CommentServiceImpl(SessionManager.getSession());

    private TextView vorname;

    private EditText commentContent;
    private TextView commentDate;
    private Switch commentIsRestricted;
    private Switch commentIsEdited;

    private Button button_edit_comment_and_cancel;
    private Button button_edit_comment_save;

    private Button commentDeleteButton;
    private Button commenSetRestrictedButton;
    private Button commentSetPublicButton;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment_details);
        AtomicBoolean editMode = new AtomicBoolean(false);

        Comment comment = (Comment) getIntent().getSerializableExtra("comment");


        vorname = findViewById(R.id.et_first_name);
        if (comment.getAuthor().getUserInfo().getForename() != null) {
            vorname.setText(comment.getAuthor().getUserInfo().getForename() + " (" + comment.getAuthor().getUsername() + ")");
        } else {
            vorname.setText(comment.getAuthor().getUsername());
        }

        commentContent = findViewById(R.id.et_comment_content);
        commentContent.setText(comment.getContent());


        commentDate = findViewById(R.id.et_comment_date);
        commentDate.setText(DateStringSplitter.datePrettyPrint(comment.getCreationTime()) + " " + DateStringSplitter.timePrettyPrint(comment.getCreationTime()));

        commentIsRestricted = findViewById(R.id.switch_comment_restricted);
        commentIsRestricted.setChecked(comment.isRestricted());
        commentIsRestricted.setClickable(false);

        commentIsEdited = findViewById(R.id.switch_comment_edited);
        commentIsEdited.setChecked(comment.isWasEdited());
        commentIsEdited.setClickable(false);

        commentDeleteButton = findViewById(R.id.comment_delete_button);
        commenSetRestrictedButton = findViewById(R.id.comment_set_restricted);
        commentSetPublicButton = findViewById(R.id.comment_set_public);

        button_edit_comment_and_cancel = findViewById(R.id.button_edit_comment_and_cancel);
        button_edit_comment_save = findViewById(R.id.button_edit_comment_save);
        button_edit_comment_and_cancel.setOnClickListener((View view) -> {

            if (!editMode.get()) {
                editMode.set(true);
                commentContent.setEnabled(true);
                button_edit_comment_and_cancel.setText("Cancel");
                button_edit_comment_save.setVisibility(View.VISIBLE);


                button_edit_comment_save.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        comment.setContent(commentContent.getText().toString());
                        commentIsEdited.setChecked(comment.isWasEdited());
                        new Thread(() -> {
                            try {
                                service.updateComment(comment.getId(), commentIsRestricted.getShowText(), commentContent.getText().toString());
                                runOnUiThread(() -> {
                                    editMode.set(false);
                                    commentContent.setEnabled(false);

                                    button_edit_comment_and_cancel.setText("Edit");
                                    button_edit_comment_save.setVisibility(View.GONE);

                                    Snackbar.make(view, "Your changes have been saved.", Snackbar.LENGTH_LONG).show();
                                    commentIsEdited.setChecked(true);
                                });
                            } catch (ResponseException e) {
                                runOnUiThread(() -> {
                                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                });
                            }
                        }).start();
                    }
                });
            } else {
                commentContent.setText(comment.getContent());

                editMode.set(false);
                commentContent.setEnabled(false);

                button_edit_comment_and_cancel.setText("Edit");
                button_edit_comment_save.setVisibility(View.GONE);
            }
        });


        commentDeleteButton.setOnClickListener((View view) -> {
            new Thread(() -> {
                try {
                    service.deleteComment(comment.getId());
                    Snackbar.make(view, "The comment has been deleted.", Snackbar.LENGTH_LONG).show();

                } catch (ResponseException e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }).start();
        });


        commenSetRestrictedButton.setOnClickListener((View view1) -> {
            new Thread(() -> {
                try {
                    service.updateComment(comment.getId(), true, commentContent.getText().toString());
                    Snackbar.make(view1, "This comment now has restricted visibility.", Snackbar.LENGTH_LONG).show();
                    Looper.prepare();
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        public void run() {
                            commentIsRestricted.setChecked(true);
                        }
                    });
                    Looper.loop();
                } catch (ResponseException e) {
                    Snackbar.make(view1, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }).start();
        });

        commentSetPublicButton.setOnClickListener((View view1) -> {
            new Thread(() -> {
                try {
                    service.updateComment(comment.getId(), false, commentContent.getText().toString());
                    Snackbar.make(view1, "This comment now has unrestricted visibility.", Snackbar.LENGTH_LONG).show();
                    Looper.prepare();
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        public void run() {
                            commentIsRestricted.setChecked(false);
                        }
                    });
                    Looper.loop();
                } catch (ResponseException e) {
                    Snackbar.make(view1, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }).start();
        });
    }
}
