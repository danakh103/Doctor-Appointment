package com.medi360.Common;

import com.medi360.Model.AppointmentData;
import com.medi360.Model.ScheduleAppointmentData;

/**
 * Created by mohanraj.sk on 6/28/2016.
 */
public class GlobalValues {
    static AppointmentData selectedAppointmentDetails;
    static ScheduleAppointmentData scheduleAppointmentData;
    static String googleEmailID;
    static String selectedAddress, selectedAddrLat, selectedAddrLong;

    public static String getSelectedAddress() {
        return selectedAddress;
    }

    public static void setSelectedAddress(String selectedAddress) {
        GlobalValues.selectedAddress = selectedAddress;
    }

    public static String getSelectedAddrLat() {
        return selectedAddrLat;
    }

    public static void setSelectedAddrLat(String selectedAddrLat) {
        GlobalValues.selectedAddrLat = selectedAddrLat;
    }

    public static String getSelectedAddrLong() {
        return selectedAddrLong;
    }

    public static void setSelectedAddrLong(String selectedAddrLong) {
        GlobalValues.selectedAddrLong = selectedAddrLong;
    }

    public static String getGoogleEmailID() {
        return googleEmailID;
    }

    public static void setGoogleEmailID(String googleEmailID) {
        GlobalValues.googleEmailID = googleEmailID;
    }

    public static ScheduleAppointmentData getScheduleAppointmentData() {
        return scheduleAppointmentData;
    }

    public static void setScheduleAppointmentData(ScheduleAppointmentData scheduleAppointmentData) {
        GlobalValues.scheduleAppointmentData = scheduleAppointmentData;
    }

    public static AppointmentData getSelectedAppointmentDetails() {
        return selectedAppointmentDetails;
    }

    public static void setSelectedAppointmentDetails(AppointmentData selectedAppointmentDetails) {
        GlobalValues.selectedAppointmentDetails = selectedAppointmentDetails;
    }

}
