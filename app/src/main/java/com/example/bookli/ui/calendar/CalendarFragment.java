package com.example.bookli.ui.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookli.R;
import com.example.bookli.databinding.FragmentCalendarBinding;
import com.example.bookli.data.BookingDataService;
import com.example.bookli.data.BookingsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment{
    private FragmentCalendarBinding binding;
    ArrayList<EventModel> eventModels = new ArrayList<>();
    ArrayList<EventModel> eventData = new ArrayList<>();
    String date;
    Event_RecyclerViewAdapter eventsAdapter;
    BookingDataService bookingDataService = new BookingDataService(getContext());
    ArrayList<EventModel> setOfEvents = new ArrayList<>();
    String name;
    int studentId;
    Calendar c;
    public final String sharedPrefFile = "com.example.android.mainsharedpref";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences pref = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        // getting name and student id from login page
        Bundle extras = requireActivity().getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            studentId = extras.getInt("studentId");
        } else {
            name = pref.getString("name", "");
            studentId = pref.getInt("studentId", -1);
        }

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = sdf.format(c.getTime());

        CalendarView calendarView = root.findViewById(R.id.calendarView);

        //getting selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String curDay = String.valueOf(dayOfMonth);
                String Year = String.valueOf(year);
                String Month = String.valueOf(month + 1);
                date = Year +"-"+ Month+"-" + curDay;
                setDayEvents(date, studentId);
            }

        });
        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // All the bookings of student
        RecyclerView eventsRecyclerView = view.findViewById(R.id.event_recyclerview);
        eventsRecyclerView.setHasFixedSize(true);
        setDayEvents(date, studentId);
        eventsAdapter = new Event_RecyclerViewAdapter( getContext(), setOfEvents);
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setDayEvents(String date, int studentId){
        // get the bookings of the student
        bookingDataService.getBookedTimesByDateByStudentById(date, studentId, new BookingDataService.EventsResponseListener() {
            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), "smth wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<BookingsModel> bookings) {
                String[] roomNames = getActivity().getResources().getStringArray(R.array.room_names);
                setOfEvents.clear();
                for (int i = 0; i < bookings.size(); i++) {
                    int bookingId = bookings.get(i).getBookingId();
                    String endTime = bookings.get(i).getEndTime();
                    String startTime = bookings.get(i).getStartTime();
                    int roomId = bookings.get(i).getRoomId();
                    String editedStart = startTime.substring(0,2) + "00";
                    int editedEndInt = Integer.parseInt(endTime.substring(0,2)) + 1;
                    String editedEnd = null;
                    if (editedEndInt < 10) {
                        editedEnd = "0" + editedEndInt + "00";
                    } else {
                        editedEnd = editedEndInt + "00";
                    }
                    String event = roomNames[roomId] + " from " + editedStart + "-" + editedEnd ;
                    setOfEvents.add(new EventModel(event, bookingId));
                }
                eventsAdapter.notifyDataSetChanged();
            }
        });
    }
}