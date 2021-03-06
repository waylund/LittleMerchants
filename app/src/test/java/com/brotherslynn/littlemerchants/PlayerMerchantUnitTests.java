package com.brotherslynn.littlemerchants;

import com.brotherslynn.littlemerchants.data.TestDataConnector;
import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlayerMerchantUnitTests {

    private GameManager gameManager = new GameManager(new TestDataConnector());

    @Test
    public void getMerchantType() throws Exception {
        Player testPlayer = gameManager.getLocalPlayer();
        int merchantType = testPlayer.getMerchantType();
        assertEquals(1, merchantType);
    }

    @Test
    public void getMerchantName() throws Exception {
        Player testPlayer = gameManager.getLocalPlayer();
        String merchantName = testPlayer.getMerchantName();
        assertEquals("Torneko", merchantName);
    }

    @Test
    public void getMerchantTypeName() throws Exception {
        Player testPlayer = gameManager.getLocalPlayer();
        String merchantTypeName = gameManager.getLocalization().getMerchantType()[testPlayer.getMerchantType()];
        assertEquals("Food Merchant", merchantTypeName);
    }

    @Test
    public void getTripProgress()
    {
        //arrange
        Player testPlayer = gameManager.getLocalPlayer();
        Trip testTrip = testPlayer.getTrip();
        //act
        int steps = testTrip.getSteps();
        //assert
        assertEquals(134, steps);
    }

    @Test
    public void createNewTrip()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        int currentStep = 15382;
        Location destination = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        boolean createTrip = gameManager.startNewTrip(testPlayer, currentStep, destination);
        Trip testTrip = testPlayer.getTrip();
        assertEquals(0, testTrip.getSteps());
        assertEquals(1802, testTrip.getDistance());
        assertTrue(createTrip);
    }

    @Test
    public void createNewTrip_Travelling()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        testPlayer.setCurrentLocation(null);
        int currentStep = 15382;
        Location destination = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        boolean createTrip = gameManager.startNewTrip(testPlayer, currentStep, destination);
        assertFalse(createTrip);
    }

    @Test
    public void isTripFinished_True()
    {
        Trip testTrip = new Trip(1321, null, 2000);
        testTrip.setCurrentStepCount(3321);
        boolean testFinished = testTrip.isFinished();
        assertTrue(testFinished);
    }

    @Test
    public void isTripFinished_True_Over()
    {
        Trip testTrip = new Trip(1321, null, 2000);
        testTrip.setCurrentStepCount(3500);
        boolean testFinished = testTrip.isFinished();
        assertTrue(testFinished);
    }

    @Test
    public void isTripFinished_False()
    {
        Trip testTrip = new Trip(1321, null, 2000);
        testTrip.setCurrentStepCount(3000);
        boolean testFinished = testTrip.isFinished();
        assertFalse(testFinished);
    }

    @Test
    public void getLocation()
    {
        Location testLocation1 = gameManager.getLocation(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        Location testLocation2 = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        String locationName1 = testLocation1.getName();
        String locationName2 = testLocation2.getName();
        assertEquals("Whitfield", locationName1);
        assertEquals("Northwick", locationName2);
    }

    @Test
    public void findDistance()
    {
        Location testLocation1 = gameManager.getLocation(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        Location testLocation2 = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        int distance = gameManager.getDistance(testLocation1, testLocation2);
        assertEquals(1802, distance);
    }

    @Test
    public void tripAddSteps()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        testPlayer.setTrip(null);
        gameManager.startNewTrip(testPlayer, 10000, gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489")));
        boolean didUpdate = gameManager.addStepsToTrip(testPlayer, 5);

        assertTrue(didUpdate);
        assertEquals(10005, testPlayer.getTrip().getCurrentStepCount());
        assertEquals(null, testPlayer.getCurrentLocation());
        assertEquals("Northwick", testPlayer.getTrip().getDestination().getName());
    }

    @Test
    public void tripAddSteps_finish()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        testPlayer.setTrip(null);
        gameManager.startNewTrip(testPlayer, 10000, gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489")));
        boolean didUpdate = gameManager.addStepsToTrip(testPlayer, 1805);

        assertTrue(didUpdate);
        assertEquals("Northwick", testPlayer.getCurrentLocation().getName());
        assertEquals(null, testPlayer.getTrip());
    }

    @Test
    public void tripAddSteps_NoTrip()
    {
        Player testPlayer = gameManager.getLocalPlayer();
        testPlayer.setTrip(null);
        boolean didUpdate = gameManager.addStepsToTrip(testPlayer, 5);
        assertFalse(didUpdate);
    }

    @Test
    public void getAllLocations()
    {
        List<Location> locations = gameManager.getAllLocations();
        assertEquals(5, locations.size());
    }

    @Test
    public void getConnectedLocations_Northwick()
    {
        Location loc = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        List<UUID> locationIds = loc.getConnections();
        assertEquals(2, locationIds.size());
        assertTrue(locationIds.contains(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2")));
        assertTrue(locationIds.contains(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef")));
    }

    @Test
    public void getConnectedLocations_Whitfield()
    {
        Location loc = gameManager.getLocation(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        List<UUID> locationIds = loc.getConnections();
        assertEquals(3, locationIds.size());
        assertTrue(locationIds.contains(UUID.fromString("26c794e9-45f6-4a4e-a0da-1c34ae179c4a")));
        assertTrue(locationIds.contains(UUID.fromString("171d4ffc-385d-4fb1-8ab6-fcf3c78509ef")));
        assertTrue(locationIds.contains(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489")));
    }
}