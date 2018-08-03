package com.medi360.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.medi360.R;

/**
 * Created by MysteryMachine on 4/3/2016.
 */
public class Utils  {

    //singleton method
    private static Utils instance = null;
    public static Utils getInstance() {
        if(instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    //Create date picker for the given EdiText
    public static DatePickerDialog createDatePicker(Context context, final String dateFormat,final EditText fieldToUpdate){

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fieldToUpdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return  datePicker;

    }
    public  void showCustomToast(Context context,String errorText){
        LayoutInflater li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ); //getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View view = li.inflate( R.layout.customtoast_red, null );

        View layout = view.findViewById(R.id.custom_toast_red_layout);

        TextView text = (TextView) layout.findViewById(R.id.toast_text_invalid);
        text.setText(errorText);

        //Creating the Toast object
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 10, 10);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }

    //creat Time picker for the specific field
    public static TimePickerDialog createTimePicker(Context context,final EditText fieldToUpdate){

        //Get the current Time from the calender class
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String apm = (selectedHour > 12)? " PM" : " AM";
                fieldToUpdate.setText( selectedHour + ":" + selectedMinute + apm);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        return mTimePicker;
    }

    //show the toast message
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,300).show();
    }


}
