package com.ezpass.smopaye_tpe2.vuesAccepteur;


import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ezpass.smopaye_tpe2.ChaineConnexion;
import com.ezpass.smopaye_tpe2.Custom_Layout_Historique;
import com.ezpass.smopaye_tpe2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabLayoutScrollable extends Fragment {

    String urladress = "https://web-service-api-smp.ws-smopaye-cm.mon.world/index.php?auth=Card&login=historyChange&dateDeal=2019-10-10&TelDeal=691353455&TypeDeal=Telecollecte&fgfggergJHGS=Iyz4BVU2Hlt0cIeIPBlB7Wq15kMDI4NGRmOTNi&uhtdgG18=d0284df93b";
    String[] montant_valeur;
    String[] donataire_valeur;
    String[] beneficiaire_valeur;
    String[] date_HistoriqueTransaction;
    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;
    private static final SimpleDateFormat sdfNew = new SimpleDateFormat("EEEE, MMM d, yyyy HH:mm:ss a");
    private static final SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    private String dateString = "2019-05-23 00:00:00.0";
    private Date date;

    private String rechargeMaCarte = "Recharge";
    int position;
    int jour, mois, annee;
    String fullDate = "";
    /////////////////////////////////LIRE CONTENU DES FICHIERS////////////////////
    String file = "tmp_number";
    int c;
    String temp_number = "";

   /* public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TabLayoutScrollable tabFragment = new TabLayoutScrollable();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }*/


    public TabLayoutScrollable() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        jour = getArguments().getInt("jour", 0);
        mois = getArguments().getInt("mois", 0);
        annee = getArguments().getInt("annee", 0);
        fullDate = annee+"-"+mois+"-"+jour;

        /////////////////////////////////LECTURE DES CONTENUS DES FICHIERS////////////////////
        try{
            FileInputStream fIn = getActivity().openFileInput(file);

            while ((c = fIn.read()) != -1){
                temp_number = temp_number + Character.toString((char)c);
            }
            //Toast.makeText(getActivity(), temp, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView =  inflater.inflate(R.layout.fragment_tab_layout_scrollable, container, false);

     /*   try {
            // string to date
             date = defaultFormat.parse(dateString);
            int hours = date.getHours();


            String numberAsString = String.valueOf(hours);
            Toast.makeText(getActivity(), numberAsString, Toast.LENGTH_SHORT).show();



            Toast.makeText(getActivity(), defaultFormat.format(date), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), sdfNew.format(date), Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        //getArguments().getInt("jour", 0);
        /*Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("jour", 0)), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("mois", 0)), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("annee", 0)), Toast.LENGTH_SHORT).show();*/

        /*listView = (ListView) convertView.findViewById(R.id.lviewHistorique);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        Custom_Layout_Historique customListView = new Custom_Layout_Historique(getActivity(), montant_valeur, donataire_valeur, beneficiaire_valeur, date_HistoriqueTransaction);
        listView.setAdapter(customListView);*/

        return convertView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("jour", 0)), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("mois", 0)), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), String.valueOf(getArguments().getInt("annee", 0)), Toast.LENGTH_SHORT).show();

        listView = (ListView) view.findViewById(R.id.lviewHistorique);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        Custom_Layout_Historique customListView = new Custom_Layout_Historique(getActivity(), montant_valeur, donataire_valeur, beneficiaire_valeur, date_HistoriqueTransaction);
        listView.setAdapter(customListView);

        //Toast.makeText(getActivity(), String.valueOf((position + 1)), Toast.LENGTH_SHORT).show();
    }

    private void collectData(){
        //connection

        try{

            final Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("auth","Card");
            builder.appendQueryParameter("login", "historyChange");
            builder.appendQueryParameter("dateDeal", fullDate);
            builder.appendQueryParameter("TelDeal", temp_number);
            builder.appendQueryParameter("TypeDeal", "Telecollecte");
            builder.appendQueryParameter("fgfggergJHGS", ChaineConnexion.getEncrypted_password());
            builder.appendQueryParameter("uhtdgG18", ChaineConnexion.getSalt());

            //Connexion au serveur
            //URL url = new URL("http://192.168.20.11:1234/listing.php"+builder.build().toString());
            URL url = new URL(ChaineConnexion.getAdresseURLsmopayeServer() + builder.build().toString());

           // URL url = new URL(urlAddress+"?auth="+param1+"&login="+param2+"&dateDeal="+param3+"&TelDeal="+param4+"&TypeDeal="+param5+"&fgfggergJHGS="+param6+"&uhtdgG18="+param7);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //content
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //JSON
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            montant_valeur = new String[ja.length()];
            donataire_valeur = new String[ja.length()];
            beneficiaire_valeur = new String[ja.length()];
            date_HistoriqueTransaction = new String[ja.length()];

            for(int i=0; i<=ja.length();i++){
                jo = ja.getJSONObject(i);
                montant_valeur[i] = jo.getString("montant") + "F";
                donataire_valeur[i] = jo.getString("donateur");
                beneficiaire_valeur[i] = jo.getString("beneficier");

               // date_HistoriqueTransaction[i] = jo.getString("temps");
                date = defaultFormat.parse(jo.getString("temps") + ".0");
                date_HistoriqueTransaction[i] = date.getHours() + "H " + date.getMinutes() + "min";
            }


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Fragment newInstance(int jour, int mois, int annee)
    {
        TabLayoutScrollable myFragment = new TabLayoutScrollable();
        Bundle args = new Bundle();
        args.putInt("jour", jour);
        args.putInt("mois", mois);
        args.putInt("annee", annee);
        myFragment.setArguments(args);
        return myFragment;
    }

}
