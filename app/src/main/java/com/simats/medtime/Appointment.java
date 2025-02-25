package com.simats.medtime;

public class Appointment {

    private String name,user,issue,date,status;



    public Appointment(String name,  String user,String issue, String date) {
        this.name = name;

        this.user = user;
        this.issue = issue;
        this.date = date;
    }
    public Appointment(String name,  String user,String issue, String date,String status) {
        this.name = name;

        this.user = user;
        this.issue = issue;
        this.date = date;
        this.status= status;
    }

    public String getName() {
        return name;
    }
    public String getuser() {
        return user;
    }
    public String getIssue(){return issue;}
    public String getstatus() {
        return status;
    }
    public String getDate() {
        return date;
    }
}
