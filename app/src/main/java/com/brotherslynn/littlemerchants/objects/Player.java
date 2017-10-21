package com.brotherslynn.littlemerchants.objects;

/**
 * Created by danielmlynn on 10/14/17.
 */

public class Player {
    private int merchantType = -1;
    private String merchantName;
    private Location currentLocation = null;
    private Trip currentTrip;
    private int currentSteps;

    public int getMerchantType()
    {
        return merchantType;
    }

    public void setMerchantType(int merchantType)
    {
        this.merchantType = merchantType;
    }

    public String getMerchantName() { return merchantName; }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Trip getTrip() {return currentTrip; }

    public void setTrip(Trip trip) { this.currentTrip = trip; }

    public void setCurrentLocation(Location currentLocation)
    {
        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public int getCurrentSteps()
    {
        return currentSteps;
    }

    public void setCurrentSteps(int currentSteps)
    {
        this.currentSteps = currentSteps;
    }
}
