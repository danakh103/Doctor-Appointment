package com.medi360.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medi360.Adapter.PreviousAppintmentListAdapter;
import com.medi360.Common.GlobalValues;
import com.medi360.Model.AppointmentData;
import com.medi360.Model.RequestParams;
import com.medi360.R;
import com.medi360.utils.ApiCallback;
import com.medi360.utils.ApiSender;
import com.medi360.utils.Utils;
import com.medi360.utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mohanraj.SK on 6/26/2016.
 */
public class AppointmentDetailsActivity extends AppCompatActivity {

    TextView appointmentIDTxtVw,complaintTxtVw,specialistTxtVw,statusTxtVw,dateTxtVw,complaintidtziew;
    Button downloadPrescriptionsBtn,cancelappointmentbtn;
    AppointmentData appointmentData = GlobalValues.getSelectedAppointmentDetails();

    public static final String PREFS_NAME = "MediAuthUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.backbut);
        upArrow.setColorFilter(getResources().getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        initializeAll();

    }
    public void initializeAll(){
        appointmentIDTxtVw = (TextView)findViewById(R.id.app_id);
        complaintTxtVw = (TextView)findViewById(R.id.complaint_txt_vw);
        specialistTxtVw = (TextView)findViewById(R.id.doctor_name);
        statusTxtVw= (TextView)findViewById(R.id.detailstatus);
                dateTxtVw= (TextView)findViewById(R.id.detaildate);
        cancelappointmentbtn=(Button)findViewById(R.id.btncancelapp);
        complaintidtziew=(TextView)findViewById(R.id.comp_id);
        downloadPrescriptionsBtn=(Button) findViewById(R.id.btnfixapp);
        appointmentIDTxtVw.setText(appointmentData.getAppointmentId());
        complaintTxtVw.setText(appointmentData.getComplaintText());
        specialistTxtVw.setText(appointmentData.getDoctorName());
        String statustemp=appointmentData.getAppointmentStatus();
        complaintidtziew.setText(appointmentData.getComplaintid());
        downloadPrescriptionsBtn.setVisibility(View.GONE);
        cancelappointmentbtn.setVisibility(View.GONE);
        System.out.println(" statustemp " + statustemp);
        if (statustemp.contains("0")) {

            try {
                String tempappdate = appointmentData.getdate();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(tempappdate);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 Date todate = new Date();
                System.out.println(dateFormat.format(todate));

                if(date.before(todate))
                    statusTxtVw.setText("Expired");
                else{
                    statusTxtVw.setText("Waiting");
                    cancelappointmentbtn.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

                System.out.println("Exception   "+e);
            }

        }
        else if(statustemp.contains("1"))
        {
            statusTxtVw.setText("Accepted");
            cancelappointmentbtn.setVisibility(View.VISIBLE);
        }
        else if(statustemp.contains("4")){
            statusTxtVw.setText("Waiting");
            cancelappointmentbtn.setVisibility(View.VISIBLE);
        }
        else if(statustemp.contains("2") )
        {
            downloadPrescriptionsBtn.setVisibility(View.VISIBLE);
            statusTxtVw.setText("Consulted");
        }
        else {
            statusTxtVw.setText("Cancelled");
        }
       // statusTxtVw.setText(appointmentData.getAppointmentStatus());
        dateTxtVw.setText(appointmentData.getAppointmentTime() + " " + appointmentData.getAm() + " " + appointmentData.getdate());

    }

    public void btnfixapp_OnClick(View view) {
        System.out.println("clicked on fix appointments");

        getDiagnosisFromServer();

    }

    public void btncancelapp_OnClick(View view) {
        System.out.println("clicked on fix appointments");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel");
        alertDialogBuilder.setMessage("Are you sure,You want to cancel appointment?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
               // Toast.makeText(AppointmentDetailsActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                cancelappointment();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


    public void cancelappointment(){
        ApiSender apiSender = new ApiSender(AppointmentDetailsActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = loggedUser.edit();

                System.out.println("response : "+response);
                if(response.contains("No records found.")) {
                    utilsObj.showCustomToast(getApplicationContext(),"No records found.");
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{
                    // String temp[]={};
                    ArrayList<String> stringArraystatus = new ArrayList<String>();
                    try{

                        System.out.println(" inside else");
                        System.out.println("response : " + response);
                        String str=response;
finish();

                        // JSONArray diagonsisArray = new JSONArray(response);


                    }catch(Exception e){


                        System.out.println(" Exception  "+e );
                    }
                }

            }
        });

        TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
        int offvalue = tz.getOffset(new Date().getTime()) / 1000 / 60;
        //Build input params
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        RequestParams requestParams = new RequestParams();
        requestParams.setPatid(loggedUser.getInt("Patid", 0));
        requestParams.setAppointmentid(appointmentData.getAppointmentId());
        requestParams.setAppointmentcomplaintid(appointmentData.getComplaintid().toString());
        requestParams.setAppointmentdoctorname(appointmentData.getDoctorName());
        requestParams.setAppointmentdate(appointmentData.getdate());
        requestParams.setAppttime(appointmentData.getAppointmentTime() + " " + appointmentData.getAm());
        requestParams.setSpecialistID(appointmentData.getSpecialistID());
        requestParams.setOffsetvalue("-" + String.valueOf(offvalue));

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);


        //Send request
        String[] url = {"cancelappointment", "POST",inputParams};
        apiSender.execute(url);
    }




    public void getDiagnosisFromServer(){
        ApiSender apiSender = new ApiSender(AppointmentDetailsActivity.this, new ApiCallback() {
            @Override
            public void responseCallback(Context context, Activity activity, String response) throws JSONException {
                Utils utilsObj = new Utils();
                SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = loggedUser.edit();

                System.out.println("response : "+response);
                if(response.contains("No records found.")) {
                    utilsObj.showCustomToast(getApplicationContext(),"No records found.");
                }
                else if(response.equals("Null") || response.equals("Error occured")) {
                    utilsObj.showCustomToast(getApplicationContext(),"Error Occured..!");

                }
                else{
                    // String temp[]={};
                    ArrayList<String> stringArraystatus = new ArrayList<String>();
                    try{

                        System.out.println(" inside else");
                        System.out.println("response : "+response);
                        String str=response;

                        new DownloadFileFromURL().execute(str);
                       // JSONArray diagonsisArray = new JSONArray(response);


                    }catch(Exception e){


                        System.out.println(" Exception  "+e );
                    }
                }

            }
        });


        //Build input params
        SharedPreferences loggedUser = getSharedPreferences(PREFS_NAME, 0);
        RequestParams requestParams = new RequestParams();
        requestParams.setPatid(loggedUser.getInt("Patid", 0));
        requestParams.setAppointmentid(appointmentData.getAppointmentId());

        //Convert the JAVA class object to json string
        Gson gson = new Gson();
        String inputParams = gson.toJson(requestParams);

        //Send request
        String[] url = {"gethosppatientpdf", "POST",inputParams};
        apiSender.execute(url);
    }


    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                System.out.println("Inside download  ");
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/uploadfiles.pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                //Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/uploadfiles.pdf")), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
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
