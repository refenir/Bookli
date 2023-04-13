package com.example.bookli.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.BookingDataService;
import com.example.bookli.R;

import java.util.ArrayList;

public class Event_RecyclerViewAdapter extends RecyclerView.Adapter<Event_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<EventModel> eventModels;
    private final OnEventClickListener clickListener;
    BookingDataService bookingDataService;

    public Event_RecyclerViewAdapter(Context context, ArrayList<EventModel> eventModels,
                                     OnEventClickListener clickListener){
        this.context = context;
        this.eventModels = eventModels;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public Event_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_event, parent, false);

        return new Event_RecyclerViewAdapter.MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assigning values to the views we created in recycler_view_row layout file
        // based on position of the recyclerview
        String durationLook = eventModels.get(position).getDesc();
        holder.duration.setText(durationLook);
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Hi! We have an upcoming library booking at " + durationLook;
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                holder.shareButton.getContext().startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingDataService = new BookingDataService(holder.deleteButton.getContext());
                int pos = holder.getBindingAdapterPosition();
                bookingDataService.deleteBooking(eventModels.get(pos).getBookingId(), new BookingDataService.DeleteBookingResponseListener() {
                    @Override
                    public void onError(String msg) {
                        Toast.makeText(holder.deleteButton.getContext(), "delete failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse() {
                        Toast.makeText(holder.deleteButton.getContext(), "delete successful", Toast.LENGTH_SHORT).show();
                        eventModels.remove(holder.getBindingAdapterPosition());
                        notifyItemRemoved(holder.getBindingAdapterPosition());
                        notifyItemRangeChanged(holder.getBindingAdapterPosition(), eventModels.size());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnEventClickListener{
        // Grabbing views from recycler_view_row layout file

        TextView duration;
        Button shareButton;
        Button deleteButton;


        public MyViewHolder(@NonNull View itemView, OnEventClickListener clickListener) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            shareButton = itemView.findViewById(R.id.share_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            clickListener.onEventClick(view, pos);
                        }
                    }

                }
            });
        }

        @Override
        public void onEventClick(View view, int position) {

        }
    }
}
