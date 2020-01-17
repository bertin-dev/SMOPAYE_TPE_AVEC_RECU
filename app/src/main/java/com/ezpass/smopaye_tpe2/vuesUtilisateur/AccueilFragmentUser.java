package com.ezpass.smopaye_tpe2.vuesUtilisateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.MenuQRCode;
import com.ezpass.smopaye_tpe2.MenuRetraitOperateur;
import com.ezpass.smopaye_tpe2.PayerFacture;
import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.ServicesIndisponible;
import com.ezpass.smopaye_tpe2.vuesAccepteur.ConsulterSolde;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class AccueilFragmentUser extends Fragment {

    TextView jour, jourSemaine, moisAnnee;
    private LinearLayout btnRecharge, btnConsulter, btnPayerFacture, btnQrCode, btnRetraitOperateur;
    //private LinearLayout CheckCardNumber;
    private Button consulterHistoriqueUser;

    /////////////////////////////////LIRE CONTENU DES FICHIERS////////////////////
    String file = "tmp_number";
    int c;
    String temp_number = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_accueil_user, container, false);

        btnRecharge = (LinearLayout) view.findViewById(R.id.btnRecharger);
        btnConsulter = (LinearLayout) view.findViewById(R.id.btnConsultSolde);
        //CheckCardNumber = (LinearLayout) view.findViewById(R.id.btnCheckCardNumber);


        jour = (TextView) view.findViewById(R.id.jour);
        jourSemaine = (TextView) view.findViewById(R.id.jourSemaine);
        moisAnnee   = (TextView) view.findViewById(R.id.moisAnnee);

        /////////////////////////////////LECTURE DES CONTENUS DES FICHIERS////////////////////
        try{
            FileInputStream fIn = getActivity().openFileInput(file);
            while ((c = fIn.read()) != -1){
                temp_number = temp_number + Character.toString((char)c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


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

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargePropreCompte.class);
                startActivity(intent);
            }
        });

        btnConsulter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConsulterSolde.class);
                startActivity(intent);
            }
        });

        /*CheckCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifierNumCarte.class);
                startActivity(intent);
            }
        });*/


        //GESTION DES EVENEMENTS SUR LES BOUTONS DU MENU

        consulterHistoriqueUser = (Button) view.findViewById(R.id.consulterHistoriqueUser);
        consulterHistoriqueUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), MenuHistoriqueTransaction.class);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), ServicesIndisponible.class);
                startActivity(intent);
            }
        });


        //paiement des factures
        btnPayerFacture = (LinearLayout) view.findViewById(R.id.btnPayerFacture);
        btnPayerFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PayerFacture.class);
                intent.putExtra("telephone", temp_number);
                startActivity(intent);
            }
        });

        //paiement par Code QR
        btnQrCode = (LinearLayout) view.findViewById(R.id.btnQrCode);
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuQRCode.class);
                startActivity(intent);

                /*IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan Encours...");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();*/
            }
        });


        //paiement des factures
        btnRetraitOperateur = (LinearLayout) view.findViewById(R.id.btnRetraitOperateur);
        btnRetraitOperateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuRetraitOperateur.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

