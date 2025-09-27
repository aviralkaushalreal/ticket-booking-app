package com.aviral.popcorntickets.DataObjects;

import java.util.ArrayList;

public class MovieData {

    public String title;
    public String date;
    public String time;

    public ArrayList<String> seats;

    public String img;


    public void setData(String title,String date,String time,ArrayList<String> seats,String img)
    {
        this.title=title;
        this.date=date;
        this.seats=seats;
        this.time=time;
        this.img=img;
    }

}
