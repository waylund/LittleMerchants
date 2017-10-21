package com.brotherslynn.littlemerchants.data;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;

import java.util.List;
import java.util.UUID;

public interface IDataConnector {
    Player getPlayer();
    boolean savePlayer(Player player);
    Location getLocation(UUID id);
    List<Location> getAllLocations();
    boolean syncLocations(List<Location> locations);
}
