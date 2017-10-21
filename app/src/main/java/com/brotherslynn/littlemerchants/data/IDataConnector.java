package com.brotherslynn.littlemerchants.data;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;

import java.util.UUID;

/**
 * Created by danielmlynn on 10/14/17.
 */

public interface IDataConnector {
    public Player getPlayer();
    public boolean savePlayer(Player player);
    public Location getLocation(UUID id);
}
