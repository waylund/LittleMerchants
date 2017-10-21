package com.brotherslynn.littlemerchants.data;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
        } else if (id.equals(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"))) {
            loc.setId(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
            loc.setName("Northwick");
            loc.setLocationCoordinates(11500,11000);
        } else {
            loc.setName("Portlandia");
            loc.setLocationCoordinates(11500,11000);
        }
        return loc;
    }

    public List<Location> getAllLocations()
    {
        ArrayList<Location> locs = new ArrayList<Location>();
        Location loc1 = new Location();
        loc1.setId(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        loc1.setName("Whitfair");
        loc1.setLocationCoordinates(10000,10000);
        locs.add(loc1);
        Location loc2 = new Location();
        loc2.setId(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        loc2.setName("Northwick");
        loc2.setLocationCoordinates(11500,11000);
        locs.add(loc2);
        Location loc3 = new Location();
        loc3.setId(UUID.fromString("26c794e9-45f6-4a4e-a0da-1c34ae179c4a"));
        loc3.setName("Janis Farmstead");
        loc3.setLocationCoordinates(9050,9050);
        locs.add(loc3);
        Location loc4 = new Location();
        loc4.setId(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef"));
        loc4.setName("Portsmouth");
        loc4.setLocationCoordinates(13000,10500);
        locs.add(loc4);
        Location loc5 = new Location();
        loc5.setId(UUID.fromString("02baeaef-9f97-444c-976f-9a4dbe859638"));
        loc5.setName("Portsmouth Sea Port");
        loc5.setLocationCoordinates(12900,10450);
        locs.add(loc5);
        return locs;
    }

    public boolean syncLocations(List<Location> locations)
    {
        return true;
    }
}
