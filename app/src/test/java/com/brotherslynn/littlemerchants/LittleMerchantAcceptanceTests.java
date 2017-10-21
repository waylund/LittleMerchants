package com.brotherslynn.littlemerchants;

import com.brotherslynn.littlemerchants.data.TestDataConnector;
import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by danielmlynn on 10/20/17.
 */

public class LittleMerchantAcceptanceTests {

    GameManager gameManager = new GameManager(new TestDataConnector());

    @Test
    public void NewTripCreation() {

        // precondition
        Player testPlayer = gameManager.getLocalPlayer();
        Location testDest = testPlayer.getTrip().getDestination();
        testPlayer.setTrip(null);

        // action under test
        gameManager.startNewTrip(testPlayer, 10000, testDest);

        // post conditions
        assertEquals("Hometown USA", testPlayer.getCurrentLocation().getName());
        assertEquals(10000, testPlayer.getTrip().getStartingStepCount());
        assertEquals(1802, testPlayer.getTrip().getDistance());
        assertEquals(0, testPlayer.getTrip().getSteps());
        assertEquals("Portlandia", testPlayer.getTrip().getDestination().getName());

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
        assertEquals("Portlandia", testPlayer.getTrip().getDestination().getName());
    }
}
