package com.example.bookli.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.OnRoomClickListener;
import com.example.bookli.R;

import java.util.ArrayList;

public class Room_RecyclerViewAdapter extends RecyclerView.Adapter<Room_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<RoomModel> roomModels;
    private final OnRoomClickListener clickListener;

    public Room_RecyclerViewAdapter(Context context, ArrayList<RoomModel> roomModels,
                                    OnRoomClickListener clickListener){
        this.context = context;
        this.roomModels = roomModels;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public Room_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // This is where you inflate the layout (Giving a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_rooms, parent, false);

        return new Room_RecyclerViewAdapter.MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Room_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assigning values to the views we created in recycler_view_row layout file
        // based on position of the recyclerview
        holder.roomName.setText(roomModels.get(position).getRoomName());
        holder.imageView.setImageResource(roomModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnRoomClickListener {
        // Grabbing views from recycler_view_row layout file

        ImageView imageView;
        TextView roomName;

        public MyViewHolder(@NonNull View itemView, OnRoomClickListener clickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            roomName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            clickListener.onRoomClick(view, pos);
                        }
                    }

                }
            });
        }

        @Override
        public void onRoomClick(View view, int position) {
        }
    }
}
