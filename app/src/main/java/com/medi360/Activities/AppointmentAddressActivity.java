package com.medi360.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.ScheduleAppointmentData;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Configuration;
import com.medi360.utils.Utils;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vijaya_ on 6/28/2016.
 */
public class AppointmentAddressActivity  extends AppCompatActivity {

    RequestParams requestParams = new RequestParams();
    Spinner genderSpinner;
    Spinner citySpinner;
    EditText firstNameEditText,lastNameEditText,addressLine2EditText,pincodeEditText,emailEditText,mobileNoEditText,genderEditText;
    TextView addressLine1EditText;
    Utils utilsObj = new Utils();
    EditText stateEditText,countryEdittext;
    Button doneBtn;
    List<String> genderSpinnerList = new ArrayList<String>();
    List<String> citySpinnerList = new ArrayList<String>();
    List<Integer> cityIDList = new ArrayList<Integer>();
    public static final String PREFS_NAME = "MediAuthUser";
    int selectedSex;
    Integer selectedCity;
    Boolean isCityFirstTime = true;
    ScheduleAppointmentData scheduleAppointmentData;// = new ScheduleAppointmentData();

    GlobalValues globalParams = new GlobalValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_address);
        scheduleAppointmentData = GlobalValues.getScheduleAppointmentData();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.backbut);
        upArrow.setColorFilter(getResources().getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        System.out.println("SELETECTED TIM<E : : " + scheduleAppointmentData.getTime());
        System.out.println("SELETECTED RoleID : : " + scheduleAppointmentData.getRoleID());
        System.out.println("SELETECTED Doctorname : : " + scheduleAppointmentData.getDoctorname());
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        loggedUser = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Log.v("LoggedUserDetails","=====>"+settings.getString("LoggedUserDetails","").toString());
       // addressData();
        initializeAll();

    }


    public void initializeAll(){
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        firstNameEditText=(EditText) findViewById(R.id.firstname_txt_fld);
        lastNameEditText=(EditText) findViewById(R.id.last_name_txt_fld);
        addressLine1EditText=(TextView) findViewById(R.id.address_line1);
        addressLine2EditText=(EditText) findViewById(R.id.address_line2);
        pincodeEditText=(EditText) findViewById(R.id.pincode_txt_fld);
        emailEditText=(EditText) findViewById(R.id.email_txt_fld);
        mobileNoEditText=(EditText) findViewById(R.id.mobile_no_txt_fld);
        genderEditText = (EditText) findViewById(R.id.gender_edit_txt);

        stateEditText = (EditText) findViewById(R.id.state_name_edit_txt);
        countryEdittext = (EditText) findViewById(R.id.country_name_edit_txt);
//        genderSpinner = (Spinner)findViewById(R.id.gender_spinner);
        citySpinner = (Spinner)findViewById(R.id.district_spinner);
        doneBtn = (Button)findViewById(R.id.done_btn);
//        genderSpinnerList.add("Male");
//        genderSpinnerList.add("Female");
//        setSpinner(genderSpinnerList, genderSpinner);
        setSpinner(citySpinnerList, citySpinner);
        firstNameEditText.setEnabled(false);
        lastNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        mobileNoEditText.setEnabled(false);
        genderEditText.setEnabled(false);
       /* stateEditText.setEnabled(false);
        countryEdittext.setEnabled(false);*/
//        genderSpinner.setEnabled(false);
       /* citySpinner.setEnabled(false);*/
      // firstNameEditText.setEnabled(false);
        //addressLine1EditText.setInputType(InputType.TYPE_NULL);

        addressLine2EditText.setVisibility(View.GONE);
        pincodeEditText.setVisibility(View.GONE);
                citySpinner.setVisibility(View.GONE);
        countryEdittext.setVisibility(View.GONE);
                citySpinner.setVisibility(View.GONE);
        stateEditText.setVisibility(View.GONE);
//        emailEditText.setVisibility(View.GONE);
//        mobileNoEditText.setVisibility(View.GONE);

        addressLine1EditText.setText(loggedUser.getString("Addressline1",""));
        addressLine1EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_openmap();
            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressLine1EditText.getText().toString().equals("")){
                    utilsObj.showCustomToast(getApplicationContext(), "Please select your address");
                }else{
                    updateProfile();
                }

            }
        });
      //  setSpinnerListener();
        initializeFromLocal();


    }
    public void initializeFromLocal(){
        try{
//            SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
            Log.v("Firstname", "FirstName" + loggedUser.getString("FirstName", ""));
            firstNameEditText.setText(loggedUser.getString("FirstName",""));
            lastNameEditText.setText(loggedUser.getString("LastName",""));
         //   SharedPreferences mapadd = getSharedPreferences(PREFS_NAME, 0);
            //String mapped=loggedUser.getString("selectedaddress","");

            String genderVal = loggedUser.getString("Gender","");
            if(genderVal.contains("0") || genderVal.contains("M")){
                genderEditText.setText("Male");
            }else{
                genderEditText.setText("Female");
            }

//            genderEditText.setText(loggedUser.getString("Gender",""));
//            Gender

            //addressLine1EditText.setText(loggedUser.getString("Addressline1",""));
            addressLine2EditText.setText(loggedUser.getString("Addressline2",""));
            pincodeEditText.setText(loggedUser.getString("Zipcode",""));
            emailEditText.setText(loggedUser.getString("Email",""));
            mobileNoEditText.setText(loggedUser.getString("ContactNo",""));
            selectedCity = loggedUser.getInt("CityId",0);
            Log.v("selectedCity","selectedCity=>"+selectedCity);
//            setStateAndCountyValue();

            System.out.println("SELECTED ADDRESSSSSSSSSS");
            System.out.println(globalParams.getSelectedAddress());
            if(globalParams.getSelectedAddress()!=null && !globalParams.getSelectedAddress().equals("") ){
                addressLine1EditText.setHint("Address");
                addressLine1EditText.setText(globalParams.getSelectedAddress().toString());
            }
//            else{
//                int profileCity = loggedUser.getInt("CityId",0);
//                Log.v("setStateAndCountyValue","setStateAndCountyValue=>"+profileCity);
//                String cityStateCountryVal = "";
//                Cursor cityCursor = Configuration.getDatabase().rawQuery("SELECT * FROM mastercity WHERE cityid=? ", new String[]{profileCity+""});
//                if(cityCursor.getCount()>0){
//                    cityCursor.moveToFirst();
//                    int valuestate = cityCursor.getInt(cityCursor.getColumnIndex("stateid"));
//                    cityStateCountryVal = cityStateCountryVal + cityCursor.getString(cityCursor.getColumnIndex("cityname"));
//                    Log.d("valuestate ","valuestate =>"+valuestate);
//                    Cursor stateCursor = Configuration.getDatabase().rawQuery("SELECT * FROM masterstate where stateid="+valuestate,null);
//                    if(stateCursor.getCount()>0){
//                        stateCursor.moveToFirst();
//                        String stateName = stateCursor.getString(stateCursor.getColumnIndex("statename"));
//                        Log.d("valuestate ","stateName =>"+stateName);
//                        cityStateCountryVal = cityStateCountryVal + stateName;
//                        int valuecountry= stateCursor.getInt(stateCursor.getColumnIndex("countryid"));
//                        Cursor countryCursor = Configuration.getDatabase().rawQuery("SELECT * FROM mastercountry where Countryid="+valuecountry,null);
//                        if(countryCursor.getCount()>0){
//                            countryCursor.moveToFirst();
//                            String countryName = countryCursor.getString(countryCursor.getColumnIndex("countryname"));
//                            Log.d("valuestate ","countryName =>"+countryName);
////                            countryEdittext.setText(countryName);
//                            cityStateCountryVal = cityStateCountryVal + countryName;
//                        }
//
//                    }
//                }
//                String profileAddress = loggedUser.getString("Addressline1","") + " " + loggedUser.getString("Addressline2","") + " " + cityStateCountryVal + " PIN " + loggedUser.getString("Zipcode","");
//                addressLine1EditText.setText(globalParams.getSelectedAddress().toString());
//            }
//            editor.putString("Patientname",userDtails.getString("Patientname"));
//            editor.putInt("Patid",userDtails.getInt("Patid"));
//            editor.putString("RegId",usernameEditText.getText().toString() );
//            //editor.putString("hospital",hospital.getText().toString());
//            editor.putString("FirstName",userDtails.getString("FirstName"));
//            editor.putString("LastName",userDtails.getString("LastName"));
//            editor.putString("Email",userDtails.getString("Email"));
//            editor.putString("ContactNo",userDtails.getString("ContactNo"));
//            editor.putString("Addressline1",userDtails.getString("Addressline1"));
//            editor.putString("Addressline2",userDtails.getString("Addressline2"));
//            editor.putString("Zipcode",userDtails.getString("Zipcode"));
//            editor.putString("Username",userDtails.getString("UserName"));
//            editor.putString("Password",userDtails.getString("Password"));
//            editor.putString("Gender",userDtails.getString("Sex"));
//            editor.putInt("CityId",(userDtails.getInt("CityID")));
        }catch (Exception e){
            Log.v("Excepyion","Exception==>"+e.getMessage());
            System.out.println(e);
        }
    }



public void btn_openmap( ){

finish();
    Intent map=new Intent(AppointmentAddressActivity.this,SelectAddressFromMapActivity.class);
    startActivity(map);

}

    public void setSpinnerListener(){
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isCityFirstTime) {
                    selectedCity = cityIDList.get(position);
                    setStateAndCountyValue();
                    isCityFirstTime = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isCityFirstTime) {
                    selectedSex = position;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setStateAndCountyValue(){
        Log.v("setStateAndCountyValue", "setStateAndCountyValue=>" + selectedCity);
        Cursor cityCursor = Configuration.getDatabase().rawQuery("SELECT * FROM mastercity WHERE cityid=? ", new String[]{selectedCity+""});
        if(cityCursor.getCount()>0){
            cityCursor.moveToFirst();
            int valuestate = cityCursor.getInt(cityCursor.getColumnIndex("stateid"));
            Log.d("valuestate ","valuestate =>"+valuestate);
            Cursor stateCursor = Configuration.getDatabase().rawQuery("SELECT * FROM masterstate where stateid="+valuestate,null);
            if(stateCursor.getCount()>0){
                stateCursor.moveToFirst();
                String stateName = stateCursor.getString(stateCursor.getColumnIndex("statename"));
                Log.d("valuestate ","stateName =>"+stateName);
                stateEditText.setText(stateName);
                int valuecountry= stateCursor.getInt(stateCursor.getColumnIndex("countryid"));
                Cursor countryCursor = Configuration.getDatabase().rawQuery("SELECT * FROM mastercountry where Countryid="+valuecountry,null);
                if(countryCursor.getCount()>0){
                    countryCursor.moveToFirst();
                    String countryName = countryCursor.getString(countryCursor.getColumnIndex("countryname"));
                    Log.d("valuestate ","countryName =>"+countryName);
                    countryEdittext.setText(countryName);
                }

            }
        }
    }
    public void setSpinner(List<String> spinnerData, Spinner spinner){
        // Spinner Drop down elements


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }
    public void addressData(){
        Configuration.InitializeDatabase(this.getApplicationContext());
        Cursor c = Configuration.getDatabase().rawQuery("SELECT * FROM mastercity ORDER BY cityname ", null);
        String[] values = new String[100];
        List<String> senders = new ArrayList<String>();
        int i = 0;
        try {
            if (c.moveToFirst()) {
                do {
                    System.out.println("inside cursor " + c);
                    String value = c.getString(c.getColumnIndex("cityname"));
                    int cityId = c.getInt(c.getColumnIndex("cityid"));
                    // String cityid=c.getString(c.getColumnIndex("cityid"));
                    citySpinnerList.add(value);
                    cityIDList.add(cityId);
//                    System.out.println("valuse are  " + value);
                    i++;
                } while (c.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateProfile(){
        ApiSender apiSender = new ApiSender(AppointmentAddressActivity.this, new ApiCallback() {
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
                else if(response.contains("already") || response.contains("Already")) {
                    utilsObj.showCustomToast(getApplicationContext(),"An appointment has already been scheduled for this time slot. Please select another time slot.");

                }
                else{
                    //SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
                    //SharedPreferences.Editor editor=loggedUser.edit();
                    editor.putString("selectedaddress", "");
                    editor.putString("selectedaddlat", "");
                    editor.putString("selectedaddlong", "");
                    editor.commit();
                    utilsObj.showCustomToast(getApplicationContext(),"Appointment Scheduled Successfully");
                    finish();
                    Intent registerIntent = new Intent(AppointmentAddressActivity.this,DashboardActivity.class);
                    startActivity(registerIntent);
                }


//                if (responseObj.has("status") && responseObj.getBoolean("status")){
//
//                }else{
//                    Util.showAlert(Login.this, "Login", responseObj.getString("message"));
//                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate1 = sdf.format(new Date());

        //Build input params
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        RequestParams requestParams = new RequestParams();
        //requestParams.setPatientId(loggedUser.getInt("Patid", 0) + "");
        requestParams.setAddress1(addressLine1EditText.getText().toString());
        requestParams.setAddress2(addressLine2EditText.getText().toString());
        requestParams.setZipcode(pincodeEditText.getText().toString());

        requestParams.setPatientId(loggedUser.getString("RegId", ""));
        requestParams.setSpecialistId(scheduleAppointmentData.getSpecialistId());
        requestParams.setAppointmentDate(scheduleAppointmentData.getAvailDate());
        requestParams.setComplaint(scheduleAppointmentData.getComplaint());
        requestParams.setTime(scheduleAppointmentData.getTime());
        requestParams.setRoleID(scheduleAppointmentData.getRoleID());
        requestParams.setDoctorname(scheduleAppointmentData.getDoctorname());
        requestParams.setCreateddate(currentDate1);
        requestParams.setPatientname(loggedUser.getString("Patientname", ""));
        requestParams.setPatid(loggedUser.getInt("Patid", 0));
       requestParams.setCity(loggedUser.getInt("CityId", 0));
        requestParams.setLat(globalParams.getSelectedAddrLat());
        requestParams.setLongi(globalParams.getSelectedAddrLong());


        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"setappointment", "POST",inputParams};
        apiSender.execute(url);

    }
   /* public Barcode.GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new Barcode.GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));

            return p1;
        }catch (Exception e){

        }

    }*/
    @Override
    public void onBackPressed() {
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor=loggedUser.edit();
        editor.putString("selectedaddress", "");
        editor.putString("selectedaddlat", "");
        editor.putString("selectedaddlong", "");
        editor.commit();
        finish();

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


}
