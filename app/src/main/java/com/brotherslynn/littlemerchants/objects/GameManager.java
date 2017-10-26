package com.brotherslynn.littlemerchants.objects;

import android.content.Context;

import com.brotherslynn.littlemerchants.data.IDataConnector;
import com.brotherslynn.littlemerchants.data.XMLDataConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private IDataConnector dataConnector;
    private Localization localization;

    public GameManager(Context applicationContext)
    {
        dataConnector = new XMLDataConnector(applicationContext);
        localization = new Localization("eng");
    }

    public GameManager(IDataConnector dataConnector)
    {
        this.dataConnector = dataConnector;
        localization = new Localization("eng");
    }

    public Player getLocalPlayer() {
        return dataConnector.getPlayer();
    }

    public Localization getLocalization()
    {
        return localization;
    }

    public boolean savePlayer(Player player)
    {
        boolean didSave = dataConnector.savePlayer(player);
        return didSave;
    }

    public boolean startNewTrip(Player player, int currentSteps, Location destination)
    {
        if (player.getCurrentLocation() != null) {
            Trip newTrip = new Trip(currentSteps, destination, getDistance(player.getCurrentLocation(), destination));
            player.setTrip(newTrip);
            return true;
        }
        return false;
    }

    public boolean addStepsToTrip(Player player, int addSteps)
    {
        if (player.getTrip() != null) {
            player.getTrip().setCurrentStepCount(player.getTrip().getCurrentStepCount() + addSteps);
            player.setCurrentLocation(null);
            if (player.getTrip().isFinished())
            {
                player.setCurrentLocation(player.getTrip().getDestination());
                player.setTrip(null);
            }
            return true;
        }
        return false;
    }

    public Location getLocation(UUID id)
    {
        return dataConnector.getLocation(id);
    }

    public int getDistance(Location location1, Location location2)
    {
        int difX = Math.abs(location2.getLocationX() - location1.getLocationX());
        int difY = Math.abs(location2.getLocationY() - location1.getLocationY());

        double distanceDouble = Math.sqrt((difX * difX) + (difY * difY));
        return (int) Math.floor(distanceDouble);
    }

    public List<Location> getAllLocations()
    {
        return dataConnector.getAllLocations();
    }


    public List<Location> getConnectedLocations(Location location)
    {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (UUID id : location.getConnections())
        {
            Location connectedLoc = getLocation(id);
            if (connectedLoc != null)
            {
                locations.add(connectedLoc);
            }
        }
        return locations;
    }

    public boolean syncLocations()
    {
        ArrayList<Location> locs = new ArrayList<Location>();
        Location loc1 = new Location();
        loc1.setId(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        loc1.setName("Whitfield");
        loc1.setLocationCoordinates(10000,10000);
        ArrayList<UUID> connections = new ArrayList<UUID>();
        connections.add(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        connections.add(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef"));
        connections.add(UUID.fromString("26c794e9-45f6-4a4e-a0da-1c34ae179c4a"));
        loc1.setConnections(connections);
        locs.add(loc1);
        Location loc2 = new Location();
        loc2.setId(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        loc2.setName("Northwick");
        loc2.setLocationCoordinates(11500,11000);
        connections = new ArrayList<UUID>();
        connections.add(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        connections.add(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef"));
        loc2.setConnections(connections);
        locs.add(loc2);
        Location loc3 = new Location();
        loc3.setId(UUID.fromString("26c794e9-45f6-4a4e-a0da-1c34ae179c4a"));
        loc3.setName("Janis Farmstead");
        loc3.setLocationCoordinates(9050,9050);
        connections = new ArrayList<UUID>();
        connections.add(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        loc3.setConnections(connections);
        locs.add(loc3);
        Location loc4 = new Location();
        loc4.setId(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef"));
        loc4.setName("Portsmouth");
        loc4.setLocationCoordinates(13000,10500);
        connections = new ArrayList<UUID>();
        connections.add(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        connections.add(UUID.fromString("02baeaef-9f97-444c-976f-9a4dbe859638"));
        connections.add(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        loc4.setConnections(connections);
        locs.add(loc4);
        Location loc5 = new Location();
        loc5.setId(UUID.fromString("02baeaef-9f97-444c-976f-9a4dbe859638"));
        loc5.setName("Portsmouth Sea Port");
        loc5.setLocationCoordinates(12900,10450);
        connections = new ArrayList<UUID>();
        connections.add(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef"));
        loc5.setConnections(connections);
        locs.add(loc5);
        boolean didSync = dataConnector.syncLocations(locs);
        return didSync;
    }
}
