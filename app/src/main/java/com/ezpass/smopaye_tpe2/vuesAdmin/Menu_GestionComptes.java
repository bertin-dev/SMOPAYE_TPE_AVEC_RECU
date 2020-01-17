package com.ezpass.smopaye_tpe2.vuesAdmin;

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
import com.ezpass.smopaye_tpe2.vuesUtilisateur.ModifierCompte;

public class Menu_GestionComptes extends AppCompatActivity {

    LinearLayout accepteur, userAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__gestion_comptes);

        //getSupportActionBar().setTitle("Gestion des Comptes");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accepteur = (LinearLayout) findViewById(R.id.btnGesCommercants);
        userAgent = (LinearLayout) findViewById(R.id.btnGesCommerciaux);


        accepteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GestionAccepteur.class);
                startActivity(intent);
            }
        });


        userAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GestionUtilisateursAgents.class);
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
