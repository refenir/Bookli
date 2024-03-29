package com.example.bookli.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.R;
import com.example.bookli.ui.login.LoginActivity;

import java.util.ArrayList;

public class Time_RecyclerViewAdapter extends RecyclerView.Adapter<Time_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<TimeModel> timeModels;
    private ArrayList<TimeButton> selectedItemPosition = new ArrayList<>();
    private static Button lastSelected = null;
    private static int lastSelectedPos = 0;
    public Time_RecyclerViewAdapter(Context context, ArrayList<TimeModel> timeModels){
        this.context = context;
        this.timeModels = timeModels;
    }

    public ArrayList<TimeButton> getSelectedItemPosition(){
        return selectedItemPosition;
    }
    public void clearSelectedItemPosition() {
        selectedItemPosition.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Time_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_time, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Time_RecyclerViewAdapter.MyViewHolder holder, int position) {
        TimeButton timeButton = new TimeButton(holder.timeButton, position);
        holder.timeButton.setText(timeModels.get(position).getTime());
        holder.timeButton.setEnabled(timeModels.get(position).getAvailability());
        // set the behaviour of how button react when tapped
        // selected buttons become darker, only 2 buttons can be selected at one time (start and end)
        holder.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemPosition.contains(timeButton)) {
                    selectedItemPosition.remove(timeButton);
                    timeButton.getTimeButton().setBackgroundColor(holder.timeButton.getContext().getResources().getColor(R.color.md_theme_light_secondaryContainer, null));
                    timeButton.getTimeButton().setTextColor(Color.BLACK);
                } else if (selectedItemPosition.size() >= 2) {
                    selectedItemPosition.add(timeButton);
                    TimeButton removedButton = selectedItemPosition.remove(0);
                    removedButton.getTimeButton().setBackgroundColor(holder.timeButton.getContext().getResources().getColor(R.color.md_theme_light_secondaryContainer, null));
                    removedButton.getTimeButton().setTextColor(Color.BLACK);
                    timeButton.getTimeButton().setBackgroundColor(holder.timeButton.getContext().getResources().getColor(R.color.md_theme_light_secondary, null));
                    timeButton.getTimeButton().setTextColor(Color.WHITE);
                }else {
                    selectedItemPosition.add(timeButton);
                    timeButton.getTimeButton().setBackgroundColor(holder.timeButton.getContext().getResources().getColor(R.color.md_theme_light_secondary, null));
                    timeButton.getTimeButton().setTextColor(Color.WHITE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return timeModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button timeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeButton = itemView.findViewById(R.id.time_button);
        }

    }
}
