package com.ezpass.smopaye_tpe2.vuesAgent;

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
import com.ezpass.smopaye_tpe2.PayerFacture;
import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.ServicesIndisponible;
import com.ezpass.smopaye_tpe2.vuesAccepteur.ConsulterRecette;
import com.ezpass.smopaye_tpe2.vuesAccepteur.ConsulterSolde;
import com.ezpass.smopaye_tpe2.vuesAccepteur.DebitCarte;
import com.ezpass.smopaye_tpe2.vuesAccepteur.MenuRetraitTelecollecte;
import com.ezpass.smopaye_tpe2.vuesAccepteur.VerifierNumCarte;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.RechargePropreCompte;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.Souscription;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class AccueilFragmentAgent extends Fragment {

    TextView jour, jourSemaine, moisAnnee, categorie, session;
    LinearLayout RechargeAvecCashAgent, ConsultSoldeAgent, CheckCardNumberAgent, btnMenuRechargeTelecollecte, btnDebitCarte, btnConsultationRecette, btnPayerFacture, btnQrCode, btn_souscription;
    private Button consulterHistoriqueAgent;


    /////////////////////////////////LIRE CONTENU DES FICHIERS////////////////////
    String file = "tmp_number";
    int c;
    String temp_number = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_accueil_agent, container, false);

        getActivity().setTitle("Accueil");

        RechargeAvecCashAgent = (LinearLayout) view.findViewById(R.id.btnRechargeAvecCashAgent);
        ConsultSoldeAgent = (LinearLayout) view.findViewById(R.id.btnConsultSoldeAgent);
        CheckCardNumberAgent = (LinearLayout) view.findViewById(R.id.btnCheckCardNumberAgent);
        categorie = (TextView) view.findViewById(R.id.categorieAgent);
        session = (TextView) view.findViewById(R.id.sessionAgent);
        btnMenuRechargeTelecollecte = (LinearLayout) view.findViewById(R.id.btnMenuRechargeTelecollecte);
        btnDebitCarte = (LinearLayout) view.findViewById(R.id.btnDebitCarte);
        btnConsultationRecette = (LinearLayout) view.findViewById(R.id.btnConsultationRecette);
        btn_souscription = (LinearLayout) view.findViewById(R.id.btn_souscription);




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



        //recupération des informations de la BD pendant l'authentificatiion sous forme de SESSION
        //avec les données quittant de Activity -> Fragment
        assert getArguments() != null;
        String retour = getArguments().getString("result_BD");
        assert retour != null;
        String[] parts = retour.split("-");
        String statut1 = parts[2]; // utilisateur, accepteur , agent

        String catAgent = parts[4]; // chauffeur, moto_taxi, bus, cargo
        categorie.setText(catAgent);
        session.setText(statut1);



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

        //MENU RECHARGE
        RechargeAvecCashAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargePropreCompte.class);
                startActivity(intent);
            }
        });

        //MENU CONSULTER SOLDE
        ConsultSoldeAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConsulterSolde.class);
                startActivity(intent);
            }
        });

        //MENU VERIFICATION N° CARTE
        CheckCardNumberAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifierNumCarte.class);
                startActivity(intent);
            }
        });

        //MENU HISTORIQUE
        consulterHistoriqueAgent = (Button) view.findViewById(R.id.consulterHistoriqueAgent);
        consulterHistoriqueAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), MenuHistoriqueTransaction.class);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), ServicesIndisponible.class);
                startActivity(intent);
            }
        });


        //MENU DEBIT
        btnDebitCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), DebitCarte.class);
                startActivity(intent2);
            }
        });

        //MENU TELECOLLECTE
        btnMenuRechargeTelecollecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MenuRetraitTelecollecte.class));
            }
        });

        //MENU RECETTE
        btnConsultationRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ConsulterRecette.class));
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

        btn_souscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Souscription.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
