package com.medi360.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.DoctorData;
import com.medi360.Model.ScheduleAppointmentData;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mohanraj.SK on 6/27/2016.
 */
public class ScheduleAppointmentActivity extends AppCompatActivity {

    Utils utilsObj = new Utils();
    private Button searchAvlSlotBtn, dontBtn,searchdoctor;
    private LinearLayout doctors_availablity_vw, done_vw;
    RequestParams requestParams = new RequestParams();
    ScheduleAppointmentData scheduleAppointmentData = new ScheduleAppointmentData();
    private EditText complaint_txt_fld;
    TextView date,noslots,nodoctors;
    ArrayList<DoctorData> doctorDataAry = new ArrayList<>();
    public static final String PREFS_NAME = "MediAuthUser";
    DoctorData selectoedDctor;
    ArrayList<String> doctorNameList = new ArrayList<>();
    ArrayList<String> availableSlots = new ArrayList<>();
    TextView doctorListTextvw,slotListTxtVw;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    boolean timeflag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_appointments_activity);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.backbut);
        upArrow.setColorFilter(getResources().getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        initializeAll();
    }

    public void initializeAll() {
        searchAvlSlotBtn = (Button) findViewById(R.id.searchAvlSlotBtn);
        dontBtn = (Button) findViewById(R.id.dontBtn);
        doctors_availablity_vw = (LinearLayout) findViewById(R.id.doctors_availablity_vw);
        done_vw = (LinearLayout) findViewById(R.id.done_vw);
        complaint_txt_fld = (EditText) findViewById(R.id.complaint_txt_fld);
        doctorListTextvw = (TextView) findViewById(R.id.doctor_list_textvw);
        date=(TextView) findViewById(R.id.datevw);
        noslots=(TextView) findViewById(R.id.noslot);
        nodoctors=(TextView) findViewById(R.id.nodoctor);
        slotListTxtVw = (TextView) findViewById(R.id.slot_list_txt_vw);
        searchdoctor= (Button) findViewById(R.id.searchAvlDoctor);
       // doctors_availablity_vw.setVisibility(View.GONE);
        searchAvlSlotBtn.setVisibility(View.GONE);
        date.setVisibility(View.VISIBLE);
        setCurrentDateOnView();

        searchAvlSlotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                slotListTxtVw.setText("");
                String doctorname = doctorListTextvw.getText().toString();
                if (doctorname.equals("") || doctorname.equals(" ")) {
                    utilsObj.showCustomToast(getApplicationContext(), "Please select a Doctor!");
                } else {

                    searchAndAvailableSlots();
                }

            }
        });

//        fixAppointmentBtn.setOnClickListener(this);
        searchdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                String selecteddate = date.getText().toString();
                System.out.println("selected date is:"+selecteddate);
                selectoedDctor = null;
                doctorListTextvw.setText("");
                done_vw.setVisibility(View.GONE);
                slotListTxtVw.setText("");
                if (selecteddate.equals("") || selecteddate.equals(" ")) {
                    utilsObj.showCustomToast(getApplicationContext(), "Please select a Doctor!");
                } else {
                    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = df.format(new Date());
                    System.out.println("CURRENT DATE : " + currentDate);
                    try {
                        boolean checkdate = compareDatesByCompareTo(df, df.parse(selecteddate), df.parse(currentDate));
                        if (checkdate) {
                            getDoctorsFromServer();
                        } else {
                            nodoctors.setVisibility(View.GONE);
                            doctorListTextvw.setVisibility(View.GONE);
                            searchAvlSlotBtn.setVisibility(View.GONE);
                            if(timeflag) {
                                utilsObj.showCustomToast(getApplicationContext(), "SELECTED DATE SHOULD BE GREATER THAN " + currentDate);
                            }
                            else {
                                utilsObj.showCustomToast(getApplicationContext(), " BOOKING TIME CLOSED");
                                timeflag = true;
                            }
                        }
                    } catch (Exception err) {
                        System.out.println("Inside catch");
                        System.out.println(err);
                    }
                }

            }
        });


    }

    public boolean compareDatesByCompareTo(DateFormat df, Date selecteddate, Date currentdate) {
        System.out.println("compare selected date is:"+selecteddate);
        System.out.println("COMPARE RESULT : " + selecteddate.compareTo(currentdate));
        boolean dateflag=false;
        Date nextdate=getNextDate(currentdate);
        Date date=new Date();
        int h=date.getHours();
        System.out.println("selected date is:" + nextdate + " and hour is:" + h);
        if(selecteddate.compareTo(currentdate)<= 0)
        {
            dateflag=false;
        }
        else if(selecteddate.compareTo(nextdate)==0) {
            if (h >= 15) {
                dateflag = false;
                timeflag=false;
            } else {
                dateflag = true;
                //timeflag=true;
            }
            //return dateflag;
        }
        else if (selecteddate.compareTo(nextdate) > 0) {
            //System.out.println(df.format(selecteddate) + " is less than " + df.format(currentdate));
            dateflag=true;
            //return dateflag;
        }
        return dateflag;
        //return false;
    }
    public Date getNextDate(Date currentdate)
    {
        //DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date nextdate=null;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentdate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println("calendar next date is: "+calendar.getTime());
        String formatteddate=df.format(calendar.getTime());
        try {
            nextdate=df.parse(formatteddate);
        }catch (Exception e)
        {
            System.out.println("format error");
        }
        return nextdate;
        /*String formatteddate=df.format(new Date(calendar.getTime().toString()));
        Date nextdate=new Date(formatteddate);
        System.out.println("next date is: "+nextdate);*/
        //return nextdate;
        /*Date nextdate=new Date(currentdate.getDate()+1);
        return nextdate;*/
    }
    public void hideKeyBoard(){

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void getDoctorsFromServer(){

        ApiSender apiSender = new ApiSender(ScheduleAppointmentActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);


                if(response.contains("No records found.")) {
                  //  utilsObj.showCustomToast(getApplicationContext(),"Invalid Username/Password");
                    nodoctors.setVisibility(View.VISIBLE);
                    doctorListTextvw.setVisibility(View.GONE);
                    searchAvlSlotBtn.setVisibility(View.GONE);
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{

                    nodoctors.setVisibility(View.GONE);
                    doctorListTextvw.setVisibility(View.VISIBLE);
                 //   searchAvlSlotBtn.setVisibility(View.VISIBLE);
                    doctorNameList.clear();
                    try{
                        JSONArray doctorJsonAry = new JSONArray(response);
                        for(int i=0;i<doctorJsonAry.length();i++){
                            DoctorData doctorData = new DoctorData();
                            JSONObject singleDoctorDetails = doctorJsonAry.getJSONObject(i);
                            doctorData.setDoctorId(singleDoctorDetails.get("SpecialistID") + "");
                            doctorData.setDoctorName(singleDoctorDetails.getString("SpecialistName"));
                            doctorData.setRoleID(singleDoctorDetails.get("RoleID")+"");
//                            doctorData.setRoleID(singleDoctorDetails.get("SpecialistID")+"");
                            doctorNameList.add(singleDoctorDetails.getString("SpecialistName"));

                            doctorDataAry.add(doctorData);
                        }

                        doctorListTextvw.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDoctorChoice();
                            }
                        });
                    }catch(Exception e){
                        System.out.println("EXCEPTIP");
                        System.out.println(e);
                    }
                }


//                if (responseObj.has("status") && responseObj.getBoolean("status")){
//
//                }else{
//                    Util.showAlert(Login.this, "Login", responseObj.getString("message"));
//                }
            }
        });

        //Build input params
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        RequestParams requestParams = new RequestParams();
        requestParams.setPatientId(String.valueOf(loggedUser.getInt("Patid", 0)));
        requestParams.setAppointmentDate(date.getText().toString());

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"getHospAvailDoctors", "POST",inputParams};
        apiSender.execute(url);
//
    }
    public void searchAndAvailableSlots(){

        ApiSender apiSender = new ApiSender(ScheduleAppointmentActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {

                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);


                if(response.contains("No records found.")) {
                    //utilsObj.showCustomToast(getApplicationContext(),"Invalid Username/Password");
                    noslots.setVisibility(View.VISIBLE);
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{

                    try{
                        JSONArray searchAvailAry = new JSONArray(response);
                        ArrayList<String> stringArrayapptime = new ArrayList<String>();
                        noslots.setVisibility(View.GONE);

                        availableSlots.clear();
                        for(int i=0;i<searchAvailAry.length();i++){

//                            DoctorData doctorData = new DoctorData();
                            JSONObject singleAvailableSlot = searchAvailAry.getJSONObject(i);
                            //   availableSlots.add(singleAvailableSlot.getString("AvailabilityTime"));
                            System.out.println("RESPONE DATA :: " + singleAvailableSlot.get("timeinAMPM").toString());
                            if(singleAvailableSlot.get("timeinAMPM")!=null&&!singleAvailableSlot.get("timeinAMPM").toString().equals("null")){
//                                stringArrayapptime.add(singleAvailableSlot.getString("timeinAMPM"));
//                                String temptime[]=stringArrayapptime.toArray(new String[stringArrayapptime.size()]);
//                                String apptime=temptime[i];
//                                System.out.println("NEWWWWWWW");

                                availableSlots.add(singleAvailableSlot.getString("timeinAMPM"));
//                                availableSlots.add(setFormatAndOrderingTime(apptime));
                            }

                           // availableSlots.add(singleAvailableSlot.getString("AvailabilityTime"));
//                            doctorData.setDoctorId(singleDoctorDetails.get("SpecialistID")+"");
//                            doctorData.setDoctorName(singleDoctorDetails.getString("doctorname"));
//                            doctorData.setRoleID(singleDoctorDetails.get("RoleID")+"");

//                            doctorData.add(appointmentData);
                        }
                        Log.v("availableSlots.size","availableSlots===>availableSlots===>availableSlots===>availableSlots===>"+availableSlots.size());
                        if(availableSlots.size()<=0){
                            noslots.setVisibility(View.VISIBLE);
                            utilsObj.showCustomToast(getApplicationContext(),"No slots available");
                        }else{
                            done_vw.setVisibility(View.VISIBLE);
                            slotListTxtVw.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    availableSlotsShow();
                                }
                            });

                        }

                    }catch(Exception e){
                            Log.v("Exception.,======>",e.getMessage());
                    }
                }


//                if (responseObj.has("status") && responseObj.getBoolean("status")){
//
//                }else{
//                    Util.showAlert(Login.this, "Login", responseObj.getString("message"));
//                }
            }
        });

        //Build input params
        TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
        int offvalue = tz.getOffset(new Date().getTime()) / 1000 / 60;

        String selectedDateVal = date.getText().toString();
        String[] dateObj = selectedDateVal.split("-");
        String year = dateObj[0], month = dateObj[1], date=dateObj[2];

        selectedDateVal = date+"/"+month+"/"+year;

        System.out.println("offset VALE : " + offvalue);

        //Build input params
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);

        RequestParams requestParams = new RequestParams();
        requestParams.setSpecialistId(selectoedDctor.getDoctorId());
        requestParams.setAvailDate(selectedDateVal);
        requestParams.setOffsetvalue("-"+String.valueOf(offvalue));

        scheduleAppointmentData.setSpecialistId(selectoedDctor.getDoctorId());



        System.out.println("SELECTED DATE " + selectedDateVal);

        scheduleAppointmentData.setAvailDate(selectedDateVal);
        scheduleAppointmentData.setDoctorname(selectoedDctor.getDoctorName());
        scheduleAppointmentData.setRoleID(selectoedDctor.getRoleID());

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"getdoctoravail", "POST",inputParams};
        apiSender.execute(url);
//        getdoctoravail
    }

    public String setFormatAndOrderingTime(String timeVal){
        String result = timeVal;
        String[] hourMin = timeVal.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        String tempmins = String.valueOf(hourMin[1]);

        String[] mina = tempmins.split(" ");
        int mins=Integer.parseInt(mina[0]);
        String ampmVal = mina[1];

//        try{
//            String str = "08:03:10 pm";
//
//        DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
//        Date date = formatter.parse(timeVal);
//        }
//    } catch (Exception e) {
//            System.out.println("Exception : " + e.getMessage());
//        }
//        try {
//            String now = new SimpleDateFormat("hh:mm aa").format(new timeVal);
//            System.out.println("time in 12 hour format : " + now);
//            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
//            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
//            String time24 = outFormat.format(inFormat.parse(now));
//            System.out.println("time in 24 hour format : " + time24);
//        } catch (Exception e) {
//            System.out.println("Exception : " + e.getMessage());
//        }


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,17);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        Date d = cal.getTime();
        System.out.println("DATEEEE : " + d);


        //Date date = new Date();

//        //formatting time to have AM/PM text using 'a' format
//        String strDateFormat = "HH:mm:ss a";
//        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        // System.out.println("Time with AM/PM field : " + sdf.format(date));
//        System.out.println("BEFORE ORDERING " + timeVal);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        String currentDate1 = sdf.format(new Date());
//        //String dtStart = currentDate1+"T"+timeVal;
//        String dtStart = "06/07/2016T22:05:00 PM";
//        SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss a");
//        try {
//            Date date = format.parse(dtStart);
//            System.out.println("CONVERTERD TIME");
//            System.out.println(date);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return result;
    }




    public String getlocal(String apptime){
        String con="";
        try {
            String[] hourMin = apptime.split(":");
            int hour = Integer.parseInt(hourMin[0]);
            String tempmins = String.valueOf(hourMin[1]);

            String[] mina = tempmins.split(" ");
            int mins=Integer.parseInt(mina[0]);
            String aa=mina[1];

            int hoursInMins = hour * 60;
            int totalmin=hoursInMins+mins;
            System.out.println("before : "+apptime);
            DateFormat sdf = new SimpleDateFormat("H:mm a");

            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            Date date = sdf.parse(""+apptime);
            TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
            int offvalue = tz.getOffset(new Date().getTime()) / 1000 / 60;
            //System.out.println("offvalue :" + offvalue);
            int totaltime=totalmin + offvalue;
            int hours = totaltime / 60; //since both are ints, you get an int
            int minutes = totaltime % 60;
            String ampmVal = mina[1];

            if(hours>=12){
                hours = hours - 12;
                if(hours>12){
                    hours = hours - 12;
                }
//                if(ampmVal.equals("AM"))
//                    ampmVal = "PM";
//                else
//                    ampmVal = "AM";
            }
            String hoursStr = String.valueOf(hours);
            String minStrs = String.valueOf(minutes);
            if(hours<10){
                hoursStr = "0"+hoursStr;
            }
            if(minutes<10){
                minStrs = "0"+minStrs;
            }

            con = hoursStr+":"+minStrs+" "+ampmVal;
            System.out.println("FINAL CONVERTED TIMEEEEEEE  : : : " + con);

            return con;

//            System.out.println("CONVERTED TIME : " + hours+":"+minutes+":00");
//
//            System.out.printf("time by calcllating   %d:%02d", hours, minutes);
//            String minutesStr = String.valueOf(minutes);
//            System.out.println(sdf.format(date));
//            String temptime=String.valueOf(hours)+":"+minutesStr;
//            if(minutes<10)
//                minutesStr = "0"+minutesStr;
//            temptime=String.valueOf(hours)+":"+minutesStr;
//            if(hours>12) {
//
//                int temphour=hours-12;
//                if(temphour>12)
//                    temphour = temphour-12;
//
//                String tempHourStr = String.valueOf(temphour);
//                if(temphour<10)
//                    tempHourStr = "0"+tempHourStr;
//
//
//
//                temptime=tempHourStr+":"+minutesStr+" "+"PM";
//
//                    /*final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//                    final Date dateObj = sdf2.parse(temptime);
//                    System.out.println(dateObj);
//                    System.out.println("------------------------------------------------------------");
//                    System.out.println(new SimpleDateFormat("KK:mm").format(dateObj));
//                    result=new SimpleDateFormat("KK:mm").format(dateObj)+" "+"PM";*/
//                System.out.println("------------------------------------------------------------");
//                System.out.println("result : "+temptime);
//                return temptime;
//
//            }
//            con=sdf.format(date);
//            return temptime;
        } catch(Exception e){
            System.out.println(" Exception "+e);
            return con;
        }

    }
    public void setDoctorChoice(){
        CharSequence[] doctorNamesCS = doctorNameList.toArray(new CharSequence[doctorNameList.size()]);
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(doctorNamesCS, 0, null)
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        selectoedDctor = doctorDataAry.get(selectedPosition);
                        doctorListTextvw.setText(doctorNameList.get(selectedPosition));
                        searchAndAvailableSlots();
                        // Do something useful withe the position of the selected radio button
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        // Do something useful withe the position of the selected radio button
                    }
                })
                .show();
    }
    public void availableSlotsShow(){

        CharSequence[] availableSlotCS = availableSlots.toArray(new CharSequence[availableSlots.size()]);
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(availableSlotCS, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();


                        slotListTxtVw.setText(availableSlots.get(selectedPosition));
                        scheduleAppointmentData.setTime(availableSlots.get(selectedPosition));
                        System.out.println("SELETECTED TIM<E : : " + scheduleAppointmentData.getTime());
                        // Do something useful withe the position of the selected radio button
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        // Do something useful withe the position of the selected radio button
                    }
                })
                .show();


        }


    public void btnfix(View view) {
        String timeslot = slotListTxtVw.getText().toString();
        if (complaint_txt_fld.getText().toString().equals("") || complaint_txt_fld.getText().toString().equals(" ") ) {
            utilsObj.showCustomToast(getApplicationContext(), "Complaint is required");
        }
        else if (timeslot.equals("") || timeslot.equals(" ")) {
            utilsObj.showCustomToast(getApplicationContext(), "Please select appointment time slot");
        }
       else {

            scheduleAppointmentData.setComplaint(complaint_txt_fld.getText().toString());
            GlobalValues.setScheduleAppointmentData(scheduleAppointmentData);
            GlobalValues.setSelectedAddress(null);
            Intent i=new Intent(this,AppointmentAddressActivity.class);
            //finish();
            startActivity(i);
        }


    }



    public void btncal_OnClick(View view) {
        showDialog(DATE_DIALOG_ID);
        /*showDialog(999);
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
                .show();*/
        // DatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }


    static final int DATE_DIALOG_ID = 999;
    private int myear;
    private int mmonth;
    private int mday;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date =   new DatePickerDialog(this, datePickerListener, myear,mmonth,
                        mday){
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                           /* if (year < cyear)
                                view.updateDate(cyear, mmonth, mday);

                            if (monthOfYear + 1 < cmonth && year >= cyear)
                                view.updateDate(myear, cmonth, mday);

                            if (dayOfMonth < cday && year >= myear && monthOfYear + 1 >= mmonth)
                                view.updateDate(myear, mmonth, cday);*/


                    }
                };

//                _date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
            System.out.println("mday  "+mday);
            int mday1=02;
            String temp2="";
            if(mday<10){
                String zero="0";
                String temp=String.valueOf(mday);
                temp2=zero+temp;
                // mday1=Integer.getInteger(temp2);
                System.out.println("mday1 "+mday1);
            }
            else{
                temp2=String.valueOf(mday);
            }
            int mplu=1;
            mplu=mmonth+1;
            String tempm="";
            if(mplu<10){

                String zerom="0";
                String temp1=String.valueOf(mplu);
                System.out.println("mmonth : "+mmonth);
                tempm=zerom+temp1;
                System.out.println("tempm:  "+tempm);
            }
            else{
                tempm=String.valueOf(mplu);
                System.out.println("else "+tempm);
            }
            // set selected date into textview
//            date.setText(new StringBuilder().append(temp2)
//                            .append("-").append(tempm).append("-").append(myear)
//            );

            date.setText(new StringBuilder().append(myear)
                    .append("-").append(tempm).append("-").append(temp2)

            );

        }
    };



    public void setCurrentDateOnView() {

        // date = (TextView) findViewById(R.id.tvDate);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        System.out.println("mday  "+mday);
        int mday1=02;
        String temp2="";
        if(mday<10){
            String zero="0";
            String temp=String.valueOf(mday);
            temp2=zero+temp;
            // mday1=Integer.getInteger(temp2);
            System.out.println("mday1 "+mday1);
        }
        else{
            temp2=String.valueOf(mday);
        }
        int mplu=1;
        mplu=mmonth+1;
        String tempm="";
        if(mplu<10){

            String zerom="0";
            String temp1=String.valueOf(mplu);
            System.out.println("mmonth : "+mmonth);
            tempm=zerom+temp1;
            System.out.println("tempm:  " + tempm);
        //    searchAndAvailableSlots();

        }
        else{
            tempm=String.valueOf(mplu);
            System.out.println("else "+tempm);
        }
        // set selected date into textview
        date.setText(new StringBuilder().append(myear)
                        .append("-").append(tempm).append("-").append(temp2)

        );
       // searchAndAvailableSlots();


/*
        // set current date into textview
        date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mday).append("/").append(mmonth + 1).append("/")
                .append(myear));
*/
    }

    @Override
    public void onBackPressed()
    {       System.out.println("i am on backkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        System.out.println(" inside back key");
        finish();
        //   System.exit(0);
        // super.onBackPressed(); Do not call me!

        // Go to the previous web page.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println(" inside home key");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public boolean compareDatesByCompareTo(DateFormat df, Date oldDate, Date newDate) {
        System.out.println("COMPARE RESULT : " + oldDate.compareTo(newDate));
        if (oldDate.compareTo(newDate) < 0) {
            System.out.println(df.format(oldDate) + " is less than " + df.format(newDate));
            return true;
        }
        return false;
    }*/

}
