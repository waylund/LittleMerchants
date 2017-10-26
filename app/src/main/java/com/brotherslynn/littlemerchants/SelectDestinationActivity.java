package com.brotherslynn.littlemerchants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;

import java.util.List;

public class SelectDestinationActivity extends AppCompatActivity {

    Player localPlayer;
    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        gameManager = new GameManager(getApplicationContext());
        localPlayer = gameManager.getLocalPlayer();
        List<Location> possibleDestinations = gameManager.getConnectedLocations(localPlayer.getCurrentLocation());

        LinearLayout destinationView = (LinearLayout) findViewById(R.id.destinationSelectArea);


        for (int i = 0; i < possibleDestinations.size(); i++)
        {
            final Location loc = possibleDestinations.get(i);
            Button destinationButton = new Button(getApplicationContext());
            destinationButton.setText(loc.getName());
            destinationView.addView(destinationButton);

            destinationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDestination(view, loc);
                }
            });

        }
    }

    private void setDestination(View view, Location location)
    {
        boolean didStartTrip = gameManager.startNewTrip(localPlayer, localPlayer.getCurrentSteps(), location);
        if (didStartTrip)
        {
            gameManager.savePlayer(localPlayer);
            finish();
        }
    }
}
