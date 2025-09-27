package com.aviral.popcorntickets.DataObjects;

public class Movie {
    private String title;
    private String url;

    private int cost;

    public Movie(String title_,String url_,int cost)
    {
        this.title = title_;
        this.url = url_;
        this.cost=cost;
    }

    public String getTitle(){return title;}
    public String getUrl(){return url;}

    public int getCost(){return cost;}


}
