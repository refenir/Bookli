package com.example.bookli.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.OnTimeClickListener;
import com.example.bookli.R;

import java.util.ArrayList;

public class Time_RecyclerViewAdapter extends RecyclerView.Adapter<Time_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<TimeModel> timeModels;
    private ArrayList<Integer> selectedItemPosition = new ArrayList<>();
    public Time_RecyclerViewAdapter(Context context, ArrayList<TimeModel> timeModels){
        this.context = context;
        this.timeModels = timeModels;
    }

    public ArrayList<Integer> getSelectedItemPosition(){
        return selectedItemPosition;
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
        holder.timeButton.setText(timeModels.get(position).getTime());
        holder.timeButton.setEnabled(timeModels.get(position).getAvailability());

        holder.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemPosition.contains(holder.getAdapterPosition())) selectedItemPosition.remove(Integer.valueOf(holder.getAdapterPosition()));
                else selectedItemPosition.add(holder.getAdapterPosition());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        if (selectedItemPosition.contains(holder.getAdapterPosition())) {
            holder.timeButton.setBackgroundColor(holder.timeButton.getContext().getResources().getColor(R.color.md_theme_light_secondaryContainer, null));
        } else {
            holder.timeButton.setBackgroundColor(holder.timeButton.getContext().getColor(R.color.md_theme_light_primary));
        }
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
