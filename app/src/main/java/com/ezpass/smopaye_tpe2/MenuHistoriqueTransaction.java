package com.ezpass.smopaye_tpe2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.ModifierCompte;

public class MenuHistoriqueTransaction extends AppCompatActivity {

    private LinearLayout historiqueRechargeMaCarte, historiqueRechargePourAgent, historiqueDebitDeCarte, historiqueRechargeUneAutreCarte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_historique_transaction);


        getSupportActionBar().setTitle("Historique de transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        /*Intent intent = getIntent();
        telephone = intent.getStringExtra("telephone");

        Toast.makeText(this, telephone, Toast.LENGTH_SHORT).show();*/




        historiqueRechargeMaCarte = (LinearLayout) findViewById(R.id.historiqueRechargeMaCarte);
        historiqueRechargeMaCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoriqueTransactions.class));
            }
        });

        historiqueRechargePourAgent = (LinearLayout) findViewById(R.id.historiqueRechargePourAgent);
        historiqueRechargePourAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuHistoriqueTransaction.this, "Historique en cours de traitement", Toast.LENGTH_SHORT).show();
            }
        });

        historiqueDebitDeCarte = (LinearLayout) findViewById(R.id.historiqueDebitDeCarte);
        historiqueDebitDeCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuHistoriqueTransaction.this, "Historique en cours de traitement", Toast.LENGTH_SHORT).show();
            }
        });

        historiqueRechargeUneAutreCarte = (LinearLayout) findViewById(R.id.historiqueRechargeUneAutreCarte);
        historiqueRechargeUneAutreCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuHistoriqueTransaction.this, "Historique en cours de traitement", Toast.LENGTH_SHORT).show();
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
