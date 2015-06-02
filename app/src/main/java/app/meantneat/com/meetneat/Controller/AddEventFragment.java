package app.meantneat.com.meetneat.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import app.meantneat.com.meetneat.Dish;
import app.meantneat.com.meetneat.R;


public class AddEventFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView startingTimeTextView,startingDateTextView,endingTimeTextView,endingDateTextView;
    private TextView dishTitleTextView,dishPriceTextView,dishQuantityTextView,dishDescriptionTextView;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int startingYear,startingMonth,startingDay,startingHour,startingMinute;
    private int endingYear,endingMonth,endingDay,endingHour,endingMinute;
    private Calendar calendar;
    private ListView dishesListView;
    private DishRowListAdapter dishRowListAdapter;
    private ArrayList<Dish> dishArrayList;
    private ListView eventsListView;
    private DishRowListAdapter eventsArrayAdapter;
    private Dialog addDishDialog;
    //add dish dialog views
    private EditText addDishTitleEditText,addDishPriceEditText,addDishDishesLeftEditText,addDishDescriptionEditText;
    private ImageView addDishImageView,eventImageView;
    //
    public class DishRowListAdapter extends ArrayAdapter<Dish>
    {
        public DishRowListAdapter()
        {
            super(getActivity(), R.layout.add_event_fragment_dish_row, dishArrayList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if(itemView==null)
            {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.add_event_fragment_dish_row,parent,false);
            }
            final Dish dish = dishArrayList.get(position);
            final String title = dish.getName();
            String price = "Price: "+dish.getPrice();
            String dishesLeft = "Dishes left: "+dish.getQuantity();
            final String description = dish.getDescriprion();


            TextView titleTextView = (TextView)itemView.findViewById(R.id.add_fragment_fragment_dish_row_title_text_view);
            titleTextView.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dishTitleTextView.setText(title);
                    dishPriceTextView.setText(Double.toString(dish.getPrice()));
                    dishQuantityTextView.setText(Double.toString(dish.getQuantity()));
                    dishDescriptionTextView.setText(description);
                }
            });
            return itemView;
        }
    }
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();

        return fragment;
    }

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initViews();
        buildAddDishDiaglog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_fragment, container, false);

        // Inflate the layout for this fragment
        return view;
    }
    private void getEventDetailsFromBundle()
    {



        startingYear = getArguments().getInt("year");
        startingMonth = getArguments().getInt("month");
        startingDay = getArguments().getInt("day");
        startingHour = getArguments().getInt("starting_hour");
        startingMinute = getArguments().getInt("starting_minute");
        endingHour = getArguments().getInt("ending_hour");
        endingMinute = getArguments().getInt("ending_minute");
        startingDateTextView.setText(startingDay+"."+"."+startingMonth+"."+startingYear);
        startingTimeTextView.setText(startingHour+":"+startingMinute);
        endingTimeTextView.setText(endingHour+":"+endingMinute);

    }
private void initViews()
{
    calendar=Calendar.getInstance();
    startingTimeTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_starting_time_label);
    startingDateTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_starting_date_label);
    endingTimeTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_ending_time_label);
    endingDateTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_ending_date_label);

    dishTitleTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_dish_title_text_view);
    dishPriceTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_dish_price_text_view);
    dishQuantityTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_dish_dishes_left_text_view);
    dishDescriptionTextView = (TextView)getActivity().findViewById(R.id.add_event_fragment_dish_description_text_view);

    startingDateTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    TextView textView = (TextView)v;
                    startingYear=year;
                    startingMonth=monthOfYear;
                    startingDay=dayOfMonth;
                    ((TextView) v).setText(dayOfMonth+"."+monthOfYear+"."+year);
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }
    });
    startingTimeTextView.setOnClickListener(new View.OnClickListener() {
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
    endingDateTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    TextView textView = (TextView)v;
                    endingYear=year;
                    endingMonth=monthOfYear;
                    endingDay=dayOfMonth;
                    ((TextView) v).setText(dayOfMonth+"."+monthOfYear+"."+year);
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }
    });
    endingTimeTextView.setOnClickListener(new View.OnClickListener() {
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
    eventImageView = (ImageView)getActivity().findViewById(R.id.add_event_fragment_event_image_view);
    eventImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();;
        }
    });
    initListView();
    getEventDetailsFromBundle();

}

    private void initListView() {
        dishArrayList = new ArrayList<>();
        dishArrayList.add(new Dish("פרגית במחבת","מנה טעימה ומשביעה עם טעמים עשירים",34,7,true,true,null));
        dishArrayList.add(new Dish("שניצל דה דיינר","שניצל קלאסי עם רוטב טעים",27.90,9,true,true,null));
        dishArrayList.add(new Dish("סלט פלחים","מבחר ירקות העונה חתוכים גס",19,10,true,true,null));
        dishArrayList.add(new Dish("שרימפס חמאה ושום","מנת שרימפס קלאסי עם רוטב מנצח",46.90,9,true,true,null));

        dishRowListAdapter = new DishRowListAdapter();
        dishesListView = (ListView)getActivity().findViewById(R.id.add_event_fragment_list_view);
        dishesListView.setAdapter(dishRowListAdapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.chef_fragment_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    private void buildAddDishDiaglog()
    {
        addDishDialog = new Dialog(getActivity());
        addDishDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addDishDialog.setContentView(R.layout.add_event_dialog);
        addDishTitleEditText = (EditText)addDishDialog.findViewById(R.id.add_dish_dialog_title_edit_text);
        addDishPriceEditText = (EditText)addDishDialog.findViewById(R.id.add_dish_dialog_price_edit_text);
        addDishDishesLeftEditText = (EditText)addDishDialog.findViewById(R.id.add_dish_dialog_dishes_left_edit_text);
        addDishDescriptionEditText = (EditText)addDishDialog.findViewById(R.id.add_dish_dialog_decription_edit_text);
        addDishImageView = (ImageView)addDishDialog.findViewById(R.id.add_dish_dialog_image_view);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.chef_fragment_menu_add_button)
        {
            addDishDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            eventImageView.setImageBitmap(imageBitmap);
        }
    }


}