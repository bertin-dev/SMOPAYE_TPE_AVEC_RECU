package com.ezpass.smopaye_tpe2.Assistance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.R;

public class GrandPublic extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private Button callFromForeign, callFromCameroun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grand_public);

        //getSupportActionBar().setTitle("Service client SMOPAYE");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);

        callFromForeign = (Button) findViewById(R.id.btn_callFromForeign);
        callFromCameroun = (Button) findViewById(R.id.btn_callFromCameroun);


        callFromForeign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makePhoneCall();
                Toast.makeText(GrandPublic.this, "Numéro indisponible pour le moment", Toast.LENGTH_SHORT).show();
            }
        });

        callFromCameroun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }


    private void makePhoneCall() {
        String number = "222231744";
        if (!number.equals("")) {

            if (ContextCompat.checkSelfPermission(GrandPublic.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GrandPublic.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(GrandPublic.this, "Entrez votre Numéro", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Refusé", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
