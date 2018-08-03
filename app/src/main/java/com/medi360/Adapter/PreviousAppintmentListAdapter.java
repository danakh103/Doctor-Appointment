package com.medi360.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medi360.Model.AppointmentData;
import com.medi360.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohanraj.SK on 6/25/2016.
 */
public class PreviousAppintmentListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<AppointmentData> appointmentListData = new ArrayList<>();
    Context context;
    public PreviousAppintmentListAdapter(Context context,ArrayList<AppointmentData> appointmentListData){
        this.context = context;
        this.appointmentListData = appointmentListData;
    }
    @Override
    public int getCount() {
        return appointmentListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.appointment_single_item,null);

        }
        AppointmentData appointmentData = appointmentListData.get(position);
        TextView complaintTextView = (TextView)convertView.findViewById(R.id.issue_txt_vw);
        TextView dateview=(TextView)convertView.findViewById(R.id.date_txt_vw);
        TextView timeview= (TextView)convertView.findViewById(R.id.time_txt_vw);
        TextView dayview= (TextView)convertView.findViewById(R.id.day_txt_vw);
        TextView amview= (TextView)convertView.findViewById(R.id.am_pm_txt_vw);
        complaintTextView.setText(appointmentData.getComplaintText());
        dateview.setText(appointmentData.getdate());
                timeview.setText(appointmentData.getAppointmentTime());
        //dayview.setText(appointmentData.getAppointmentStatus());
        String statustemp=appointmentData.getAppointmentStatus();
        System.out.println("statustempstatustemp : " + statustemp);
        if (statustemp.contains("0")) {

            try {
                String tempappdate = appointmentData.getdate();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(tempappdate);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date todate = new Date();
                System.out.println(dateFormat.format(todate));

                if(date.before(todate))
                    dayview.setText("Expired");
                else{
                    dayview.setText("Waiting");
                }
            }catch (Exception e){

                System.out.println("Exception   "+e);
            }

        }
        else if(statustemp.contains("1"))
        {
            dayview.setText("Accepted");
        }
        else if(statustemp.contains("4")){
            dayview.setText("Waiting");
        }
        else if(statustemp.contains("2") )
        {
            dayview.setText("Consulted");
        }
        else {
            dayview.setText("Cancelled");
        }

        String temptime=appointmentData.getAppointmentTime();

            amview.setText(appointmentData.getAm());


        return convertView;
    }
}
