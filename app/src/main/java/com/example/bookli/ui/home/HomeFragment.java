package com.example.bookli.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.OnRoomClickListener;
import com.example.bookli.OnTimeClickListener;
import com.example.bookli.R;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.example.bookli.ui.booking_confirmation.BookingConfirmationActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnRoomClickListener, OnTimeClickListener {

    private FragmentHomeBinding binding;

    ArrayList<RoomModel> roomModels = new ArrayList<>();
    ArrayList<TimeModel> timeModels = new ArrayList<>();
    int[] roomImages = {R.drawable.dr2_1, R.drawable.dr2_2, R.drawable.dr2_3, R.drawable.dr3_1};
    RelativeLayout rooms;
//    TextView roomName;
//    boolean isUp;
//    TextInputEditText datePicker;
    BottomSheetDialog bottomSheetDialog;
    TextView dateSelected;
    Button bookButton;
    RecyclerView timesRecyclerView;
    Time_RecyclerViewAdapter timesAdapter;
    Room_RecyclerViewAdapter roomsAdapter;
    String[] selectedTimes;
    Calendar c;
    String formattedDate;
    Button incrementDate;
    Button reduceDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomSheetDialog = new BottomSheetDialog(root.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

        // Set date in booking form
        c = Calendar.getInstance();
        dateSelected = bottomSheetDialog.findViewById(R.id.date_selection);
        SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = date.format(c.getTime());
        dateSelected.setText(formattedDate);

       //  Increment date
        incrementDate = bottomSheetDialog.findViewById(R.id.increment_date);
        incrementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c.add(Calendar.DATE, 1);
                formattedDate = date.format(c.getTime());

                Log.v("NEXT DATE: ", formattedDate);
                dateSelected.setText(formattedDate);
            }
        });

        // Reduce date
        reduceDate = bottomSheetDialog.findViewById(R.id.decrease_date);
        reduceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c.add(Calendar.DATE, -1);
                formattedDate = date.format(c.getTime());

                Log.v("PREV DATE:", formattedDate);
                dateSelected.setText(formattedDate);
            }
        });

        rooms = root.findViewById(R.id.rooms);

        bookButton = bottomSheetDialog.findViewById(R.id.book_button);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] times = getResources().getStringArray(R.array.times);
                ArrayList<Integer> selectedTimePositions = timesAdapter.getSelectedItemPosition();
                selectedTimes = new String[selectedTimePositions.size()];
                for (int i = 0; i < selectedTimePositions.size(); i++){
                    selectedTimes[i] = times[selectedTimePositions.get(i)];
                }
                TextView dateTextView = bottomSheetDialog.findViewById(R.id.date_selection);
                String dateSelected = dateTextView.getText().toString();
                TextView bookingTitle = bottomSheetDialog.findViewById(R.id.booking_title);
                String roomName = bookingTitle.getText().toString();
                Intent intent = new Intent(getActivity(), BookingConfirmationActivity.class);
                intent.putExtra("selectedTimes", selectedTimes);
                intent.putExtra("selectedRoom", roomName);
                intent.putExtra("selectedDate", dateSelected);
                startActivity(intent);
            }
        });

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
        super.onViewCreated(view, savedInstanceState);

        // All the rooms
        RecyclerView roomsRecyclerView = view.findViewById(R.id.room_recyclerview);
        roomsRecyclerView.setHasFixedSize(true);
        setUpRoomModels();
        roomsAdapter = new Room_RecyclerViewAdapter( getContext(), roomModels, this);
        roomsRecyclerView.setAdapter(roomsAdapter);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // All the times
        timesRecyclerView = bottomSheetDialog.findViewById(R.id.time_recyclerview);
        timesRecyclerView.setHasFixedSize(true);
        setUpTimeModels();
        timesAdapter = new Time_RecyclerViewAdapter(getContext(), timeModels, this);
        timesRecyclerView.setAdapter(timesAdapter);
        timesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));

    }

    private void setUpRoomModels(){
        String[] roomNames = getResources().getStringArray(R.array.room_names);
        if (roomModels.size() == 0) {
            for (int i=0; i<roomNames.length; i++) {
                roomModels.add(new RoomModel(roomImages[i], roomNames[i]));
            }
        }
    }

    private void setUpTimeModels(){
        String[] times = getResources().getStringArray(R.array.times);
        if (timeModels.size() == 0){
            for (int i = 0; i < times.length; i++){
                timeModels.add(new TimeModel(times[i]));
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRoomClick(View view, int position) {
        bottomSheetDialog.show();
        TextView roomName = bottomSheetDialog.findViewById(R.id.booking_title);
        roomName.setText(roomModels.get(position).getRoomName());
    }

    @Override
    public void onTimeClick(int position) {
    }
}