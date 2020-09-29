package com.praktikum.spapp.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praktikum.spapp.R;
import com.praktikum.spapp.activity.appointment.AppointmentDetailsActivity;
import com.praktikum.spapp.common.DateStringSplitter;
import com.praktikum.spapp.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterAppointment extends RecyclerView.Adapter<RecyclerViewAdapterAppointment.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Appointment> appointments;
    private ArrayList<Appointment> appointmentsAll;


    private Context aContext;

    public RecyclerViewAdapterAppointment(ArrayList<Appointment> appointments, Context aContext) {
        this.appointments = appointments;
        this.appointmentsAll = new ArrayList<>();
        this.aContext = aContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_appointment, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appointmentName;
        TextView appointmentType;
        TextView appointmentDate;
        RelativeLayout parentLayout;
        Button button_create_appointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentName = itemView.findViewById(R.id.appointment_adapter_name);
            appointmentType = itemView.findViewById(R.id.appointment_adapter_type);
            appointmentDate = itemView.findViewById(R.id.appointment_adapter_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder:  called.");

        viewHolder.appointmentName.setText(appointments.get(i).getName());
        viewHolder.appointmentDate.setText("Start: " + DateStringSplitter.datePrettyPrint(appointments.get(i).getStartDate()) + " " + DateStringSplitter.timePrettyPrint(appointments.get(i).getStartDate()) + "\nEnd: " + DateStringSplitter.datePrettyPrint(appointments.get(i).getEndDate()) + " " + DateStringSplitter.timePrettyPrint(appointments.get(i).getEndDate()));
        if( appointments.get(i).getType() != null) {
           viewHolder.appointmentType.setText(appointments.get(i).getType().toString());
        }
        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(aContext, AppointmentDetailsActivity.class);
            intent.putExtra("appointment", appointments.get(i));
            intent.putExtra("appointmentId", new Long(i));
            aContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    public Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Appointment> filterdList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterdList.addAll(appointmentsAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Appointment appointment : appointmentsAll) {
                    if (appointment.getName().toLowerCase().contains(filterPattern)) {
                        filterdList.add(appointment);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            appointments.clear();
            appointments.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
