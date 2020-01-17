package com.ezpass.smopaye_tpe2.vuesUtilisateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.TutorielUtilise;

public class MenuRechargeCompte extends AppCompatActivity {

    LinearLayout rechargeCompte, rechargeAutreCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_recharge_compte);

        //getSupportActionBar().setTitle("Opération de recharge");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rechargeCompte = (LinearLayout) findViewById(R.id.btnRechargeCompte);
        rechargeAutreCompte = (LinearLayout) findViewById(R.id.btnRechargeAutreCompte);

        rechargeCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RechargePropreCompte.class);
                startActivity(intent);
            }
        });

        rechargeAutreCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RechargeAutreCompte.class);
                startActivity(intent);
            }
        });

    }

    /*                    GESTION DU MENU DROIT                  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.apropos) {
            Intent intent = new Intent(getApplicationContext(), Apropos.class);
            startActivity(intent);
        }

        if (id == R.id.tuto) {
            Intent intent = new Intent(getApplicationContext(), TutorielUtilise.class);
            startActivity(intent);
        }

        if(id == R.id.modifierCompte){
            Intent intent = new Intent(getApplicationContext(), ModifierCompte.class);
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
