package com.brotherslynn.littlemerchants.objects;

import java.util.UUID;

public class Location {



    private UUID id;
    private String name;
    private int location_x;
    private int location_y;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

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
