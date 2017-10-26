package com.brotherslynn.littlemerchants;

import com.brotherslynn.littlemerchants.data.TestDataConnector;
import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;

import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class LittleMerchantAcceptanceTests {

    private GameManager gameManager = new GameManager(new TestDataConnector());

    @Test
    public void NewTripCreation() {

        // precondition
        Player testPlayer = gameManager.getLocalPlayer();
        Location testDest = testPlayer.getTrip().getDestination();
        testPlayer.setTrip(null);

        // action under test
        gameManager.startNewTrip(testPlayer, 10000, testDest);

        // post conditions
        assertEquals("Whitfield", testPlayer.getCurrentLocation().getName());
        assertEquals(10000, testPlayer.getTrip().getStartingStepCount());
        assertEquals(1802, testPlayer.getTrip().getDistance());
        assertEquals(0, testPlayer.getTrip().getSteps());
        assertEquals("Northwick", testPlayer.getTrip().getDestination().getName());

    }

    @Test
    public void NewTripStepsButNotThere()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        Location testDest = testPlayer.getTrip().getDestination();
        testPlayer.setTrip(null);
        gameManager.startNewTrip(testPlayer, 10000, testDest);

        // action under test
        gameManager.addStepsToTrip(testPlayer, 152);

        // post conditions
        assertEquals(null, testPlayer.getCurrentLocation());
        assertEquals(152, testPlayer.getTrip().getSteps());
        assertEquals("Northwick", testPlayer.getTrip().getDestination().getName());
    }

    @Test
    public void MapLocations_NumberofConnections()
    {
        // b73f1468-b85f-4cb7-aa3d-950c6ce19bf2   Whitfield (10000,10000) Whitfield?
        // 26c794e9-45f6-4a4e-a0da-1c34ae179c4a   Janis Farmstead (9050,9050)
        // 7e26f6dd-7343-4878-9076-c2b2b1758489   Northwick (11500,11000)
        // 171d4ffc-385d-4fb1-8ab6-fcf3c78509ef   Portsmouth (13000,10500)
        // 02baeaef-9f97-444c-976f-9a4dbe859638   Portsmouth Sea Port (12900,10450)

        Location startingLoc = gameManager.getLocation(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));

        assertTrue(connectsTo(startingLoc, "Northwick"));
        assertTrue(connectsTo(startingLoc, "Portsmouth"));
        assertTrue(connectsTo(startingLoc, "Janis Farmstead"));
        assertFalse(connectsTo(startingLoc, "Portsmouth Sea Port"));
    }

    @Test
    public void MapLocations_NumberofConnections_Example2()
    {

        Location startingLoc = gameManager.getLocation(UUID.fromString("26c794e9-45f6-4a4e-a0da-1c34ae179c4a"));

        assertFalse(connectsTo(startingLoc, "Northwick"));
        assertTrue(connectsTo(startingLoc, "Whitfield"));
        assertFalse(connectsTo(startingLoc, "Portsmouth"));
        assertFalse(connectsTo(startingLoc, "Portsmouth Sea Port"));
    }

    private boolean connectsTo(Location origination, String connectionName)
    {
        List<Location> locs = gameManager.getConnectedLocations(origination);
        for (Location loc : locs)
        {
            if (loc.getName().equals(connectionName))
            {
                return true;
            }
        }
        return false;
    }
}
