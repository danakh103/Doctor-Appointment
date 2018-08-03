package com.medi360.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Config;
import com.medi360.utils.Configuration;
import com.medi360.utils.Utils;
import com.medi360.utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Mohanraj.SK on 6/25/2016.
 */
public class LoginActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    //Signin button
    private SignInButton signInButton;

//    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    public GlobalValues global;

    //Signing Options
    //private GoogleSignInOptions gso;

    //google api client
//    private GoogleApiClient mGoogleApiClient;
//    public GlobalValues global;

    //Signin constant to check the activity result
//    private int RC_SIGN_IN = 100;

    String googleEmailID;
TextView server;
    EditText usernameEditText;
    public static final String PREFS_NAME = "MediAuthUser";
    protected String SENDER_ID = "1062140732913";
    private GoogleCloudMessaging gcm =null;
    private String regid = null;
    final Context context = this;
    Button loginBtn;
    private final static String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addListenerOnTextView();
        activity = this;

        Boolean value=checkPermission();
        if (!value) {
            requestPermission();
        }


        //setupGoogleLogin();
        setUpGoogleLogin();
        if(checkAlreadyLoggedIn()){
            GetGCM();
            initializeAll();
        }
    }

    public void setUpGoogleLogin(){
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]

    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            googleEmailID = acct.getEmail();
            System.out.println("Achyutha,email id is:" +googleEmailID );
            global.setGoogleEmailID(googleEmailID);
            signOut();
            checkEmailExist();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]



//    public void setupGoogleLogin(){
//        //Initializing google signin option
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        //Initializing signinbutton
//        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
//        signInButton.setScopes(gso.getScopeArray());
//
//        //Initializing google api client
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//
//        //Setting onclick listener to signing button
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSignInWithGoogle();
//            }
//        });
//
//    }
    public boolean checkAlreadyLoggedIn(){

//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Log.v("IsAlready Login","=====>"+settings.getBoolean("isAlreadyLoggedIn",false));
        Log.v("AutoLog","=====>"+settings.getString("AutoLog","").toString());
        Log.v("LoggedUserDetails","=====>"+settings.getString("LoggedUserDetails","").toString());
        if (settings.getString("AutoLog","").equals("yes")) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//            intent.putExtra("callingActivity", "LoginActivity");

            startActivity(intent);
            finish();
            return false;
        }
        return true;
    }
    public void initializeAll(){
        usernameEditText = (EditText)findViewById(R.id.register_no_edit_txt);
       // loginBtn = (Button)findViewById(R.id.btn_login);
       // loginBtn.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
            //    doSignIn();
            //}
        //});


    }
    private void doSignIn(){
        ApiSender apiSender = new ApiSender(LoginActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
//                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
//                SharedPreferences.Editor editor = loggedUser.edit();




                if(response.contains("Invalid")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Invalid Username/Password");
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{
                    JSONArray responseAry = new JSONArray(response);
                    JSONObject userDtails = responseAry.getJSONObject(0);
                    saveLocal(userDtails);
//                    editor.putString("LoggedUserDetails",userDtails.toString());
                    finish();
                    Intent registerIntent = new Intent(LoginActivity.this,DashboardActivity.class);
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
        RequestParams requestParams = new RequestParams();
        requestParams.setPatientId(usernameEditText.getText().toString());
        requestParams.setDeviceid(regid);

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"HospitalauthenticateUser", "POST",inputParams};
        apiSender.execute(url);

    }
    public void saveLocal(JSONObject userDtails){
        try{
//            SharedPreferences loggedUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor=loggedUser.edit();
            editor.putBoolean("isAlreadyLoggedIn", true);
            editor.putString("AutoLog", "yes");


            editor.putString("Patientname", userDtails.getString("Patientname"));
            editor.putInt("Patid", userDtails.getInt("Patid"));
//            editor.putString("RegId",usernameEditText.getText().toString() );
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
            editor.putInt("CityId", (userDtails.getInt("CityID")));
            System.out.println(" city id "+userDtails.getInt("CityID"));
            editor.commit();
            Log.v("JJJJ",loggedUser.getBoolean("isAlreadyLoggedIn",false)+"");
            Log.v("AutoLog",loggedUser.getString("AutoLog","")+"");
        }catch(Exception e){

        }

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
    public void addListenerOnTextView() {
        //final Context context = this;

        server = (TextView) findViewById(R.id.tv_server);
        server.setText(Configuration.server);
        server.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                if (Configuration.server.equals("LIVE")) {
                    Configuration.server = "TEST";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("SERVER", "true");
                    editor.commit();

                } else if (Configuration.server.equals(("TEST"))) {
                    Configuration.server = "LIVE";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("SERVER", "false");
                    editor.commit();
                }
                server.setText(Configuration.server);


            }
        });
    }

        //This function will option signing intent
    private void doSignInWithGoogle() {
//        //Creating an intent
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//
//        //Starting intent for result
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //If signin
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            //Calling a new function to handle signin
//            handleSignInResult(result);
//        }
//    }
//
//
//    //After the signing we are calling this function
//    private void handleSignInResult(GoogleSignInResult result) {
//        //If the login succeed
//        if (result.isSuccess()) {
//            //Getting google account
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            System.out.println(acct.getDisplayName());
//            System.out.println(acct.getEmail());
//            googleEmailID = acct.getEmail();
//            global.setGoogleEmailID(googleEmailID);
//            checkEmailExist();
//
//        } else {
//            System.out.println("ERORRRRRR");
//            System.out.print(result);
//            System.out.println(" result "+result.isSuccess());
//
//            //If login fails
//            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }

    public void checkEmailExist(){
        ApiSender apiSender = new ApiSender(LoginActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
                if(response.contains("false") || response.equals("") || response.equals(" ") || response.equals("Null") || response.equals("Error occured")) {

                    Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(i);
                    //utilsObj.showCustomToast(getApplicationContext(),"Invalid Username/Password");
                }
                 else{
                    JSONArray responseAry = new JSONArray(response);
                    JSONObject userDtails = responseAry.getJSONObject(0);
                    saveLocal(userDtails);

                    Intent registerIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(registerIntent);
                    /*String loginstatus=userDtails.getString("Status");
                    System.out.println("login status is: "+loginstatus);
                    if(loginstatus.equals("true")) {
                        System.out.println("login status inside if is : "+loginstatus);
                        Intent registerIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(registerIntent);
                    }
                    else
                        utilsObj.showCustomToast(getApplicationContext(),"IP number is already used or not valid");*/
                }

            }
        });

        //Convert the JAVA class object to json string
        //Gson gson = new Gson();
        String inputParams = googleEmailID;

        //Send request
        String[] url = {"patemailalreadyexist", "POST",inputParams};
        apiSender.execute(url);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private Activity activity;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private void requestPermission(){

        if ((ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) &&(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) &&(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) &&(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))){

//            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int resultLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int resultCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && resultLocation ==PackageManager.PERMISSION_GRANTED && resultCoarseLocation ==PackageManager.PERMISSION_GRANTED && writeExternalStorage ==PackageManager.PERMISSION_GRANTED ){

            return true;

        } else {

            return false;

        }
    }

    private View view;
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(LoginActivity.this, "Permission Granted, Now you can access location data ", Toast.LENGTH_SHORT).show();
                    // Toast.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
//                    Toast.makeText(LoginActivity.this, "Permission Denied, You cannot access location data. ", Toast.LENGTH_SHORT).show();
                    //  Snackbar.make(view,"Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
}