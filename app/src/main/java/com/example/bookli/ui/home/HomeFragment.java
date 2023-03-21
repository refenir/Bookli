package com.example.bookli.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.OnItemClickListener;
import com.example.bookli.R;
import com.example.bookli.RoomModel;
import com.example.bookli.Room_RecyclerViewAdapter;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private FragmentHomeBinding binding;

    ArrayList<RoomModel> roomModels = new ArrayList<>();
    int[] roomImages = {R.mipmap.dr21_foreground, R.mipmap.dr22_foreground, R.mipmap.dr23_foreground, R.mipmap.dr301_foreground};
    RelativeLayout bookingView;
    RelativeLayout rooms;
    boolean isUp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.room_carousel);

        setUpRoomModels();

        Room_RecyclerViewAdapter adapter = new Room_RecyclerViewAdapter( requireContext(), roomModels, this, bookingView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true));

        bookingView = view.findViewById(R.id.booking);
        rooms = view.findViewById(R.id.rooms);

        // hide booking view initially
        bookingView.setVisibility(View.INVISIBLE);
        bookingView.animate().translationYBy(1000);
        rooms.animate().setDuration(500);
        bookingView.animate().setDuration(500);
        isUp = false;

    }

    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideUpRooms(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -250);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    public void slideDownRooms(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -250,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void setUpRoomModels(){
        String[] roomNames = getResources().getStringArray(R.array.room_names);

        for (int i=0; i<roomNames.length; i++) {
            roomModels.add(new RoomModel(roomImages[i], roomNames[i]));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view, int position) {
        if (isUp) {

            bookingView.animate().translationYBy(1000);
//            slideDown(bookingView);
            rooms.animate().x(0).y(0);
        } else {
//            slideUp(bookingView);
            bookingView.setVisibility(View.VISIBLE);
            bookingView.animate().translationYBy(-1000);
            rooms.animate().x(0).y(-250);
        }
        isUp = !isUp;
    }
}