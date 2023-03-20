package com.example.bookli.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.R;
import com.example.bookli.RoomModel;
import com.example.bookli.Room_RecyclerViewAdapter;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    ArrayList<RoomModel> roomModels = new ArrayList<>();
    int[] roomImages = {R.mipmap.dr21_foreground, R.mipmap.dr22_foreground, R.mipmap.dr23_foreground, R.mipmap.dr301_foreground};

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

        Room_RecyclerViewAdapter adapter = new Room_RecyclerViewAdapter( requireContext(), roomModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true));
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
}