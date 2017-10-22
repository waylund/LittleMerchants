package com.brotherslynn.littlemerchants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Localization;
import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import java.util.UUID;

public class MerchantStatusActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    GameManager gameManager;
    Localization localization;
    boolean activityRunning;
    TextView destination, money, travel, currentLoc;
    ProgressBar progress;
    Player localPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_status);

        // Step counter - initialize sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        gameManager = new GameManager(getApplicationContext());
        localization = gameManager.getLocalization();
        localPlayer = gameManager.getLocalPlayer();

        TextView helloView = (TextView) findViewById(R.id.labelHelloMessage);
        helloView.setText(localization.getGreeting() + localPlayer.getMerchantName() + "!");

        refreshLocations();

        money = (TextView) findViewById(R.id.labelMoney);


        Button resetPlayerLocationButton = (Button) findViewById(R.id.buttonResPlayerLoc);

        resetPlayerLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPlayerLocation(view);
            }
        });

        Button addDestinationButton = (Button) findViewById(R.id.buttonAddDestination);

        addDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDestination(view);
            }
        });


    }

    private void refreshLocations()
    {
        currentLoc = (TextView) findViewById(R.id.labelValueCurrentLocation);
        Location current = localPlayer.getCurrentLocation();
        if (current == null)
            currentLoc.setText("On the Road");
        else
            currentLoc.setText(current.getName());

        destination = (TextView) findViewById(R.id.labelValueDestination);
        travel = (TextView) findViewById(R.id.labelTravelCounter);
        progress = (ProgressBar) findViewById(R.id.progressBarTravel);
        Trip currentTrip = localPlayer.getTrip();
        if (currentTrip == null) {
            destination.setText("None");
            travel.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        }
        else {
            destination.setText(currentTrip.getDestination().getName());
            travel.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            travel.setText(currentTrip.getSteps() + " / " + currentTrip.getDistance() + " Steps");
            progress.setMax(100);
            progress.setProgress((int) Math.ceil(currentTrip.getSteps()/currentTrip.getDistance()));
        }
    }

    private void resetPlayerLocation(View view)
    {
        Location loc = gameManager.getLocation(UUID.fromString("b73f1468-b85f-4cb7-aa3d-950c6ce19bf2"));
        localPlayer.setCurrentLocation(loc);
        localPlayer.setTrip(null);
        gameManager.savePlayer(localPlayer);
        refreshLocations();
    }

    private void setDestination(View view)
    {
        Location loc = gameManager.getLocation(UUID.fromString("7e26f6dd-7343-4878-9076-c2b2b1758489"));
        gameManager.startNewTrip(localPlayer, localPlayer.getCurrentSteps(), loc);
        gameManager.savePlayer(localPlayer);
        refreshLocations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {

            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            int currentSteps = (int) event.values[0];
            localPlayer.setCurrentSteps(currentSteps);
            Trip thisTrip = localPlayer.getTrip();
            if (thisTrip != null)
            {
                gameManager.addStepsToTrip(localPlayer, currentSteps - thisTrip.getCurrentStepCount());
                travel.setText(String.valueOf(thisTrip.getSteps()) + " Steps");
                refreshLocations();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
