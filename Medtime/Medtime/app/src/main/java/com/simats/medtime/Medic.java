package com.simats.medtime;

public class Medic {

    private String name,mrng,eve,nght;



    public Medic(String name,  String mrng,String eve,  String nght) {
        this.name = name;

        this.mrng = mrng;
        this.eve = eve;
        this.nght = nght;

    }

    public String getName() {
        return name;
    }
    public String getmrng() {
        return mrng;
    }
    public String geteve() {
        return eve;
    }
    public String getnght() {
        return nght;
    }
}
