package com.medi360.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.medi360.Adapter.PreviousAppintmentListAdapter;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.AppointmentData;
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
 * Created by Mohanraj.SK on 6/26/2016.
 */
public class PreviousAppointmentActivity extends AppCompatActivity {

    ListView previousAppointmentListVw;
    public static final String PREFS_NAME = "MediAuthUser";
    ArrayList<AppointmentData> appointmentListData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_appointment);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.backbut);
        upArrow.setColorFilter(getResources().getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        initializeAll();
    }


    public void initializeAll(){
        previousAppointmentListVw = (ListView)findViewById(R.id.apointment_list_list_vw);

        getPreviousAppointmentsFromServer();

    }
    public void getPreviousAppointmentsFromServer(){
//        getpatientprevhistory
        ApiSender apiSender = new ApiSender(PreviousAppointmentActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = loggedUser.edit();

                if(response.contains("Invalid")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Invalid Username/Password");
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{
                   // String temp[]={};
                    ArrayList<String> stringArraystatus = new ArrayList<String>();
                    ArrayList<String> stringArrayapptime = new ArrayList<String>();
                    try{
                        JSONArray appointmentListAry = new JSONArray(response);
                        appointmentListData.clear();
                        for(int i=0;i<appointmentListAry.length();i++){
                            AppointmentData appointmentData = new AppointmentData();
                            JSONObject singleAppointment = appointmentListAry.getJSONObject(i);
                            appointmentData.setAppointmentId(singleAppointment.get("AppointmentID") + "");
                            appointmentData.setComplaintText(singleAppointment.getString("Complaint"));
                            appointmentData.setAppointmentStatus(singleAppointment.get("ApptStatus") + "");
                            appointmentData.setDoctorName(singleAppointment.getString("Doctorname"));
                            stringArraystatus.add(singleAppointment.getString("AppointmentDate"));
                            appointmentData.setAppointmentTime(singleAppointment.getString("AppointmentTime"));
                            appointmentData.setComplaintid(singleAppointment.getString("ComplaintID"));
                            appointmentData.setSpecialistID(singleAppointment.getString("SpecialistID"));
                            System.out.println("SpecialistID : "+singleAppointment.getString("SpecialistID"));
                            //stringArrayapptime.add(singleAppointment.getString("timeinAMPM"));
                            //String temptime[]=stringArrayapptime.toArray(new String[stringArraystatus.size()]);
                            String apptime=singleAppointment.getString("timeinAMPM");
                            String[] convertedTime = apptime.split(" ");
                            System.out.println("UTC TIME : "+ singleAppointment.getString("AppointmentTime"));
                            System.out.println("CONVERTED TIME : "+ singleAppointment.getString("timeinAMPM"));
                            System.out.println("time : " + convertedTime[0] + " ampm : "+ convertedTime[1]);
                            appointmentData.setAppointmentTime(convertedTime[0]);
                            appointmentData.setAm(convertedTime[1]);




                            String temp[]=stringArraystatus.toArray(new String[stringArraystatus.size()]);
                           // for(int m=0;i==temp.length;m++){
                                String tempall=temp[i];
                                String temp25=tempall.substring(tempall.indexOf("(") + 1,tempall.indexOf(")"));
                                System.out.println("sub strng "+temp25);
                                long l = Long.parseLong(temp25);
                                appointmentData.setDate(getDateCurrentdateZone(l));
                              //  appointmentData.setAppointmentTime(getDateCurrentTimeZone(l));
                                appointmentData.setGetAppointmentDay(getDateCurrentdayZone(l));
//                            appointmentData.setAm(getDateCurrentamZone(l));
                           // }
                           //// appointmentData.setDate(singleAppointment.getString("AppointmentDate"));
                            appointmentListData.add(appointmentData);
                        }
                        PreviousAppintmentListAdapter adapter = new PreviousAppintmentListAdapter(PreviousAppointmentActivity.this,appointmentListData);
                        previousAppointmentListVw.setAdapter(adapter);
                        previousAppointmentListVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                GlobalValues.setSelectedAppointmentDetails(appointmentListData.get(position));
                                Intent intent = new Intent(PreviousAppointmentActivity.this, AppointmentDetailsActivity.class);
                                startActivity(intent);

                            }
                        });

                    }catch(Exception e){


                        System.out.println(" Exception  "+e );
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


        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        RequestParams requestParams = new RequestParams();
        requestParams.setPatientId(loggedUser.getString("RegId", ""));
        requestParams.setOffsetvalue("-"+String.valueOf(offvalue));

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"getpatientprevhistory", "POST",inputParams};
        apiSender.execute(url);
    }

    public String setTimeFormat(String timeObj){
        String res = "";
        String[] timeArryObj = timeObj.split(":");
        int hour = Integer.parseInt(timeArryObj[0]);
        int mins = Integer.parseInt(timeArryObj[1]);
        int hoursInMins = hour * 60;
        int totalmin=hoursInMins+mins;
        TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
        int offvalue = tz.getOffset(new Date().getTime()) / 1000 / 60;
        int totaltime=totalmin + offvalue;


        int convertedHour = totaltime / 60; //since both are ints, you get an int
        int convertedMin = totaltime % 60;
        System.out.println("timeObj : " + timeObj);
        System.out.println("convertedHour : " + convertedHour);
        System.out.println("convertedMin : " + convertedMin);

        String ampmVal = "AM";
        if(convertedHour>12){
            if(convertedHour==24){
                ampmVal = "AM";
            }else{
                ampmVal = "PM";
            }
            convertedHour = convertedHour-12;

            if(convertedHour>12){
                convertedHour = convertedHour-12;
            }

        }

        String hoursStrNew=String.valueOf(convertedHour), minStrNew=String.valueOf(convertedMin);


        if(convertedHour<10){
            hoursStrNew = "0"+ String.valueOf(convertedHour);
        }
        if(convertedMin<10){
            minStrNew = "0"+ String.valueOf(convertedMin);
        }

        res = hoursStrNew + ":" + minStrNew + " " + ampmVal;
        System.out.println("FINAL CONVERTED TIME : " + res);
        return res;
    }
public String getlocal(String apptime){
    String con="";
    try {
        String[] hourMin = apptime.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        int totalmin=hoursInMins+mins;
        System.out.println("before : "+apptime);
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        Date date = sdf.parse(""+apptime);
        TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
        int offvalue = tz.getOffset(new Date().getTime()) / 1000 / 60;
        //System.out.println("offvalue :" + offvalue);
        int totaltime=totalmin + offvalue;
        int hours = totaltime / 60; //since both are ints, you get an int
        int minutes = totaltime % 60;
        System.out.println("AFTER : "+hours+":"+minutes+":"+"00");
        String ampmVal = "AM";

        if(hours>12){
            hours = hours-12;
            if(hours>12){
                hours = hours - 12;
            }
            ampmVal = "PM";
        }
        String hoursStr = String.valueOf(hours);
        String minsStr = String.valueOf(minutes);

        if(hours<10){
            hoursStr = "0"+hours;
        }
        if(minutes<10){
            minsStr = "0"+minutes;
        }
        con = hoursStr+":"+minsStr+" "+ampmVal;
        System.out.println("FINAL CONVERTED TIME : " + con);
        return con;
//        System.out.printf("time by calcllating   %d:%02d", hours, minutes);
//        String minutesStr = String.valueOf(minutes);
//        System.out.println(sdf.format(date));
//        String temptime=String.valueOf(hours)+":"+minutesStr;
//        if(minutes<10)
//            minutesStr = "0"+minutesStr;
//     temptime=String.valueOf(hours)+":"+minutesStr;
//        if(hours>12) {
//
//            int temphour=hours-12;
//            if(temphour>12)
//                temphour = temphour-12;
//
//            String tempHourStr = String.valueOf(temphour);
//            if(temphour<10)
//                tempHourStr = "0"+tempHourStr;
//
//
//
//            temptime=tempHourStr+":"+minutesStr+" "+"PM";
//
//                    /*final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//                    final Date dateObj = sdf2.parse(temptime);
//                    System.out.println(dateObj);
//                    System.out.println("------------------------------------------------------------");
//                    System.out.println(new SimpleDateFormat("KK:mm").format(dateObj));
//                    result=new SimpleDateFormat("KK:mm").format(dateObj)+" "+"PM";*/
//            System.out.println("------------------------------------------------------------");
//            System.out.println("result : "+temptime);
//            return temptime;
//
//        }
//        con=sdf.format(date);
//        return temptime;
    } catch(Exception e){
System.out.println(" Exception "+e);
        return con;
    }

}

    public  String getDateCurrentdateZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time= new SimpleDateFormat(" HH:mm aa");
            SimpleDateFormat day = new SimpleDateFormat("EEEE");

            Date currenTimeZone = (Date) calendar.getTime();
            //System.out.println("datestamp" +time.format(timestamp));
            return date.format(timestamp);
        }catch (Exception e) {
        }
        return "";
    }





    public  String getDateCurrentamZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            //  SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time= new SimpleDateFormat("aa");
            // SimpleDateFormat day = new SimpleDateFormat("EEEE");

            // Date currenTimeZone = (Date) calendar.getTime();
            System.out.println("timestamp" +time.format(timestamp));
            return time.format(timestamp);
        }catch (Exception e) {
        }
        return "";
    }





    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
          //  SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time= new SimpleDateFormat(" HH:mm");
           // SimpleDateFormat day = new SimpleDateFormat("EEEE");

           // Date currenTimeZone = (Date) calendar.getTime();
            System.out.println("timestamp" +time.format(timestamp));
            return time.format(timestamp);
        }catch (Exception e) {
        }
        return "";
    }
    public  String getDateCurrentdayZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time= new SimpleDateFormat(" HH:mm aa");
            SimpleDateFormat day = new SimpleDateFormat("EEEE");

            Date currenTimeZone = (Date) calendar.getTime();
            //System.out.println("daystamp" +time.format(timestamp));
            return day.format(timestamp);
        }catch (Exception e) {
        }
        return "";
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

    @Override
    public void onResume(){
        super.onResume();
        appointmentListData.clear();
        initializeAll();
    }
}