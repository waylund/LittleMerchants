package com.brotherslynn.littlemerchants;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Player;

import java.util.concurrent.TimeUnit;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        try {
            LoadActivity loadactivity = new LoadActivity();
            loadactivity.execute();
        } catch (Exception ex)
        {
            String blah = ex.getLocalizedMessage();
        }
    }

    private class LoadActivity extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Context appContext = getApplicationContext();

            GameManager gameManager = new GameManager(appContext);
            gameManager.syncLocations();
            Player player = gameManager.getLocalPlayer();
            if (player.getMerchantType() == -1) {
                //redirect to character creation
                try {
                    Intent newIntent = new Intent(appContext, MerchantCreationActivity.class);
                    startActivity(newIntent);
                } catch (Exception ex) {
                    String blah = ex.getLocalizedMessage();
                }
            } else {
                Intent newIntent = new Intent(appContext, MerchantStatusActivity.class);
                startActivity(newIntent);
            }
        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) { // don't care really
                }

            return null;
        }
    }

}
