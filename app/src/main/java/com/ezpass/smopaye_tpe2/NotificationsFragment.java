package com.ezpass.smopaye_tpe2;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.DBLocale_Notifications.DbHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsFragment extends Fragment {


    // Array of strings...
    ListView simpleList;
    String  Item[] = {"Télécollecte", "Carte smopaye", "Recharge", "Retrait", "Retrait", "Recharge"};
    String  SubItem[] = {"Une Télécollecte sur un montant de 10 000 fcfa a été éffectué.",
            "Votre carte s'expire le 29/09/19 veuillez passer mettre à jour.",
            "Votre recharge a échoué carte invalid. Veuillez vous rapprocher.",
            "Votre retrait aupres de votre opérateur orange a reussit.",
            "Votre retrait aupres de votre opérateur orange a reussit.",
            "Votre retrait aupres de votre opérateur orange a reussit."};
    int flags[] = {R.drawable.ic_notifications_black_48dp, R.drawable.ic_notifications_red_48dp, R.drawable.ic_notifications_red_48dp, R.drawable.ic_notifications_black_48dp, R.drawable.ic_notifications_red_48dp, R.drawable.ic_notifications_black_48dp};
    String  ItemDate[] = {"01/08/2019", "02/08/2019", "03/08/2019", "04/08/2019", "05/08/2019", "06/08/2019"};

    private FloatingActionButton btnSuppNotif;
    private LinearLayout notificationsVides;


//https://web-service-api-smp.ws-smopaye-cm.mon.world/index.php
    String urladress = "http://bertin-mounok.com/soccer.php";
    BufferedInputStream is;
    String line = null;
    String result = null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_notifications, container, false);

        getActivity().setTitle("Notifications");


        btnSuppNotif = (FloatingActionButton) view.findViewById(R.id.btnSuppNotif);
        simpleList = (ListView) view.findViewById(R.id.ListViewNotification);
        notificationsVides = (LinearLayout) view.findViewById(R.id.notificationsVides);



        //recupération des informations de la BD pendant l'authentificatiion sous forme de SESSION
        //avec les données quittant de Activity -> Fragment
        /*assert getArguments() != null;
        String retour = getArguments().getString("result_BD");
        assert retour != null;
        String[] parts = retour.split("-");*/

        //numTel = getArguments().getString("telephone");


        //BASE DE DONNEES DISTANTE
        /*******************************DEBUT******************************/
        /*simpleList = (ListView) view.findViewById(R.id.ListViewNotification);
        //cela fontionne bien. faut juste enlever les commentaires en dessous
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        //collectData();
        CustomAdapterNotification customAdapter = new CustomAdapterNotification(getActivity(), Item,SubItem, flags, ItemDate);
        simpleList.setAdapter(customAdapter);*/
        /********************************FIN*******************************/

        //BASE DE DONNEES LOCALE
        final DbHandler db = new DbHandler(getActivity());
        final ArrayList<HashMap<String, String>> userList = db.GetUsers();

        if(userList.isEmpty()){
            notificationsVides.setVisibility(View.VISIBLE);
            simpleList.setVisibility(View.GONE);

        } else {
            notificationsVides.setVisibility(View.GONE);
            simpleList.setVisibility(View.VISIBLE);
            ListAdapter adapter = new SimpleAdapter(getActivity(), userList, R.layout.listitem_notification, new String[]{"title","description", "image_notification", "dateEnreg"}, new int[]{R.id.item, R.id.subitem, R.id.image, R.id.itemDate});
            simpleList.setAdapter(adapter);
        }




        btnSuppNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userList.isEmpty()){
                    notificationsVides.setVisibility(View.VISIBLE);
                    simpleList.setVisibility(View.GONE);
                } else {
                    db.DeleteAllNotifications();
                    ArrayList<HashMap<String, String>> userList = db.GetUsers();
                    ListAdapter adapter = new SimpleAdapter(getActivity(), userList, R.layout.listitem_notification, new String[]{"title","description", "image_notification", "dateEnreg"}, new int[]{R.id.item, R.id.subitem, R.id.image, R.id.itemDate});
                    simpleList.setAdapter(adapter);
                    Toast.makeText(getContext(), "vos notifications ont été vidé.", Toast.LENGTH_SHORT).show();

                    notificationsVides.setVisibility(View.VISIBLE);
                    simpleList.setVisibility(View.GONE);
                }

            }
        });

        return view;
    }



    private void collectData(){
        //connection
        try{
            //parametres
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("nom","Vini");

            URL url = new URL(urladress + builder.build().toString());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            /*con.setConnectTimeout(5000);
            con.setRequestMethod("POST");
            con.connect();*/

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

            /*String data = "";
            while (br.ready() || data=="")
            {
                data+= br.readLine();
            }*/

            is.close();
            result=sb.toString();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //JSON
        /*try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            Item = new String[ja.length()];
            SubItem = new String[ja.length()];
           // flags = new String[ja.length()];
            ItemDate = new String[ja.length()];

            for(int i=0; i<=ja.length();i++){
                jo = ja.getJSONObject(i);
                Item[i] = jo.getString("name");
                SubItem[i] = jo.getString("email");
                ItemDate[i] = jo.getString("dates");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }*/
    }

    /*private  void recharg(final String adresse){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //********************DEBUT***********
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // On ajoute un message à notre progress dialog
                            progressDialog.setMessage("Connexion au serveur");
                            // On donne un titre à notre progress dialog
                            progressDialog.setTitle("Attente d'une réponse");
                            // On spécifie le style
                            //  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            // On affiche notre message
                            progressDialog.show();
                            //build.setPositiveButton("ok", new View.OnClickListener()
                        }
                    });
                    //*******************FIN*****
                    Uri.Builder builder = new Uri.Builder();
                    builder.appendQueryParameter("montant",montant.getText().toString().trim());
                    builder.appendQueryParameter("numcarte",numCarte.getText().toString().trim());


                    URL url = new URL(adresse+builder.build().toString());//"http://192.168.20.11:1234/recharge.php"
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();


                    InputStream inputStream = httpURLConnection.getInputStream();

                    final BufferedReader bufferedReader  =  new BufferedReader(new InputStreamReader(inputStream));

                    String string="";
                    String data="";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RechargePropreCompte.this, "ooooo", Toast.LENGTH_SHORT).show();
                        }
                    });

                    while (bufferedReader.ready() || data==""){
                        data+=bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    inputStream.close();


                    final String f = data;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();


                            View view = LayoutInflater.from(RechargePropreCompte.this).inflate(R.layout.alert_dialog_success, null);
                            TextView title = (TextView) view.findViewById(R.id.title);
                            TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                            ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                            title.setText("Etat de votre Compte");
                            imageButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            statutOperation.setText(f);
                            build_error.setPositiveButton("OK", null);
                            build_error.setCancelable(false);
                            build_error.setView(view);
                            build_error.show();
                            notifications("Recharge Propre Compte", f);
                            //Toast.makeText(getApplicationContext(), "login, mot de passe ou numéro de carte incorrect !!!", Toast.LENGTH_LONG).show();
                        }
                    });


                    //    JSONObject jsonObject = new JSONObject(data);
                    //  jsonObject.getString("status");
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i=0;i<jsonArray.length();i++){
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(RechargePropreCompte.this, jsonObject.getString("telephone"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RechargePropreCompte.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                    try {
                        Thread.sleep(2000);
                        //Toast.makeText(RechargePropreCompte.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
