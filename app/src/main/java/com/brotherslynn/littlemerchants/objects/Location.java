package com.brotherslynn.littlemerchants.objects;

/**
 * Created by danielmlynn on 10/17/17.
 */

public class Location {

    private String name;
    private int location_x;
    private int location_y;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getLocationX()
    {
        return location_x;
    }

    public int getLocationY()
    {
        return location_y;
    }

    public void setLocationCoordinates(int X, int Y)
    {
        this.location_x = X;
        this.location_y = Y;
    }
}
