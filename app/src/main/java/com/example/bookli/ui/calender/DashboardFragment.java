package com.example.bookli.ui.calender;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookli.R;
import com.example.bookli.databinding.FragmentDashboardBinding;
import com.example.bookli.BookingDataService;
import com.example.bookli.BookingsModel;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnEventClickListener {
    private FragmentDashboardBinding binding;
    ArrayList<EventModel> eventModels = new ArrayList<>();
    ArrayList<EventModel> eventData = new ArrayList<>();
    public String day;
    RelativeLayout events;
    Event_RecyclerViewAdapter eventsAdapter;
    BookingDataService bookingDataService = new BookingDataService(getContext());
    ArrayList<EventModel> setOfEvents = new ArrayList<>();
    String name;
    int studentId;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // getting name and student id from login page
        Bundle extras = requireActivity().getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            studentId = extras.getInt("studentId");
        }
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        CalendarView calendarView = root.findViewById(R.id.calendarView);

        //getting selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String curDay = String.valueOf(dayOfMonth);
                String Year = String.valueOf(year);
                String Month = String.valueOf(month);
                day = Year +"-"+ Month+"-" + curDay;
                setDayEvents(day, name, studentId);
            }

        });
        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // All the rooms
        RecyclerView eventsRecyclerView = view.findViewById(R.id.event_recyclerview);
        eventsRecyclerView.setHasFixedSize(true);
        setDayEvents(day, name, studentId);
        eventsAdapter = new Event_RecyclerViewAdapter( getContext(), setOfEvents );
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void setDayEvents(String date, String name, int studentId){


        bookingDataService.getBookedTimesByDateByStudentById(name, date, studentId, new BookingDataService.EventsResponseListener() {
            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), "smth wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<BookingsModel> bookings) {
                setOfEvents.clear();
                for (int i = 0; i < bookings.size(); i++) {

                    String endTime = bookings.get(i).getEndTime();
                    String startTime = bookings.get(i).getStartTime();
                    String location = String.valueOf(bookings.get(i).getRoomId());
                    String editedStart = startTime.charAt(0) + startTime.charAt(1) + ":00";
                    String editedEnd = endTime.charAt(0) + endTime.charAt(1) + ":00";
                    String event = editedStart + "-" + editedEnd + ": " + location;
                    setOfEvents.add(new EventModel(event));
                }
            }
        });
    }


    @Override
    public void onEventClick(View view, int position) {
        //blob
    }
}