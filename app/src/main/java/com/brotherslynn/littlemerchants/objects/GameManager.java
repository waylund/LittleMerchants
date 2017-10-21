package com.brotherslynn.littlemerchants.objects;

import android.content.Context;

import com.brotherslynn.littlemerchants.data.IDataConnector;
import com.brotherslynn.littlemerchants.data.XMLDataConnector;

import java.util.UUID;

/**
 * Created by danielmlynn on 10/14/17.
 */

public class GameManager {

    IDataConnector dataConnector;
    Localization localization;

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
}
