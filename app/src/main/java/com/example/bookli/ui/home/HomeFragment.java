package com.example.bookli.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.data.BookingDataService;
import com.example.bookli.data.BookingsModel;
import com.example.bookli.R;
import com.example.bookli.databinding.FragmentHomeBinding;
import com.example.bookli.ui.booking_confirmation.BookingConfirmationActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment implements OnRoomClickListener{

    private FragmentHomeBinding binding;
    ArrayList<RoomModel> roomModels = new ArrayList<>();
    ArrayList<TimeModel> timeModels = new ArrayList<>();
    int[] roomImages = {R.drawable.dr2_1, R.drawable.dr2_2, R.drawable.dr2_3, R.drawable.dr3_1};
    int[] capacities = {2, 5, 5, 8};

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
    String name;
    int studentId;
    String email;
    String phoneNumber;
    TextInputEditText dateEdit;
    Boolean[] roomAvailability = new Boolean[4];
    public final String sharedPrefFile = "com.example.android.mainsharedpref";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // store the unavailable timings
        setOfBookedTimings = new HashSet<String>();


        bookingDataService = new BookingDataService(getContext());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences pref = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        // get user details from MainActivity
        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            studentId = bundle.getInt("studentId");
            phoneNumber = bundle.getString("phoneNumber");
            email = bundle.getString("email");
        } else {
            name = pref.getString("name", "");
            studentId = pref.getInt("studentId", 1000000);
            phoneNumber = pref.getString("phoneNumber", "");
            email = pref.getString("email", "");
        }

        // bottom sheet that shows time and book button
        bottomSheetDialog = new BottomSheetDialog(root.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

        // initialize current date in booking form
        c = Calendar.getInstance();
        dateEdit = binding.search;
        SimpleDateFormat date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat dateFormatBackend = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateEdit.setText(date.format(c.getTime()));
        dateSelected = bottomSheetDialog.findViewById(R.id.date_selection);

       //  Increment date, icon button at the right side
        incrementDate = bottomSheetDialog.findViewById(R.id.increment_date);
        incrementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date + 1

                c.add(Calendar.DATE, 1);
                formattedDate = dateFormatBackend.format(c.getTime());

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
                formattedDate = dateFormatBackend.format(c.getTime());

                Log.v("PREV DATE:", formattedDate);
                dateSelected.setText(formattedDate);

                setTimeButtons(formattedDate,selectedRoomPosition);

                timesAdapter.clearSelectedItemPosition();
            }
        });

        bookButton = bottomSheetDialog.findViewById(R.id.book_button);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the selected timings
                String[] times = getResources().getStringArray(R.array.times);
                ArrayList<TimeButton> selectedTimePositions = timesAdapter.getSelectedItemPosition();
                if (selectedTimePositions.isEmpty()) {
                    Toast.makeText(getContext(), "Please select at least one time slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedTimes = new String[selectedTimePositions.size()];
                for (int i = 0; i < selectedTimePositions.size(); i++){
                    selectedTimes[i] = times[selectedTimePositions.get(i).getPosition()];
                }
                Arrays.sort(selectedTimes);
                // get the selected date
                TextView dateTextView = bottomSheetDialog.findViewById(R.id.date_selection);
                String dateSelected = dateTextView.getText().toString();
                // get the selected room
                TextView bookingTitle = bottomSheetDialog.findViewById(R.id.booking_title);
                String roomName = bookingTitle.getText().toString();
                String occupantDetails = name + " " + phoneNumber + " " + email;

                // update backend, post request to database to make booking
                bookingDataService.makeBooking(dateSelected, selectedTimes[0], selectedTimes[selectedTimes.length-1], selectedRoomPosition, studentId, occupantDetails, new BookingDataService.MakeBookingResponseListener() {
                    @Override
                    public void onError(String msg) {
                    }

                    @Override
                    public void onResponse(int bookingId) {
                        timesAdapter.clearSelectedItemPosition();
                        // pass data to booking confirmation screen
                        Intent intent = new Intent(getActivity(), BookingConfirmationActivity.class);
                        intent.putExtra("selectedTimes", selectedTimes);
                        intent.putExtra("selectedRoom", roomName);
                        intent.putExtra("selectedDate", dateSelected);
                        intent.putExtra("image", selectedRoomImage);
                        intent.putExtra("bookingId", bookingId);
                        timesAdapter.clearSelectedItemPosition();
                        timesAdapter.notifyDataSetChanged();
                        startActivity(intent);

                    }
                });
            }
        });

        // set up date picker
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now());

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();
        //  when they press ok on the date picker
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {

            @Override
            public void onPositiveButtonClick(Object selection) {
                dateEdit.setText(datePicker.getHeaderText());
                try {
                    Date newDate = formatDate(datePicker.getHeaderText());
                    c.setTime(newDate);
                    String date = dateFormatBackend.format(newDate);
                    roomAvailability(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        // change date
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // All the rooms
        RecyclerView roomsRecyclerView = view.findViewById(R.id.room_recyclerview);
        roomsRecyclerView.setHasFixedSize(true);
        SimpleDateFormat dateFormatBackend = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currDate;
        try {
            currDate = dateFormatBackend.format(formatDate(dateEdit.getText().toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // see if the room is available on the date selected
        roomAvailability(currDate);
        roomsAdapter = new Room_RecyclerViewAdapter( getContext(), roomModels, this);
        roomsRecyclerView.setAdapter(roomsAdapter);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // All the times
        timesRecyclerView = bottomSheetDialog.findViewById(R.id.time_recyclerview);
        setUpTimeModels();
        timesAdapter = new Time_RecyclerViewAdapter(getContext(), timeModels);
        timesRecyclerView.setAdapter(timesAdapter);
        timesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));

    }
    // get the data for the unavailable times
    private void setTimeButtons(String date, int roomPosition){

        bookingDataService.getBookedTimesByDate(date, roomPosition, new BookingDataService.BookingResponseListener() {
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
                setUpTimeModels();
                timesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpTimeModels(){
        timeModels.clear();
        String[] times = getResources().getStringArray(R.array.times);

        for (int i = 0; i < times.length; i++){
            String formattedTime = times[i].substring(0,2) + ":" + times[i].substring(2) + ":00";
            if (setOfBookedTimings.contains(formattedTime)) {
                timeModels.add(new TimeModel(times[i], false));
            } else{
                timeModels.add(new TimeModel(times[i], true));
            }
        }
    }

    private void setUpRoomModels(){
        roomModels.clear();
        final String[] roomNames = getResources().getStringArray(R.array.room_names);
        for (int i = 0; i < roomNames.length; i++) {
            roomModels.add(new RoomModel(roomImages[i], roomNames[i],
                    String.format(getResources().getString(R.string.capacity), capacities[i]),
                    roomAvailability[i])
            );
        }
    }
     // see if room is available on a selected date
    private void roomAvailability(String date){
        bookingDataService.getBookedTimesByDate(date, -1, new BookingDataService.BookingResponseListener() {
            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), "smth wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<BookingsModel> bookings) {
                for (int i = 0; i < roomImages.length; i++) {
                    String[] times = getResources().getStringArray(R.array.times);
                    List<String> timesList = new ArrayList<>(Arrays.asList(times));
                    for (int x = 0; x < bookings.size(); x++) {
                        if (bookings.get(x).getRoomId() == i){
                            String endTime = bookings.get(x).getEndTime();
                            String startTime = bookings.get(x).getStartTime();
                            int[] range = IntStream.rangeClosed(Integer.parseInt(startTime.substring(0, 2)),
                                    Integer.parseInt(endTime.substring(0, 2))).toArray();
                            for (int j = 0; j < range.length; j++) {
                                String time;
                                if (range[j] < 10) time = "0" + range[j] + "00";
                                else time = range[j] + "00";
                                int index = timesList.indexOf(time);
                                if (index != -1) {
                                    timesList.remove(index);
                                }
                            }
                        }
                    }
                    if (timesList.size() == 0) {
                        roomAvailability[i] = false;
                    } else {
                        roomAvailability[i] = true;
                    }
                }
                setUpRoomModels();
                roomsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timesAdapter.clearSelectedItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        // reset the recyclerviews and update them with new bookings
        timesAdapter.clearSelectedItemPosition();
        setTimeButtons(dateSelected.getText().toString(), selectedRoomPosition);
        SimpleDateFormat dateFormatBackend = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currDate = null;
        try {
            currDate = dateFormatBackend.format(formatDate(dateEdit.getText().toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        roomAvailability(currDate);
    }

    // When you click on a room card view, this will trigger
    @Override
    public void onRoomClick(View view, int position) throws ParseException {
        // set date to be the same as the date in the date search bar
        TextView dateSelected = bottomSheetDialog.findViewById(R.id.date_selection);
        String oldDate = dateEdit.getText().toString();
        Date newDate = formatDate(oldDate);
        c.setTime(newDate);
        String newDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate);
        dateSelected.setText(newDateString);
        // populate room name to the room selected
        TextView roomName = bottomSheetDialog.findViewById(R.id.booking_title);
        roomName.setText(roomModels.get(position).getRoomName());
        selectedRoomImage = roomModels.get(position).getImage();
        selectedRoomPosition = position;
        // disable unavailable times
        setTimeButtons(dateSelected.getText().toString(), selectedRoomPosition);
        // make bottom sheet show up
        bottomSheetDialog.show();
    }

    // Change date from "dd MMM yyy" format to "yyyy-MM-dd"
    public Date formatDate(String date) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date newDate = dt.parse(date);
        String newDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate);
        return newDate;
    }

}