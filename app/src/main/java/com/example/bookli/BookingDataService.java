package com.example.bookli;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bookli.ui.home.TimeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingDataService {

    public static final String QUERY_FOR_BOOKINGS = "http://10.16.61.159:8080/bookings?";
    Context context;
    String endTime;

    public BookingDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String msg);

        void onResponse(String endTime);
    }

    public void getEndTime(VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_BOOKINGS;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        endTime = "";
                        try {
                            JSONObject bookingInfo = response.getJSONObject(0);
                            endTime = bookingInfo.getString("endTime");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Toast.makeText(context, "End Time = " + endTime, Toast.LENGTH_LONG).show();
                        volleyResponseListener.onResponse(endTime);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Smth wrong", Toast.LENGTH_LONG).show();
                volleyResponseListener.onError("smth wrong.");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface BookingResponseListener {
        void onError(String msg);

        void onResponse(List<BookingsModel> bookings);
    }

    public void getBookedTimesByDateByRoom(String date, int roomId, BookingResponseListener bookingResponseListener){
        String url = QUERY_FOR_BOOKINGS + "startDate=" + date + "&endDate=" + date + "&rooms=" + roomId;
        List<BookingsModel> bookings = new ArrayList<>();
        // get json object
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    BookingsModel one_booking = new BookingsModel();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject booking = response.getJSONObject(i);
                        one_booking.setDate(booking.getString("date"));
                        one_booking.setEndTime(booking.getString("endTime"));
                        one_booking.setStartTime(booking.getString("startTime"));
                        one_booking.setStudentId(booking.getInt("studentId"));
                        one_booking.setRoomId(booking.getInt("roomId"));
                        bookings.add(one_booking);
                    }
                    Log.i("VOLLEY", "got data");
                    bookingResponseListener.onResponse(bookings);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface MakeBookingResponseListener {
        void onError(String msg);

        void onResponse(JSONObject jsonObject);
    }

    public void makeBooking(String date, String startTime, String endTime, int roomId, int studentId, MakeBookingResponseListener makeBookingResponseListener){
        try{
            String url = QUERY_FOR_BOOKINGS;
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("date", date);
            jsonBody.put("startTime", startTime.substring(0,2) + ":" + startTime.substring(2) + ":00");
            jsonBody.put("endTime", endTime.substring(0,2) + ":" + endTime.substring(2) + ":00");
            jsonBody.put("roomId", roomId);
            jsonBody.put("studentId", studentId);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("VOLLEY", "post success");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VOLLEY", "post failed");
                }


            });

            MySingleton.getInstance(context).addToRequestQueue(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
