package com.example.meetishah.status;

public class Status  {
    String email;
    String userid;
    String status;
    String date;
    String time;
    String imagePath;
    String dat;
    Status(){

    }

    public Status(String email, String userid, String status, String date, String time, String imagePath, String dat) {
        this.email = email;
        this.userid = userid;
        this.status = status;
        this.date=date;
        this.time=time;
        this.imagePath=imagePath;
        this.dat=dat;
    }

    public Status(String email, String userid, String date, String time, String imagePath, String dat, int phi) {
        this.email = email;
        this.userid = userid;
        this.date = date;
        this.time = time;
        this.imagePath = imagePath;
        this.dat=dat;
    }
    public Status(String email, String userid, String status, String date, String time, String dat){
        this.email = email;
        this.userid = userid;
        this.status = status;
        this.date=date;
        this.time=time;
        this.dat=dat;

    }


    public String getEmail() {
        return email;
    }

    public String getUserid() {
        return userid;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDat() {
        return dat;
    }
}
