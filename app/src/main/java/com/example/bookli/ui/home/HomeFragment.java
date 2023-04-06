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
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.BookingDataService;
import com.example.bookli.BookingsModel;
import com.example.bookli.OnRoomClickListener;
import com.example.bookli.OnTimeClickListener;
import com.example.bookli.R;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.example.bookli.ui.booking_confirmation.BookingConfirmationActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialCalendar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment implements OnRoomClickListener, OnTimeClickListener {

    private FragmentHomeBinding binding;
    ArrayList<RoomModel> roomModels = new ArrayList<>();
    ArrayList<TimeModel> timeModels = new ArrayList<>();
    int[] roomImages = {R.drawable.dr2_1, R.drawable.dr2_2, R.drawable.dr2_3, R.drawable.dr3_1};
    int[] capacities = {2, 5, 5, 8};
    RelativeLayout rooms;

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
    Set<String> setOfBookedTimings;
    BookingDataService bookingDataService;
    int selectedRoomImage;
    int selectedRoomPosition;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // store the unavailable timings
        setOfBookedTimings = new HashSet<String>();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        bookingDataService = new BookingDataService(getContext());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // bottom sheet that shows time and book button
        bottomSheetDialog = new BottomSheetDialog(root.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

        // initialize current date in booking form
        c = Calendar.getInstance();
        dateSelected = bottomSheetDialog.findViewById(R.id.date_selection);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = date.format(c.getTime());
        dateSelected.setText(formattedDate);

       //  Increment date, icon button at the right side
        incrementDate = bottomSheetDialog.findViewById(R.id.increment_date);
        incrementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date + 1
                c.add(Calendar.DATE, 1);
                formattedDate = date.format(c.getTime());

                Log.v("NEXT DATE: ", formattedDate);
                dateSelected.setText(formattedDate);

                setTimeButtons(formattedDate, selectedRoomPosition);

                timesAdapter.clearSelectedItemPosition();
            }
        });

        // Reduce date, icon button at the left side
        reduceDate = bottomSheetDialog.findViewById(R.id.decrease_date);
        reduceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date - 1
                c.add(Calendar.DATE, -1);
                formattedDate = date.format(c.getTime());

                Log.v("PREV DATE:", formattedDate);
                dateSelected.setText(formattedDate);

                setTimeButtons(formattedDate,selectedRoomPosition);

                timesAdapter.clearSelectedItemPosition();
            }
        });

        rooms = root.findViewById(R.id.rooms);

        bookButton = bottomSheetDialog.findViewById(R.id.book_button);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the selected timings
                String[] times = getResources().getStringArray(R.array.times);
                ArrayList<Integer> selectedTimePositions = timesAdapter.getSelectedItemPosition();
                selectedTimes = new String[selectedTimePositions.size()];
                for (int i = 0; i < selectedTimePositions.size(); i++){
                    selectedTimes[i] = times[selectedTimePositions.get(i)];
                }
                // get the selected date
                TextView dateTextView = bottomSheetDialog.findViewById(R.id.date_selection);
                String dateSelected = dateTextView.getText().toString();
                // get the selected room
                TextView bookingTitle = bottomSheetDialog.findViewById(R.id.booking_title);
                String roomName = bookingTitle.getText().toString();

                // update backend, post request to database to make booking
                bookingDataService.makeBooking(dateSelected, selectedTimes[0], selectedTimes[selectedTimes.length-1], selectedRoomPosition, 1006876, new BookingDataService.MakeBookingResponseListener() {
                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(int bookingId) {
                        Toast.makeText(getContext(), "Response:" + bookingId, Toast.LENGTH_SHORT).show();
                        // pass data to booking confirmation screen
                        Intent intent = new Intent(getActivity(), BookingConfirmationActivity.class);
                        intent.putExtra("selectedTimes", selectedTimes);
                        intent.putExtra("selectedRoom", roomName);
                        intent.putExtra("selectedDate", dateSelected);
                        intent.putExtra("image", selectedRoomImage);
                        intent.putExtra("bookingId", bookingId);
                        startActivity(intent);
                    }
                });
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
        timesAdapter = new Time_RecyclerViewAdapter(getContext(), timeModels);
        timesRecyclerView.setAdapter(timesAdapter);
        timesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));

    }
    // get the data for the unavailable times
    private void setTimeButtons(String date, int roomPosition){

        bookingDataService.getBookedTimesByDateByRoom(date, roomPosition, new BookingDataService.BookingResponseListener() {
            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), "smth wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<BookingsModel> bookings) {
                setOfBookedTimings.clear();
                for (int i = 0; i < bookings.size(); i++){
                    String endTime = bookings.get(i).getEndTime();
                    String startTime = bookings.get(i).getStartTime();
                    int[] range = IntStream.rangeClosed(Integer.parseInt(startTime.substring(0,2)),
                            Integer.parseInt(endTime.substring(0,2))).toArray();
                    for (int j = 0; j < range.length; j++) {
                        String time;
                        if (range[j] < 10) time = "0" + range[j] + ":00:00";
                        else time = range[j] + ":00:00";
                        setOfBookedTimings.add(time);
                    }
                }
//                Toast.makeText(getContext(), bookings.toString(), Toast.LENGTH_SHORT).show();
                setUpTimeModels();
                timesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpRoomModels(){
        roomModels.clear();
        String[] roomNames = getResources().getStringArray(R.array.room_names);

        for (int i=0; i<roomNames.length; i++) {

            roomModels.add(new RoomModel(roomImages[i], roomNames[i],
                    String.format(getResources().getString(R.string.capacity), capacities[i])));
        }
    }

    private void setUpTimeModels(){
        timeModels.clear();
        String[] times = getResources().getStringArray(R.array.times);

        for (int i = 0; i < times.length; i++){
            String formattedTime = times[i].substring(0,2) + ":" + times[i].substring(2) + ":00";
            if (setOfBookedTimings.contains(formattedTime)) {
                timeModels.add(new TimeModel(times[i], false));
            } else{
                timeModels.add(new TimeModel(times[i]));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timesAdapter.clearSelectedItemPosition();
        setTimeButtons(dateSelected.getText().toString(), selectedRoomPosition);
    }

    @Override
    public void onRoomClick(View view, int position) {
        bottomSheetDialog.show();
        TextView roomName = bottomSheetDialog.findViewById(R.id.booking_title);
        roomName.setText(roomModels.get(position).getRoomName());
        selectedRoomImage = roomModels.get(position).getImage();
        selectedRoomPosition = position;
        setTimeButtons(dateSelected.getText().toString(), selectedRoomPosition);
        timesAdapter.notifyDataSetChanged();
    }
// maybe useless, delete
    @Override
    public void onTimeClick(int position) {
    }
}