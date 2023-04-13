package com.example.bookli.ui.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.R;

import java.util.ArrayList;

public class Event_RecyclerViewAdapter extends RecyclerView.Adapter<Event_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<EventModel> eventModels;
//    private final OnRoomClickListener clickListener;

    public Event_RecyclerViewAdapter(Context context, ArrayList<EventModel> eventModels){
//                                     OnRoomClickListener clickListener){
        this.context = context;
        this.eventModels = eventModels;
//        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public Event_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_event, parent, false);

        return new Event_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assigning values to the views we created in recycler_view_row layout file
        // based on position of the recyclerview
        String durationLook = eventModels.get(position).getDesc();
        //String durationLook = eventModels.get(position).getStartTime()+eventModels.get(position).getEndTime()+": " + eventModels.get(position).getLocation();
        holder.duration.setText(durationLook);
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grabbing views from recycler_view_row layout file



        TextView duration;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (clickListener != null) {
//                        int pos = getAdapterPosition();
//                        if (pos != RecyclerView.NO_POSITION){
//                            clickListener.onRoomClick(view, pos);
//                        }
//                    }
//
//                }
//            });
        }
    }
}
