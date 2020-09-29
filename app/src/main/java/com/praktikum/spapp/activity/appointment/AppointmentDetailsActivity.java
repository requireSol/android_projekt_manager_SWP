package com.praktikum.spapp.activity.appointment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.general.WelcomeActivity;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.service.AppointmentService;
import com.praktikum.spapp.common.DateStringSplitter;
import com.praktikum.spapp.model.Appointment;
import com.praktikum.spapp.service.internal.AppointmentServiceImpl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class AppointmentDetailsActivity extends AppCompatActivity {

    AppointmentService service = new AppointmentServiceImpl(SessionManager.getSession());

    private EditText et_name;
    private EditText et_startDate;
    private EditText et_endDate;
    private EditText et_startTime;
    private EditText et_endTime;
    private EditText et_description;
    private TextView popUpName;
    private TextView popUpType;
    private DatePickerDialog dateStartPicker;
    private TimePickerDialog timeStartPicker;
    private DatePickerDialog dateEndPicker;
    private TimePickerDialog timeEndPicker;
    private Dialog myDialog;
    private Button button_delete_appointment_and_cancel;
    private Button btnEdit;
    private Button button_export_to_calendar;
    private Integer appointmentId;
    ArrayList<Project> projectArrayList;
    private Context aContext;
    int uniqueId = 0;
    private boolean editedSaved = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (editedSaved) {
            Intent intent = new Intent(aContext, WelcomeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        Spinner et_types = (Spinner) findViewById(R.id.et_types);
        et_types.setEnabled(false);
        myDialog = new Dialog(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appointment_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        et_types.setAdapter(adapter);

        AtomicBoolean editMode = new AtomicBoolean(false);

        Appointment appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        long appointmentId = (long) getIntent().getSerializableExtra("appointmentId");
        if (appointment.getType() != null) {
            int spinnerPosition = adapter.getPosition(appointment.getType().toString());
            et_types.setSelection(spinnerPosition);
        }


        //set button
        button_export_to_calendar = findViewById(R.id.button_export_to_calendar);
        Button buttonEaC = findViewById(R.id.button_edit_appointment_and_cancel);
        Button buttonEditSave = findViewById(R.id.button_save_appointment);
        button_delete_appointment_and_cancel = (Button) findViewById(R.id.button_delete_appointment_and_cancel);
        button_delete_appointment_and_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(appointment, v);
            }
        });

        et_startDate = (EditText) findViewById(R.id.et_startDate);
        this.setDate(et_startDate, appointment, true);

        et_startTime = (EditText) findViewById(R.id.et_startTime);
        this.setTime(et_startTime, appointment, true);

        et_endDate = (EditText) findViewById(R.id.et_endDate);
        this.setDate(et_endDate, appointment, false);

        et_endTime = (EditText) findViewById(R.id.et_endTime);
        this.setTime(et_endTime, appointment, false);


        button_export_to_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();

                Integer startYear = DateStringSplitter.yearPrettyPrint(appointment.getStartDate());
                Integer startMonth = DateStringSplitter.monthPrettyPrint(appointment.getStartDate());
                Integer startDay = DateStringSplitter.dayPrettyPrint(appointment.getStartDate());

                Integer startHour = DateStringSplitter.hourPrettyPrint(appointment.getStartDate());
                Integer startMinute = DateStringSplitter.monthPrettyPrint(appointment.getStartDate());

                beginTime.set(startYear, startMonth, startDay, startHour, startMinute);

                Calendar endTime = Calendar.getInstance();

                Integer endYear = DateStringSplitter.yearPrettyPrint(appointment.getEndDate());
                Integer endMonth = DateStringSplitter.monthPrettyPrint(appointment.getEndDate());
                Integer endDay = DateStringSplitter.dayPrettyPrint(appointment.getEndDate());

                Integer endHour = DateStringSplitter.hourPrettyPrint(appointment.getEndDate());
                Integer endMinute = DateStringSplitter.monthPrettyPrint(appointment.getEndDate());

                endTime.set(endYear, endMonth, endDay, endHour, endMinute);


                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                String highScore = sharedPref.getString("SAVED_APPOINTMENT", "false");
                String [] stringArray = highScore.split("");
                int sad = stringArray.length;
                boolean setted = false;
                if(!(highScore.equals("false"))) {
                    for (int i = 0; i < stringArray.length; i++) {
                        if (Long.parseLong(stringArray[i]) == appointmentId) {
                            setted = true;
                        }
                    }
                }
                if(!setted) {
                    Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, appointmentId);
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(uri)
                            .putExtra(CalendarContract.Events.TITLE, et_name.getText().toString() + "" + (et_types.getSelectedItem().toString().equals("None") ? "" : " (" + et_types.getSelectedItem().toString() + ")")) // Simple title
                            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis() + 1320000)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis() + 1320000)
                            .putExtra(CalendarContract.Events.DESCRIPTION, et_description.getText().toString()) // Description
                            .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    String appointments = sharedPref.getString("SAVED_APPOINTMENT", "false");
                    editor.putString("SAVED_APPOINTMENT", appointments += Long.toString(appointmentId));
                    editor.commit();
                    v.getContext().startActivity(intent);
                }else{
                    /*Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, appointmentId);
                    Intent intent = new Intent(Intent.ACTION_EDIT)
                            .setData(uri)
                            .putExtra(CalendarContract.Events.TITLE, et_name.getText().toString() + "" + (et_types.getSelectedItem().toString().equals("None") ? "" : " (" + et_types.getSelectedItem().toString() + ")")) // Simple title
                            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis() + 1320000)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis() + 1320000)
                            .putExtra(CalendarContract.Events.DESCRIPTION, et_description.getText().toString()) // Description
                            .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
                    v.getContext().startActivity(intent);
                    Snackbar.make(v, "Appointment updated.", Snackbar.LENGTH_LONG).show();*/
                    ShowExportPopup(appointment, v);
                }
            }
        });


        //set bind ETs and set values
        et_name = findViewById(R.id.et_name);
        et_name.setText(appointment.getName());

        et_startDate = findViewById(R.id.et_startDate);
        et_startDate.setText(DateStringSplitter.datePrettyPrint(appointment.getStartDate()));

        et_endDate = findViewById(R.id.et_endDate);
        et_endDate.setText(DateStringSplitter.datePrettyPrint(appointment.getEndDate()));

        et_startTime = findViewById(R.id.et_startTime);
        et_startTime.setText(DateStringSplitter.timePrettyPrint(appointment.getStartDate()));

        et_endTime = findViewById(R.id.et_endTime);
        et_endTime.setText(DateStringSplitter.timePrettyPrint(appointment.getEndDate()));

        et_description = findViewById(R.id.et_description);
        et_description.setText(appointment.getDescription());

        // on clicklisteners
        buttonEaC.setOnClickListener((View view) -> {

            if (!editMode.get()) {
                editMode.set(true);
                et_name.setEnabled(true);
                et_startDate.setEnabled(true);
                et_endDate.setEnabled(true);
                et_description.setEnabled(true);
                et_startTime.setEnabled(true);
                et_endTime.setEnabled(true);
                et_types.setEnabled(true);

                buttonEaC.setText("Cancel");
                buttonEditSave.setVisibility(View.VISIBLE);
                button_export_to_calendar.setVisibility(View.GONE);


                buttonEditSave.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        Calendar beginTime = Calendar.getInstance();

                        Integer startYear = DateStringSplitter.yearPrettyPrint(appointment.getStartDate());
                        Integer startMonth = DateStringSplitter.monthPrettyPrint(appointment.getStartDate());
                        Integer startDay = DateStringSplitter.dayPrettyPrint(appointment.getStartDate());

                        Integer startHour = DateStringSplitter.hourPrettyPrint(appointment.getStartDate());
                        Integer startMinute = DateStringSplitter.monthPrettyPrint(appointment.getStartDate());

                        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);

                        Calendar endTime = Calendar.getInstance();

                        Integer endYear = DateStringSplitter.yearPrettyPrint(appointment.getEndDate());
                        Integer endMonth = DateStringSplitter.monthPrettyPrint(appointment.getEndDate());
                        Integer endDay = DateStringSplitter.dayPrettyPrint(appointment.getEndDate());

                        Integer endHour = DateStringSplitter.hourPrettyPrint(appointment.getEndDate());
                        Integer endMinute = DateStringSplitter.monthPrettyPrint(appointment.getEndDate());

                        endTime.set(endYear, endMonth, endDay, endHour, endMinute);
                        long csdfd = endTime.getTimeInMillis();
                        long sdofkn = beginTime.getTimeInMillis();
                        if (!(endTime.getTimeInMillis() < beginTime.getTimeInMillis())) {
                            aContext = v.getContext();
                            JsonObject data = new JsonObject();
                            data.addProperty("name", et_name.getText().toString());
                            data.addProperty("type", et_types.getSelectedItem().toString());
                            data.addProperty("startDate", DateStringSplitter.changeToDateFormat(et_startDate.getText().toString(), et_startTime.getText().toString(), aContext));
                            data.addProperty("endDate", DateStringSplitter.changeToDateFormat(et_endDate.getText().toString(), et_endTime.getText().toString(), aContext));
                            data.addProperty("description", et_description.getText().toString());

                            new Thread(() -> {
                                try {
                                    service.updateAppointment(data, appointment.getId());
                                    runOnUiThread(() -> {

                                        editMode.set(false);
                                        et_name.setEnabled(false);
                                        et_startDate.setEnabled(false);
                                        et_endDate.setEnabled(false);
                                        et_description.setEnabled(false);
                                        et_endTime.setEnabled(false);
                                        et_startTime.setEnabled(false);
                                        et_types.setEnabled(false);
                                        buttonEaC.setText("Edit");
                                        buttonEditSave.setVisibility(View.GONE);
                                        button_export_to_calendar.setVisibility(View.VISIBLE);


                                        /*et_name.setText(result.getName());
                                        et_startDate.setText(result.getStartDate());
                                        et_endDate.setText(result.getEndDate());
                                        et_description.setText(result.getDescription());


                                         */
                                        editedSaved = true;
                                        Snackbar.make(view, "You have successfully saved your changes.", Snackbar.LENGTH_LONG).show();

                                    });
                                } catch (ResponseException e) {
                                    runOnUiThread(() -> {
                                        Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    });

                                }
                            }).start();
                        } else {
                            Snackbar.make(view, "The start date cannot be later than the end date!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                });


            } else {

                et_name.setText(appointment.getName());
                et_startDate.setText(DateStringSplitter.datePrettyPrint(appointment.getStartDate()));
                et_endDate.setText(DateStringSplitter.datePrettyPrint(appointment.getEndDate()));
                et_description.setText(appointment.getDescription());


                editMode.set(false);
                et_name.setEnabled(false);
                et_startDate.setEnabled(false);
                et_endDate.setEnabled(false);
                et_description.setEnabled(false);
                et_startTime.setEnabled(false);
                et_endTime.setEnabled(false);
                et_types.setEnabled(false);
                buttonEaC.setText("Edit");
                buttonEditSave.setVisibility(View.GONE);
                button_export_to_calendar.setVisibility(View.VISIBLE);


            }

        });

    }

    public void ShowPopup(Appointment appointment, View view) {
        TextView txtclose;
        Button btnDelete;
        Appointment appointment1 = appointment;

        myDialog.setContentView(R.layout.activity_static_pop_up);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        popUpType = (TextView) myDialog.findViewById(R.id.popUpTyp);
        System.out.print(appointment.getName());
        if (appointment.getType() != null) {
            popUpType.setText(appointment.getType().toString());
        }
        popUpName = (TextView) myDialog.findViewById(R.id.popUpName);
        popUpName.setText(appointment.getName());
        btnDelete = (Button) myDialog.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        service.deleteAppointment(appointment.getId());
                        Snackbar.make(view, "You deleted this appointment.", Snackbar.LENGTH_LONG).show();
                        aContext = v.getContext();
                        editedSaved = true;
                    } catch (ResponseException e) {
                        runOnUiThread(() -> {
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        });
                   }
                }).start();

                myDialog.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowExportPopup(Appointment appointment, View view) {
        TextView txtclose;
        Button btnDelete;
        Appointment appointment1 = appointment;

        myDialog.setContentView(R.layout.activity_export_appointment_pop_up);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        TextView exportTitle = (TextView) myDialog.findViewById(R.id.exportTitle);
        TextView exportDescription = (TextView) myDialog.findViewById(R.id.exportDescription);
        TextView exportStartDate = (TextView) myDialog.findViewById(R.id.exportStartDate);
        TextView exportEndDate = (TextView) myDialog.findViewById(R.id.exportEndDate);
        exportTitle.setText(appointment.getName());
        exportDescription.setText(appointment.getDescription());
        exportStartDate.setText("Start " + DateStringSplitter.datePrettyPrint(appointment.getStartDate()) + " " + DateStringSplitter.timePrettyPrint(appointment.getStartDate()));
        exportEndDate.setText("End " + DateStringSplitter.datePrettyPrint(appointment.getEndDate()) + " " + DateStringSplitter.timePrettyPrint(appointment.getEndDate()));

        btnEdit = (Button) myDialog.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void setTime(EditText time, Appointment appointment, boolean start) {
        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                if (start) {
                    timeStartPicker = new TimePickerDialog(AppointmentDetailsActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time.setText(sHour + ":" + sMinute + " Uhr");
                                    appointment.setStartDate(DateStringSplitter.changeToDateFormat(et_startDate.getText().toString(), et_startTime.getText().toString(), v.getContext()));
                                }

                            }, DateStringSplitter.hourPrettyPrint(appointment.getStartDate()), DateStringSplitter.minutePrettyPrint(appointment.getStartDate()), true);
                    timeStartPicker.show();
                } else {
                    timeEndPicker = new TimePickerDialog(AppointmentDetailsActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time.setText(sHour + ":" + sMinute + " Uhr");
                                    appointment.setEndDate(DateStringSplitter.changeToDateFormat(et_endDate.getText().toString(), et_endTime.getText().toString(), v.getContext()));
                                }
                            }, DateStringSplitter.hourPrettyPrint(appointment.getEndDate()), DateStringSplitter.minutePrettyPrint(appointment.getEndDate()), true);
                    timeEndPicker.show();
                }
            }
        });
    }

    public void setDate(EditText date, Appointment appointment, boolean start) {
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                if (start) {
                    dateStartPicker = new DatePickerDialog(AppointmentDetailsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                    appointment.setStartDate(DateStringSplitter.changeToDateFormat(et_startDate.getText().toString(), et_startTime.getText().toString(), v.getContext()));
                                }
                            }, DateStringSplitter.yearPrettyPrint(appointment.getStartDate()), DateStringSplitter.monthPrettyPrint(appointment.getStartDate()), DateStringSplitter.dayPrettyPrint(appointment.getStartDate()));
                    dateStartPicker.show();
                } else {
                    dateEndPicker = new DatePickerDialog(AppointmentDetailsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                    appointment.setEndDate(DateStringSplitter.changeToDateFormat(et_endDate.getText().toString(), et_endTime.getText().toString(), v.getContext()));
                                }
                            }, DateStringSplitter.yearPrettyPrint(appointment.getEndDate()), DateStringSplitter.monthPrettyPrint(appointment.getEndDate()), DateStringSplitter.dayPrettyPrint(appointment.getEndDate()));
                    dateEndPicker.show();
                }
            }
        });
    }
}
