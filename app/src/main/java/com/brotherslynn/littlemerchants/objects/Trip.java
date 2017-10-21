package com.brotherslynn.littlemerchants.objects;

/**
 * Created by danielmlynn on 10/17/17.
 */

public class Trip {

    private int startingStepCount = 0;
    private int currentStepCount = 0;
    private int distance = 0;
    private Location destination;

    public Trip()
    {

    }

    public Trip(int startingStepCount, Location destination, int distance)
    {
        this.startingStepCount = startingStepCount;
        this.currentStepCount = startingStepCount;
        this.destination = destination;
        this.distance = distance;
    }

    public int getSteps()
    {
        return currentStepCount - startingStepCount;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCurrentStepCount(int currentStepCount)
    { this.currentStepCount = currentStepCount; }

    public int getCurrentStepCount() {
        return currentStepCount;
    }

    public boolean isFinished()
    {
        int endStep = startingStepCount+distance;
        if (endStep > currentStepCount)
            return false;
        else
            return true;
    }

    public void setStartingStepCount(int startingStepCount)
    {
        this.startingStepCount = startingStepCount;
    }

    public int getStartingStepCount()
    {
        return startingStepCount;
    }

    public Location getDestination()
    {
        return destination;
    }

    public void setDestination(Location destination)
    {
        this.destination = destination;
    }
}
