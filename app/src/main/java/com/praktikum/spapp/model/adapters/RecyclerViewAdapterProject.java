package com.praktikum.spapp.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.project.ProjectDetailActivity;
import com.praktikum.spapp.model.Project;

import java.util.ArrayList;

public class RecyclerViewAdapterProject extends RecyclerView.Adapter<RecyclerViewAdapterProject.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Project> projects;
    private Context aContext;

    public RecyclerViewAdapterProject(ArrayList<Project> projectNames, Context aContext) {
        this.projects = projectNames;
        this.aContext = aContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder:  called.");

        viewHolder.projectName.setText(projects.get(i).getName());

        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(aContext, ProjectDetailActivity.class);
            intent.putExtra("project", projects.get(i));
            //If appoints changed from intern it will put this boolean as true.
            intent.putExtra("changed", false);
            intent.putExtra("createdComment", false);
            aContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView projectName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.name_project);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
