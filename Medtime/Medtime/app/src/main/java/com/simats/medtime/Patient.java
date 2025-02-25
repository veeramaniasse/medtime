package com.simats.medtime;

public class Patient {

    private String name,user,img;



    public Patient(String name,  String user,String img) {
        this.name = name;

        this.user = user;
        this.img = img;

    }

    public String getName() {
        return name;
    }
    public String getuser() {
        return user;
    }
    public String getimg() {
        return img;
    }

}
