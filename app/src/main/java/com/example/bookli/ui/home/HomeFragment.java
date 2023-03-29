package com.example.bookli.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.MainActivity;
import com.example.bookli.OnItemClickListener;
import com.example.bookli.R;
import com.example.bookli.RoomModel;
import com.example.bookli.Room_RecyclerViewAdapter;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private FragmentHomeBinding binding;

    ArrayList<RoomModel> roomModels = new ArrayList<>();
    int[] roomImages = {R.drawable.dr2_1, R.drawable.dr2_2, R.drawable.dr2_3, R.drawable.dr3_1};
    RelativeLayout rooms;
//    TextView roomName;
//    boolean isUp;
//    TextInputEditText datePicker;
    BottomSheetDialog bottomSheetDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomSheetDialog = new BottomSheetDialog(root.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

        RecyclerView recyclerView = root.findViewById(R.id.room_carousel);

        setUpRoomModels();

        Room_RecyclerViewAdapter adapter = new Room_RecyclerViewAdapter( requireContext(), roomModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));


        rooms = root.findViewById(R.id.rooms);

        // change date
//        datePicker = root.findViewById(R.id.edit_date);
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                updateCalendar();
//            }
//
//            private void updateCalendar() {
//                String Format = "dd/MM/yy";
//                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
//
//                datePicker.setText(sdf.format(calendar.getTime()));
//            }
//        };
//
//        datePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(requireContext(), date, calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        RecyclerView recyclerView = view.findViewById(R.id.room_carousel);
//
//        setUpRoomModels();
//
//        Room_RecyclerViewAdapter adapter = new Room_RecyclerViewAdapter( requireContext(), roomModels, this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
//
//
//        rooms = view.findViewById(R.id.rooms);

        // hide booking view initially
//        rooms.animate().setDuration(500);
//        isUp = false;

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
        bottomSheetDialog.show();

//        roomName.setText(roomModels.get(position).getRoomName());
    }


}