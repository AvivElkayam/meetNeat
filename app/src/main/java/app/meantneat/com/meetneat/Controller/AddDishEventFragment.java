package app.meantneat.com.meetneat.Controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import app.meantneat.com.meetneat.R;

/**
 * Created by mac on 6/1/15.
 */
public class AddDishEventFragment extends Fragment {
    TextView dateTextView,startingTime,endingTime;
    EditText location,apartmentNumber,titleEditText;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    int startingHour,startingMinute,endingHour,endingMinute;
    int eventYear,eventMonth,eventDay;
    Calendar calendar;
    View v;
    Button continueButton;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.add_dish_event_fragment_layout,container,false);
        calendar=Calendar.getInstance();
        dateTextView = (TextView)v.findViewById(R.id.add_dish_event_fragment_date_text_view_id);
        startingTime = (TextView)v.findViewById(R.id.add_dish_event_fragment_starting_time_text_view_id);
        endingTime = (TextView)v.findViewById(R.id.add_dish_event_fragment_ending_time_text_view_id);
        location = (EditText)v.findViewById(R.id.add_dish_event_fragment_location_text_view_id);
        apartmentNumber = (EditText)v.findViewById(R.id.add_dish_event_fragment_appartment_number_text_view_id);
        titleEditText = (EditText)v.findViewById(R.id.add_dish_event_fragment_title_edit_text_id);
        continueButton = (Button)v.findViewById(R.id.buttadd_dish_event_fragment_continue_button_idon3);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("year",eventYear);
                bundle.putInt("month",eventMonth);
                bundle.putInt("day",eventDay);
                bundle.putInt("starting_hour",startingHour);
                bundle.putInt("starting_minute",startingMinute);
                bundle.putInt("ending_hour",endingHour);
                bundle.putInt("ending_minute",endingMinute);
                bundle.putString("title",titleEditText.getText().toString());
                bundle.putString("location",location.getText().toString());
                bundle.putString("apartment_number",apartmentNumber.getText().toString());
                //set Fragmentclass Arguments
                AddEventFragment fragment=new AddEventFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.chef_fragment_container,fragment, "add_event")
                        .commit();
            }
        });
        initTimePicker();



        return v;
    }

    private void initTimePicker() {
        startingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                timePickerDialog = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startingHour = hourOfDay;
                        startingMinute=minute;
                        ((TextView) v).setText(hourOfDay+":"+minute);
                    }
                },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });
        endingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                timePickerDialog = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endingHour = hourOfDay;
                        endingMinute=minute;
                        ((TextView) v).setText(hourOfDay+":"+minute);
                    }
                },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        TextView textView = (TextView)v;
                        eventYear=year;
                        eventMonth=monthOfYear;
                        eventDay=dayOfMonth;
                        ((TextView) v).setText(dayOfMonth+"."+monthOfYear+"."+year);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}