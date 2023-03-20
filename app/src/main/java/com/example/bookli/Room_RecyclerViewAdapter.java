package com.example.bookli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Room_RecyclerViewAdapter extends RecyclerView.Adapter<Room_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<RoomModel> roomModels;
    public Room_RecyclerViewAdapter(Context context, ArrayList<RoomModel> roomModels){
        this.context = context;
        this.roomModels = roomModels;
    }
    @NonNull
    @Override
    public Room_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // This is where you inflate the layout (Giving a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);

        return new Room_RecyclerViewAdapter.MyViewHolder(view);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // Grabbing views from recycler_view_row layout file

        ImageView imageView;
        TextView roomName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            roomName = itemView.findViewById(R.id.textView);
        }
    }
}
