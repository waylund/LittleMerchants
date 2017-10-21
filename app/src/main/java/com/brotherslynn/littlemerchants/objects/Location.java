package com.brotherslynn.littlemerchants.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Location {



    private UUID id;
    private String name;
    private int location_x;
    private int location_y;
    private List<UUID> connections = new ArrayList<UUID>();;

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

    public void setConnections(List<UUID> connections)
    {
        this.connections = connections;
    }

    public List<UUID> getConnections()
    {
        return connections;
    }
}
