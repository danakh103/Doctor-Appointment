package com.medi360.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Configuration;
import com.medi360.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohanraj.SK on 6/25/2016.
 */
public class ProfileAddressActivity extends AppCompatActivity {

    Boolean isCityFirstTime = true;
    Utils utilsObj = new Utils();
//    Spinner genderSpinner;
    Spinner citySpinner;
    EditText firstNameEditText,lastNameEditText,addressLine1EditText,addressLine2EditText,pincodeEditText,emailEditText,mobileNoEditText;
    EditText stateEditText,countryEdittext, genderEditText, patientID, genderTxtFld;
    Button doneBtn;
    List<String> genderSpinnerList = new ArrayList<String>();
    List<String> citySpinnerList = new ArrayList<String>();
    List<Integer> cityIDList = new ArrayList<Integer>();
    public static final String PREFS_NAME = "MediAuthUser";
    int selectedSex;

//    SharedPreferences loggedUser;//= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    Integer selectedCity;
    String selectedState,selectedCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.backbut);
        upArrow.setColorFilter(getResources().getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        loggedUser = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Log.v("LoggedUserDetails","=====>"+settings.getString("LoggedUserDetails","").toString());
        addressData();
        initializeAll();

    }

    public void initializeAll(){
        firstNameEditText=(EditText) findViewById(R.id.firstname_txt_fld);
        lastNameEditText=(EditText) findViewById(R.id.last_name_txt_fld);
        addressLine1EditText=(EditText) findViewById(R.id.address_line1);
        addressLine2EditText=(EditText) findViewById(R.id.address_line2);
        pincodeEditText=(EditText) findViewById(R.id.pincode_txt_fld);
        emailEditText=(EditText) findViewById(R.id.email_txt_fld);
        mobileNoEditText=(EditText) findViewById(R.id.mobile_no_txt_fld);

        stateEditText = (EditText) findViewById(R.id.state_name_edit_txt);
        countryEdittext = (EditText) findViewById(R.id.country_name_edit_txt);
//        genderSpinner = (Spinner)findViewById(R.id.gender_spinner);
        genderEditText = (EditText) findViewById(R.id.gender_spinner);
        genderTxtFld = (EditText) findViewById(R.id.genderTxtFld);
        citySpinner = (Spinner)findViewById(R.id.district_spinner);
        doneBtn = (Button)findViewById(R.id.done_btn);
        patientID = (EditText) findViewById(R.id.patientID);
        genderSpinnerList.add("Male");
        genderSpinnerList.add("Female");
//        setSpinner(genderSpinnerList, genderSpinner);
        setSpinner(citySpinnerList, citySpinner);
        firstNameEditText.setEnabled(false);
        lastNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        genderEditText.setEnabled(false);
        stateEditText.setEnabled(false);
        countryEdittext.setEnabled(false);
        patientID.setEnabled(false);
        genderTxtFld.setEnabled(false);
        genderEditText.setVisibility(View.GONE);
        /*firstNameEditText.setEnabled(false);
        firstNameEditText.setEnabled(false);*/






        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              System.out.println("mobileNoEditText : "+mobileNoEditText.getText().toString());
                // String doctorname = doctorListTextvw.getText().toString();
                if (    addressLine1EditText.getText().toString().equals("") || addressLine1EditText.getText().toString().equals(" ") ||

                        pincodeEditText.getText().toString().equals("") || pincodeEditText.getText().toString().equals(" ") ||
                        mobileNoEditText.getText().toString().equals("") || mobileNoEditText.getText().toString().equals(" ") ||
                        stateEditText.getText().toString().equals("") || stateEditText.getText().toString().equals(" ") ||
                        countryEdittext.getText().toString().equals("") || countryEdittext.getText().toString().equals(" ") ||
                            citySpinner.toString().equals("") || citySpinner.toString().equals(" ")) {
                    utilsObj.showCustomToast(getApplicationContext(), "Please fill all Details!");
                }
                else if(mobileNoEditText.getText().toString().length()<10) {
                    utilsObj.showCustomToast(getApplicationContext(), "Enter 10 digit phone number");
                }
                else  {

                    updateProfile();
                }
             //   updateProfile();
            }
        });
        setSpinnerListener();
        initializeFromLocal();


    }
    public void initializeFromLocal(){
        try{
//            SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
            Log.v("Firstname","FirstName"+loggedUser.getString("FirstName",""));
            firstNameEditText.setText(loggedUser.getString("FirstName", ""));
            lastNameEditText.setText(loggedUser.getString("LastName",""));
            addressLine1EditText.setText(loggedUser.getString("Addressline1",""));
            addressLine2EditText.setText(loggedUser.getString("Addressline2",""));
            patientID.setText(loggedUser.getString("RegId",""));
            pincodeEditText.setText(loggedUser.getString("Zipcode",""));
            emailEditText.setText(loggedUser.getString("Email",""));
            mobileNoEditText.setText(loggedUser.getString("ContactNo",""));
            selectedCity = loggedUser.getInt("CityId",0);
            String genderVal = loggedUser.getString("Gender","");
            if(genderVal.contains("0") || genderVal.contains("M")){
                selectedSex = 0;
                genderTxtFld.setText("Male");
            }else{
                selectedSex = 1;
                genderTxtFld.setText("Female");
            }

            System.out.println("selectedCity : "+selectedCity);
            Log.v("selectedCity", "selectedCity=>" + selectedCity);
            //

            int cityPosition = cityIDList.indexOf(selectedCity);
            citySpinner.setSelection(cityPosition);

//            setStateAndCountyValue();
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
        }
    }
    public void setSpinnerListener(){
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(!isCityFirstTime){
//                    selectedCity = cityIDList.get(position);
//                    setStateAndCountyValue();
//                    isCityFirstTime=false;
//                }
                selectedCity = cityIDList.get(position);
                System.out.println("selectedCity in spinner : "+selectedCity);
                setStateAndCountyValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(!isCityFirstTime){
//                    selectedSex = position;
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    public void setStateAndCountyValue(){
        Log.v("setStateAndCountyValue","setStateAndCountyValue=>"+selectedCity);
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
                    System.out.println("valuse are  " + value);
                    i++;
                } while (c.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateProfile(){
        ApiSender apiSender = new ApiSender(ProfileAddressActivity.this, new ApiCallback() {
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
                    SharedPreferences loggedUserNew = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editorNew =loggedUserNew.edit();
                    editorNew.putString("ContactNo",mobileNoEditText.getText().toString());
                    editorNew.putString("Addressline1",addressLine1EditText.getText().toString());
                    editorNew.putString("Addressline2",addressLine2EditText.getText().toString());
                    editorNew.putString("Zipcode",pincodeEditText.getText().toString());
                    editorNew.putInt("CityId", selectedCity);

                    System.out.println("selectedCity NEWWWWWWW : "+selectedCity);


                    editorNew.commit();
                    utilsObj.showCustomToast(getApplicationContext()," Profile Updated Successfully.");
                    finish();
                    Intent registerIntent = new Intent(ProfileAddressActivity.this,DashboardActivity.class);
                    startActivity(registerIntent);
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
        requestParams.setPatientId(loggedUser.getString("RegId",  ""));
        requestParams.setFirstname(firstNameEditText.getText().toString());
        requestParams.setLastname(lastNameEditText.getText().toString());
        requestParams.setSex(String.valueOf(selectedSex));
        requestParams.setAddress1(addressLine1EditText.getText().toString());
        requestParams.setAddress2(addressLine2EditText.getText().toString());
        requestParams.setCity(selectedCity);
        requestParams.setZipcode(pincodeEditText.getText().toString());
        requestParams.setEmail(emailEditText.getText().toString());
        requestParams.setPhoneno(mobileNoEditText.getText().toString());
        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"UpdateHospitalPatientProfile", "POST",inputParams};
        apiSender.execute(url);

    }
    public void validation(){

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
}