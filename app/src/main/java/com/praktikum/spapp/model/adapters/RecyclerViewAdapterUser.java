package com.praktikum.spapp.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.user.ShowUserDetailsActivity;
import com.praktikum.spapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterUser extends RecyclerView.Adapter<RecyclerViewAdapterUser.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<User> users;
    private ArrayList<User> usersAll;


    private Context aContext;

    public RecyclerViewAdapterUser(ArrayList<User> users, Context aContext) {
        this.users = users;
        this.usersAll= new ArrayList<>(users);
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

        viewHolder.projectName.setText(users.get(i).getUsername());

        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(aContext, ShowUserDetailsActivity.class);
            intent.putExtra("user", users.get(i));
            aContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    public Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filterdList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filterdList.addAll(usersAll);
            } else {
                String filterPattern= constraint.toString().toLowerCase().trim();
                for(User user : usersAll) {
                    if(user.getUsername().toLowerCase().contains(filterPattern) || user.getEmail().toLowerCase().contains(filterPattern)){
                        filterdList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            users.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };





    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView projectName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.name_project);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
