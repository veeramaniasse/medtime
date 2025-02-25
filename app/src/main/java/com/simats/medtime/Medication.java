package com.simats.medtime;

public class Medication {
    private String name,bff,time;



    public Medication(String name,  String bff,String time) {
        this.name = name;

        this.bff = bff;
        this.time = time;

    }

    public String getName() {
        return name;
    }
    public String getbff() {
        return bff;
    }
    public String gettime() {
        return time;
    }

}
