package com.ezpass.smopaye_tpe2.vuesAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.ServicesIndisponible;
import com.ezpass.smopaye_tpe2.vuesAccepteur.VerifierNumCarte;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.RechargePropreCompte;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.Souscription;

import java.text.DateFormat;
import java.util.Calendar;

public class AccueilFragmentAdmin  extends Fragment {

    TextView jour, jourSemaine, moisAnnee;
    LinearLayout GesCompt, CheckCardNumber, ConsulterSolde, RechargeAvecCashAdmin;
    FloatingActionButton Register;
    private Button consulterHistoriqueAdmin;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_accueil_admin, container, false);

        Register = view.findViewById(R.id.btnRegister);
        GesCompt = (LinearLayout) view.findViewById(R.id.btnGesCompt);
        CheckCardNumber = (LinearLayout) view.findViewById(R.id.btnCheckCardNumber);
        ConsulterSolde = (LinearLayout) view.findViewById(R.id.btnConsulterSolde);
        RechargeAvecCashAdmin = (LinearLayout) view.findViewById(R.id.btnRechargeAvecCashAdmin);

        jour = (TextView) view.findViewById(R.id.jour);
        jourSemaine = (TextView) view.findViewById(R.id.jourSemaine);
        moisAnnee   = (TextView) view.findViewById(R.id.moisAnnee);


        //GESTION DES DATE DU MENU
        Calendar calendar = Calendar.getInstance();
        // String currentDate = DateFormat.getDateInstance().format(calendar.getTime());// 31.07.2019
        String currentDate2 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        String[] part =currentDate2.split(" ");
        if(part[0].equalsIgnoreCase(currentDate2)){
            Toast.makeText(getActivity(), "la date est en Anglais", Toast.LENGTH_SHORT).show();
        }
        else {
            jourSemaine.setText(part[0]);
            String Day = "0" + part[1];
            if(Integer.parseInt(part[1]) < 10)
                jour.setText(Day);
            else
                jour.setText(part[1]);
            moisAnnee.setText(String.format("%s %s", part[2], part[3]));
        }


        //GESTION DES EVENEMENTS SUR LES BOUTONS DU MENU
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Souscription.class);
               // intent.putExtra("Accepteur",numTelephone.getText().toString().trim());
                startActivity(intent);
            }
        });

        GesCompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menu_GestionComptes.class);
                startActivity(intent);
            }
        });

        CheckCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifierNumCarte.class);
                startActivity(intent);
            }
        });

        ConsulterSolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.ezpass.smopaye_tpe2.vuesAccepteur.ConsulterSolde.class);
                startActivity(intent);
            }
        });


        RechargeAvecCashAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargePropreCompte.class);
                startActivity(intent);
            }
        });

        consulterHistoriqueAdmin = (Button) view.findViewById(R.id.consulterHistoriqueAdmin);
        consulterHistoriqueAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), MenuHistoriqueTransaction.class);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), ServicesIndisponible.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
