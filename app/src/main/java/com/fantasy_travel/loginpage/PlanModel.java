package com.fantasy_travel.loginpage;

public class PlanModel {

    private String startLat;
    private String startLong;
    private String startLocation;
    private String endLat;
    private String endLong;
    private String endLocation;
    private String emailId;
    private String preferredSex;
    private String preferredMode;
    private String planName;
    private String time;

    public PlanModel()
    {

    }

    public PlanModel( String emailId, String planName, String startLocation,  String endLocation,   String time,  String preferredMode,String preferredSex) {
       // this.startLat = startLat;
       // this.startLong = startLong;
        this.emailId = emailId;
        this.planName = planName;
        this.startLocation = startLocation;
       //this.endLat = endLat;
       // this.endLong = endLong;
        this.endLocation = endLocation;
        this.time = time;
        this.preferredMode = preferredMode;
        this.preferredSex = preferredSex;



    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPreferredSex() {
        return preferredSex;
    }

    public void setPreferredSex(String preferredSex) {
        this.preferredSex = preferredSex;
    }

    public String getPreferredMode() {
        return preferredMode;
    }

    public void setPreferredMode(String preferredMode) {
        this.preferredMode = preferredMode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
