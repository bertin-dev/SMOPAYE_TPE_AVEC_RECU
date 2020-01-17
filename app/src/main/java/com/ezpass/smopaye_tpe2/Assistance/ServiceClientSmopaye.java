package com.ezpass.smopaye_tpe2.Assistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.WebViewMonetbil;

public class ServiceClientSmopaye extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_client_smopaye);

        //getSupportActionBar().setTitle("Assistance");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayout grandPublic = (LinearLayout) findViewById(R.id.grandPublic);
       // LinearLayout clientEntreprise = (LinearLayout) findViewById(R.id.clientEntreprise);
        LinearLayout clientFacebook = (LinearLayout) findViewById(R.id.clientFacebook);
        LinearLayout clientTwitter = (LinearLayout) findViewById(R.id.clientTwitter);

        grandPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GrandPublic.class));
            }
        });


        /*clientEntreprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceClientSmopaye.this, "2", Toast.LENGTH_SHORT).show();
            }
        });*/

        clientFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebViewMonetbil.class);
                intent.putExtra("urlMonetbil", "https://facebook.com");
                startActivity(intent);
            }
        });

        clientTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebViewMonetbil.class);
                intent.putExtra("urlMonetbil", "https://twitter.com");
                startActivity(intent);
            }
        });

    }



    /*                    GESTION DU MENU DROIT                  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_assistance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {
            Intent intent = new Intent(getApplicationContext(), Menu_Assistance.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}
