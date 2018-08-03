package com.medi360.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Utils;
import com.medi360.utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by vijaya_ on 6/29/2016.
 */
public class RegisterActivity extends AppCompatActivity{

    Utils utilsObj = new Utils();
    EditText usernameEditText;
    public static final String PREFS_NAME = "MediAuthUser";
    protected String SENDER_ID = "1062140732913";
    private GoogleCloudMessaging gcm =null;
    private String regid = null;
    final Context context = this;
    Button loginBtn;
    public GlobalValues global;
    private final static String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //   if(checkAlreadyLoggedIn()){
            GetGCM();
            initializeAll();
      //  }



    }



    private void GetGCM() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("in inside get gcm");
                    registerInBackground();
              /*  GCMHelper  gcmRegistrationHelper = new GCMHelper (
                        getApplicationContext());
                String gcmRegID = gcmRegistrationHelper.GCMRegister(SENDER_ID);
*/
                } catch (Exception bug) {
                    bug.printStackTrace();
                }

            }
        });

        thread.start();
    }
    private void registerInBackground() {

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    System.out.println("in register background");
                    regid = gcm.register(SENDER_ID);
                    System.out.println("in reg id is : "+regid);
                    Log.d(TAG, "########################################");
                    Log.d(TAG, "Current Device's Registration ID is: " + msg);
                    System.out.println("device id at 3 :"+msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return null;
            }

            protected void onPostExecute(Object result) { //to do here };
            }



            //execute(null,null,null);
        }.execute(null, null, null);
    }


    public void initializeAll(){

        System.out.println("SELECTED EMAIL : " + global.getGoogleEmailID());
        usernameEditText = (EditText)findViewById(R.id.register_no_edit_txt);
        loginBtn = (Button)findViewById(R.id.btn_register);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctorname = usernameEditText.getText().toString();
                if (doctorname.equals("") || doctorname.equals(" ")) {
                    utilsObj.showCustomToast(getApplicationContext(), "Please Enter Registration Number!");
                } else {

                    doSignUp();
                }

            }
        });


    }



    private void doSignUp(){
        ApiSender apiSender = new ApiSender(RegisterActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
//                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
//                SharedPreferences.Editor editor = loggedUser.edit();




                /*if(response.contains("Invalid")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Invalid IP");
                }*/

                if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(), "Error Occured..!");
                }
                else {
                    JSONArray responseAry = new JSONArray(response);
                    JSONObject userDtails = responseAry.getJSONObject(0);
                    //JSONObject userDtails=new JSONObject(response);
                    saveLocal(userDtails);
//                    editor.putString("LoggedUserDetails",userDtails.toString());
                    String loginstatus = userDtails.getString("Status");
                    System.out.println("login status is: " + loginstatus);
                    if (loginstatus.equals("true")) {
                        System.out.println("login status inside if is : " + loginstatus);
                        Intent registerIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
                        startActivity(registerIntent);
                    }
                    if (loginstatus.equals("false")) {
                        String message = userDtails.getString("message");
                        if (message.contains("Already email is configured"))
                            utilsObj.showCustomToast(getApplicationContext(), "This IP Is Already Configured");
                        else if (message.contains("Invalid ip number"))
                            utilsObj.showCustomToast(getApplicationContext(), "This Ip Is Invalid");

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
        RequestParams requestParams = new RequestParams();
        requestParams.setPatientId(usernameEditText.getText().toString());
        requestParams.setEmail(global.getGoogleEmailID());
        requestParams.setDeviceid(regid);

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"updatepatemail", "POST",inputParams};
        apiSender.execute(url);

    }
    public void saveLocal(JSONObject userDtails){
        try{
//            SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor=loggedUser.edit();
            editor.putBoolean("isAlreadyLoggedIn",true);
            editor.putString("AutoLog", "yes");


            editor.putString("Patientname",userDtails.getString("Patientname"));
            editor.putInt("Patid",userDtails.getInt("Patid"));
            editor.putString("RegId",userDtails.getString("PatHospId"));
            //editor.putString("hospital",hospital.getText().toString());
            editor.putString("FirstName",userDtails.getString("FirstName"));
            editor.putString("LastName",userDtails.getString("LastName"));
            editor.putString("Email",userDtails.getString("Email"));
            editor.putString("ContactNo",userDtails.getString("ContactNo"));
            editor.putString("Addressline1",userDtails.getString("Addressline1"));
            editor.putString("Addressline2",userDtails.getString("Addressline2"));
            editor.putString("Zipcode",userDtails.getString("Zipcode"));
            editor.putString("Username",userDtails.getString("UserName"));
            editor.putString("Password",userDtails.getString("Password"));
            editor.putString("Gender", userDtails.getString("Sex"));
            editor.putInt("CityId",(userDtails.getInt("CityID")));
            editor.commit();
            Log.v("JJJJ",loggedUser.getBoolean("isAlreadyLoggedIn",false)+"");
            Log.v("AutoLog",loggedUser.getString("AutoLog","")+"");
        }catch(Exception e){

        }

    }

}
