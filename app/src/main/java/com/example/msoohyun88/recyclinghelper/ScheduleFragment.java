package com.example.msoohyun88.recyclinghelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME_LOL = "savedTimeName";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TimePicker timePicker;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.time);
        final SharedPreferences pref = this.getActivity().getSharedPreferences(SHARED_PREF_NAME_LOL, Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefEditor = pref.edit();

        int restoredHour = pref.getInt("savedHour", -1);
        int restoredMinute = pref.getInt("savedMinute", -1);

        if (restoredHour != -1 && restoredMinute != -1) {
            timePicker.setHour(restoredHour);
            timePicker.setMinute(restoredMinute);
        }

        view.findViewById(R.id.buttonSaveTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = 0;

                if (((CheckBox)getView().findViewById(R.id.monday)).isChecked()) {
                    Calendar calMon = Calendar.getInstance();
                    calMon = makeCalendar(calMon, Calendar.MONDAY, timePicker.getHour(), timePicker.getMinute());
                    setAlarm(calMon.getTimeInMillis(), 1);
                    // Toast.makeText(getContext(), "MONDAY", Toast.LENGTH_LONG).show();
                    count++;

                }
                if (((CheckBox)getView().findViewById(R.id.tuesday)).isChecked()) {
                    Calendar calTues = Calendar.getInstance();
                    calTues = makeCalendar(calTues, Calendar.TUESDAY, timePicker.getHour(), timePicker.getMinute());
                    setAlarm(calTues.getTimeInMillis(), 2);
                    //  Toast.makeText(getContext(), "TUESDAY", Toast.LENGTH_LONG).show();
                    count++;
                }
                if (((CheckBox)getView().findViewById(R.id.wednesday)).isChecked()) {
                    Calendar calWed = Calendar.getInstance();
                    calWed = makeCalendar(calWed, Calendar.WEDNESDAY, timePicker.getHour(), timePicker.getMinute());
                    setAlarm(calWed.getTimeInMillis(), 3);
                    // Toast.makeText(getContext(), "WEDNESDAY", Toast.LENGTH_LONG).show();
                    count++;
                }
                if (((CheckBox)getView().findViewById(R.id.thursday)).isChecked()) {
                    Calendar calThurs = Calendar.getInstance();
                    calThurs = makeCalendar(calThurs, Calendar.WEDNESDAY, timePicker.getHour(), timePicker.getMinute());
                    setAlarm(calThurs.getTimeInMillis(), 4);
                    // Toast.makeText(getContext(), "THURSDAY", Toast.LENGTH_LONG).show();
                    count++;
                }
                if (((CheckBox)getView().findViewById(R.id.friday)).isChecked()) {
                    Calendar calFri = Calendar.getInstance();
                    calFri = makeCalendar(calFri, Calendar.FRIDAY, timePicker.getHour(), timePicker.getMinute());
                    setAlarm(calFri.getTimeInMillis(), 5);
                    // Toast.makeText(getContext(), "FRIDAY", Toast.LENGTH_LONG).show();
                    count++;
                }

                //saving user preferences
                prefEditor.putInt("savedHour", timePicker.getHour());
                prefEditor.putInt("savedMinute", timePicker.getMinute());
                prefEditor.commit();
                if (count == 0) {
                    Toast.makeText(getContext(), "Please select a day.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    //takes calendar and sets it up. has a weird lag
    private Calendar makeCalendar (Calendar cal, int day, int hour, int min) {

        cal.set(Calendar.DAY_OF_WEEK, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

/*
        Log.d("KMS", "Day of week input...." + Integer.toString(day));
        Log.d("KMS", "Hour input..." + Integer.toString(hour));

        Log.d("KMS", "Day of week: " + Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));
        Log.d("KMS", "Day of month: " + Integer.toString(cal.get(Calendar.DATE)));
        Log.d("KMS", "Month: " + Integer.toString(cal.get(Calendar.MONTH)));
        Log.d("KMS", "Hour of day: " + Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
        Log.d("KMS", "Minute: " + cal.get(Calendar.MINUTE));
        Log.d("KMS", "Second: " + cal.get(Calendar.SECOND));*/

        return cal;
    }

    private void setAlarm(long timeInMillis, int code) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), Alarm.class);

        //need diff pending intents for each alarm w dif req code
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), code, intent, 0);

        //weekly repeating alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY*7, pendingIntent);

        Toast.makeText(getActivity(), "Reminder is set.", Toast.LENGTH_SHORT).show();
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}