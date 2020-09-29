package com.praktikum.spapp.activity.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonObject;
import com.praktikum.spapp.R;
import com.praktikum.spapp.common.SessionManager;
import com.praktikum.spapp.exception.ResponseException;
import com.praktikum.spapp.service.ProjectService;
import com.praktikum.spapp.service.internal.ProjectServiceImpl;
import com.praktikum.spapp.model.Project;
import com.praktikum.spapp.model.adapters.RecyclerViewAdapterUser;

public class FragmentProjectOverview extends Fragment {
    View view;
    EditText pdTitle;
    EditText pdDescription;
    Spinner spinnerType;
    Spinner spinnerStatus;

    Button editAndSaveButton;
    Button deleteAndCancelButton;
    Button deleteProjectButton;

    ProjectService service = new ProjectServiceImpl(SessionManager.getSession());

    int editMode = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project_overview, container, false);
        Project project = (Project) getArguments().getSerializable("project");
        Handler mHandler = new Handler();


        // Set Values
        pdTitle = view.findViewById(R.id.pd_overview_et_title);
        pdTitle.setText(project.getName());
        pdDescription = view.findViewById(R.id.pd_overview_et_description);
        pdDescription.setText(project.getDescription());

        editAndSaveButton = view.findViewById(R.id.pd_overview_edit_button);
        deleteAndCancelButton = view.findViewById(R.id.pd_overview_save_or_delete_button);
        //deleteProjectButton = view.findViewById(R.id.pd_overview_delete_projectButton);


        // SPINNER
        spinnerType = view.findViewById(R.id.pd_overview_spinner_type);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(getContext(),
                R.array.project_type_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setEnabled(false);
        if (project.getType() == null) {
            spinnerType.setSelection(0);
        } else {
            spinnerType.setSelection(adapterType.getPosition(project.getType().toString()));
        }

        spinnerStatus = view.findViewById(R.id.pd_overview_spinner_status);
        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(getContext(),
                R.array.project_status_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setEnabled(false);
        if (project.getProjectStatus() == null) {
            spinnerStatus.setSelection(0);
        } else {
            spinnerStatus.setSelection(adapterStatus.getPosition(project.getStatus().toString()));
        }

        // RECYCLER
        RecyclerView recyclerViewHandler = view.findViewById(R.id.pd_overview_recycler_handlers);
        RecyclerViewAdapterUser adapterHandler = new RecyclerViewAdapterUser(project.getHandlers(), getContext());
        recyclerViewHandler.setAdapter(adapterHandler);
        recyclerViewHandler.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView recyclerViewProcessor = view.findViewById(R.id.pd_overview_recycler_processors);
        RecyclerViewAdapterUser adapterProcessor = new RecyclerViewAdapterUser(project.getProcessor(), getContext());
        recyclerViewProcessor.setAdapter(adapterProcessor);
        recyclerViewProcessor.setLayoutManager(new LinearLayoutManager(getContext()));

        editAndSaveButton.setOnClickListener((View view) -> {
            switch (editMode) {
                case 0:
                    // edit
                    pdTitle.setEnabled(true);
                    pdDescription.setEnabled(true);
                    spinnerType.setEnabled(true);
                    spinnerStatus.setEnabled(true);
                    editAndSaveButton.setText("SAVE");
                    deleteAndCancelButton.setText("CANCEL");
                    editMode += 1;
                    break;

                case 1:

                    /**
                     * This should probably be handled differently. The JsonObject should be declared and initialized in the Service Class
                     * in a way that the logic is encapsuled in the service. e.g. create a Map, call service.updateProject(ID, Map);
                     *
                     */
                    JsonObject data = new JsonObject();
                    if (pdTitle.getText() != null) data.addProperty("name", pdTitle.getText().toString());
                    if (pdDescription.getText() != null)
                        data.addProperty("description", pdDescription.getText().toString());
                    if (spinnerType.getSelectedItem().toString().equalsIgnoreCase("none"))
                        data.addProperty("projectType", spinnerType.getSelectedItem().toString());
                    if (spinnerStatus.getSelectedItem().toString().equalsIgnoreCase("none"))
                        data.addProperty("projectStatus", spinnerStatus.getSelectedItem().toString());
                    new Thread(() -> {
                        try {
                            service.updateProject(project.getId(), data);
                            getActivity().runOnUiThread(() -> {

                                pdTitle.setEnabled(false);
                                pdDescription.setEnabled(false);
                                spinnerStatus.setEnabled(false);
                                spinnerType.setEnabled(false);
                                editAndSaveButton.setText("EDIT");
                                deleteAndCancelButton.setText("DELETE");

                                Snackbar.make(view, "Hooray.", Snackbar.LENGTH_LONG).show();
                                editMode -= 1;
                            });

                        } catch (ResponseException e) {
                            getActivity().runOnUiThread(() -> {
                                Snackbar.make(view, "Whoops, something went wrong.", Snackbar.LENGTH_LONG).show();
                            });
                        }
                    }).start();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + editMode);
            }
        });

        /**
         * delete: delete project
         * cancel: cancel changes
         */
        deleteAndCancelButton.setOnClickListener((View view) -> {
            switch (editMode) {
                case 0:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setMessage("Are you sure?");
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }
                    );
                    builder.setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(() -> {
                                        try {
                                            service.deleteProject(project.getId());
                                            getActivity().runOnUiThread(() -> Snackbar.make(getActivity().findViewById(R.id.activity_project_detail), "Project succesfully deleted.", Snackbar.LENGTH_LONG).show());
                                            mHandler.postDelayed(mUpdateTimeTask, 3000);

                                        } catch (ResponseException e) {
                                            getActivity().runOnUiThread(() -> Snackbar.make(view, "Deleting Project failed", Snackbar.LENGTH_LONG));
                                        }
                                    }).start();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                case 1:
                    // cancel
                    pdTitle.setEnabled(false);
                    pdDescription.setEnabled(false);
                    spinnerType.setEnabled(false);
                    spinnerStatus.setEnabled(false);

                    editMode -= 1;
                    deleteAndCancelButton.setText("DELETE");
                    editAndSaveButton.setText("EDIT");
                    pdTitle.setText(project.getName());
                    pdDescription.setText(project.getDescription());
//                    spinnerStatus.setSelection(adapterStatus.getPosition(project.getStatus().toString()));
//                    spinnerType.setSelection(adapterType.getPosition(project.getType().toString()));
            }
        });
        return view;
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            startActivity(new Intent(getActivity().getApplicationContext(), OpenAllProjectsActivity.class));
        }
    };
}







