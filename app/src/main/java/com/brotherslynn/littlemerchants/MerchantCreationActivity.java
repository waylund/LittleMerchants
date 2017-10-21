package com.brotherslynn.littlemerchants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.brotherslynn.littlemerchants.objects.GameManager;
import com.brotherslynn.littlemerchants.objects.Player;

public class MerchantCreationActivity extends AppCompatActivity {

    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_creation);

        gameManager = new GameManager(getApplicationContext());

        final Spinner merchantTypeSpinner = (Spinner) findViewById(R.id.merchantTypeSpinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, gameManager.getLocalization().getMerchantType());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        merchantTypeSpinner.setAdapter(aa);

        merchantTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                changeMerchantDescriptionText(merchantTypeSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Button startPlayingButton = (Button) findViewById(R.id.CreateMerchantButton);

        startPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartPlayingListener(view);
            }
        });

    }

    private void changeMerchantDescriptionText(int merchantType)
    {
        TextView descriptionText =  (TextView) findViewById(R.id.merchantTypeDescription);
        descriptionText.setText(gameManager.getLocalization().getMerchantTypeDescriptions()[merchantType]);
    }

    private void onStartPlayingListener(View view)
    {
        final EditText merchantNameText = (EditText) findViewById(R.id.editMerchantName);
        final Spinner merchantTypeSpinner = (Spinner) findViewById(R.id.merchantTypeSpinner);

        Player localPlayer = gameManager.getLocalPlayer();
        localPlayer.setMerchantName(merchantNameText.getText().toString());
        localPlayer.setMerchantType(merchantTypeSpinner.getSelectedItemPosition());

        boolean didSave = gameManager.savePlayer(localPlayer);
        if (didSave) {

            //Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
            //startActivity(intent);
            finish();
        }

    }
}
