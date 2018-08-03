package com.medi360.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.medi360.Adapter.PreviousAppintmentListAdapter;
import com.medi360.R;
import com.medi360.utils.Utils;

/**
 * Created by Mohanraj.SK on 6/26/2016.
 */
public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout previousAppointmentVw,scheduleAppointmentVw,trackKioskVW,profileVw;
    public static final String PREFS_NAME = "MediAuthUser";
    Utils utilsObj = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        initializeAll();

    }
    public void initializeAll(){
        previousAppointmentVw = (LinearLayout)findViewById(R.id.previous_appointmet);
        scheduleAppointmentVw = (LinearLayout)findViewById(R.id.schedule_appointment);
        profileVw = (LinearLayout)findViewById(R.id.profile_vw);
        previousAppointmentVw.setOnClickListener(this);
        scheduleAppointmentVw.setOnClickListener(this);
        profileVw.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_vw :
                Intent intent = new Intent(DashboardActivity.this, ProfileAddressActivity.class);
                startActivity(intent);
                break;
            case  R.id.previous_appointmet :
                Intent previousAppointmetIntent = new Intent(DashboardActivity.this, PreviousAppointmentActivity.class);
                startActivity(previousAppointmetIntent);
                break;
            case R.id.track_kiosk :
//                Intent trackLioskIntent = new Intent(DashboardActivity.this, ProfileAddressActivity.class);
//                startActivity(trackLioskIntent);
                utilsObj.showCustomToast(getApplicationContext(),"Inprogress");
                break;
            case  R.id.schedule_appointment :
                Intent scheduleAppointmentIntent = new Intent(DashboardActivity.this, ScheduleAppointmentActivity.class);
                startActivity(scheduleAppointmentIntent);
                break;

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.menu_login, menu);
        return true;
    }
    public void switchActivity(Intent intent){

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isAlreadyLoggedIn", false);
            editor.putString("AutoLog","no");
           // editor.remove("username");


            editor.commit();

            System.out.println("Going to stop the service");
            //  Intent intent= new Intent(this, MyService.class);
//            this.stopService(intent);

finish();
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
