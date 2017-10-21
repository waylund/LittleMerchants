package com.brotherslynn.littlemerchants.data;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import java.util.UUID;

/**
 * Created by danielmlynn on 10/14/17.
 */

public class TestDataConnector implements IDataConnector {

    private Trip createTestTrip()
    {
        Trip testTrip = new Trip(2389, createTestDestinationLocation(), 2000);
        testTrip.setCurrentStepCount(2523);
        return testTrip;
    }

    private Location createTestCurrentLocation()
    {
        Location loc = new Location();
        loc.setName("Hometown USA");
        loc.setLocationCoordinates(10000,10000);
        return loc;
    }

    private Location createTestDestinationLocation()
    {
        Location loc = new Location();
        loc.setName("Portlandia");
        loc.setLocationCoordinates(11500,11000);
        return loc;
    }

    public Player getPlayer()
    {
        Player retPlayer = new Player();
        retPlayer.setMerchantType(1);
        retPlayer.setMerchantName("Torneko");
        retPlayer.setTrip(createTestTrip());
        retPlayer.setCurrentLocation(createTestCurrentLocation());
        return retPlayer;
    }

    public boolean savePlayer(Player player)
    {
        return true;
    }

    public Location getLocation(UUID id)
    {
        Location loc = new Location();
        if (id.equals(UUID.fromString("5edd8ef5-4401-40d6-9207-bd0f29d7004e"))) {
            loc.setName("Hometown USA");
            loc.setLocationCoordinates(10000,10000);
        } else {
            loc.setName("Portlandia");
            loc.setLocationCoordinates(11500,11000);
        }
        return loc;
    }
}
