package com.medi360.Model;

/**
 * Created by Mohanraj.SK on 6/26/2016.
 */
public class AppointmentData {
    String appointmentTime;
    String appointmentId;
    String doctorName,date,Am;
    String appointmentSession,complaintText,complaintType,getAppointmentDay,complaintid,SpecialistID;

    public String getSpecialistID() {
        return SpecialistID;
    }

    public void setSpecialistID(String specialistID) {
        SpecialistID = specialistID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDate(String date) {
        this.date = date;
    }

        public String getdate(){
        return date;
    }
    public void setAm(String am){
        this.Am=am;
    }
    public String getAm(){
        return Am;
    }


    public String getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(String complaintid) {
        this.complaintid = complaintid;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    String appointmentStatus;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentTime() {

        return appointmentTime;
    }



    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentSession() {
        return appointmentSession;
    }

    public void setAppointmentSession(String appointmentSession) {
        this.appointmentSession = appointmentSession;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getGetAppointmentDay() {
        return getAppointmentDay;
    }

    public void setGetAppointmentDay(String getAppointmentDay) {
        this.getAppointmentDay = getAppointmentDay;
    }
    public String getAppointmentDay(){
        return getAppointmentDay;
    }
}
