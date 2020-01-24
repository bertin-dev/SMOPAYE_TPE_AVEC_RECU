package com.ezpass.smopaye_tpe2.vuesUtilisateur;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.ezpass.smopaye_tpe2.ChaineConnexion;
import com.ezpass.smopaye_tpe2.DBLocale_Notifications.DbHandler;
import com.ezpass.smopaye_tpe2.DBLocale_Notifications.DbUser;
import com.ezpass.smopaye_tpe2.NotifApp;
import com.ezpass.smopaye_tpe2.NotifReceiver;
import com.ezpass.smopaye_tpe2.QRCodeShow;
import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.RemoteFragments.APIService;
import com.ezpass.smopaye_tpe2.RemoteModel.User;
import com.ezpass.smopaye_tpe2.RemoteNotifications.Client;
import com.ezpass.smopaye_tpe2.RemoteNotifications.Data;
import com.ezpass.smopaye_tpe2.RemoteNotifications.MyResponse;
import com.ezpass.smopaye_tpe2.RemoteNotifications.Sender;
import com.ezpass.smopaye_tpe2.RemoteNotifications.Token;
import com.ezpass.smopaye_tpe2.TutorielUtilise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.nfc.Nfc;
import com.telpo.tps550.api.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.ezpass.smopaye_tpe2.ChaineConnexion.getsecurity_keys;
import static com.ezpass.smopaye_tpe2.NotifApp.CHANNEL_ID;

public class Souscription extends AppCompatActivity {

    private EditText nom,prenom,telephone,cni,numCarte, adresse;
    private Spinner sexe, statut, typeChauffeur, typePjustificative;
    private Button btnEnregistrer, btnAnnuler, btnOpenNFC;
    private ProgressDialog progressDialog;
    /////////////////////////////////////////////////////////////////////////////////
    Handler handler;
    Runnable runnable;
    Timer timer;
    Thread readThread;
    private byte blockNum_1 = 1;
    private byte blockNum_2 = 2;
    private final int CHECK_NFC_TIMEOUT = 1;
    private final int SHOW_NFC_DATA = 2;
    long time1, time2;
    private final byte B_CPU = 3;
    private final byte A_CPU = 1;
    private final byte A_M1 = 2;
    Nfc nfc = new Nfc(this);
    DialogInterface dialog;
    AlertDialog.Builder build_error;


    private CheckBox AbonnementMensuel;
    private CheckBox AbonnementHebdomadaire;
    private CheckBox AbonnementService;


    //BD LOCALE
    private DbHandler dbHandler;
    private DbUser dbUser;
    private Date aujourdhui;
    private DateFormat shortDateFormat;



    //SERVICES GOOGLE FIREBASE
    /*FirebaseAuth auth;
    DatabaseReference reference;
    APIService apiService;
    FirebaseUser fuser;*/


    private String abonnement = "service", nom_prenom = "";


    private LinearLayout internetIndisponible, authWindows;
    private Button btnReessayer;
    ImageView conStatusIv;
    TextView titleNetworkLimited, msgNetworkLimited;

    /////////////////////////////////LIRE CONTENU DES FICHIERS////////////////////
    String file = "tmp_number";
    int c;
    String temp_number = "";


    BufferedInputStream is;
    String line = null;
    String result = null;


    String[] id_session;
    String[] nom_session;

    String[] IDCathegorie;
    String[] NOMCath;
    String[] typeuser;

    ArrayList<String> listStatut = new ArrayList<>();
    List<String> idStatut = new ArrayList<String>();

    ArrayList<String> listChauffeur = new ArrayList<>();
    List<String> idChauffeur = new ArrayList<String>();
    List<String> typeUserChauffeur = new ArrayList<String>();

    String num_statut = "";
    String num_categorie = "";


    ArrayList<String> listUser = new ArrayList<>();
    List<String> idUser = new ArrayList<String>();
    List<String> typeUser = new ArrayList<String>();


    ArrayList<String> autreUser = new ArrayList<>();
    List<String> idAutreUser = new ArrayList<String>();
    List<String> typeAutreUser = new ArrayList<String>();

    ArrayList<String> allListID = new ArrayList<>();
    String[] allID;



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // new loadDataSpinner().execute();


        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //chargement et affichage des données
        LoadDBStatutInSpinner();
        //statut de la session
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.spinner_item, listStatut);
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        statut.setAdapter(spinnerArrayAdapter1);

        //chargement sans affichage des données
        LoadDBCategorieInpinner();

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_souscription);

        //getSupportActionBar().setTitle("Souscription");
        //getSupportParentActivityIntent().putExtra("resultatBD", "Administrateur");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressDialog = new ProgressDialog(Souscription.this);
        build_error = new AlertDialog.Builder(Souscription.this);


        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        sexe = (Spinner) findViewById(R.id.sexe);
        telephone = (EditText) findViewById(R.id.numeroTel);
        cni = (EditText) findViewById(R.id.cni);
        statut = (Spinner) findViewById(R.id.statut);
        numCarte = (EditText) findViewById(R.id.numCarte);
        btnEnregistrer = (Button) findViewById(R.id.btnInscription);
        //btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
        btnOpenNFC = (Button) findViewById(R.id.btnOpenNFC);
        // operateur = (Spinner) findViewById(R.id.operateurs);

        typeChauffeur = (Spinner) findViewById(R.id.typeChauffeur);

        adresse = (EditText) findViewById(R.id.adresse);
        AbonnementMensuel = (CheckBox) findViewById(R.id.AbonnementMensuel);
        AbonnementHebdomadaire = (CheckBox) findViewById(R.id.AbonnementHebdomadaire);
        AbonnementService = (CheckBox) findViewById(R.id.AbonnementService);
        typePjustificative = (Spinner) findViewById(R.id.typePjustificative);

        authWindows = (LinearLayout) findViewById(R.id.authWindows);
        internetIndisponible = (LinearLayout) findViewById(R.id.internetIndisponible);
        btnReessayer = (Button) findViewById(R.id.btnReessayer);
        conStatusIv = (ImageView) findViewById(R.id.conStatusIv);
        titleNetworkLimited = (TextView) findViewById(R.id.titleNetworkLimited);
        msgNetworkLimited = (TextView) findViewById(R.id.msgNetworkLimited);

        //SERVICE GOOGLE FIREBASE
        /*auth = FirebaseAuth.getInstance();
        apiService = Client.getClient(ChaineConnexion.getAdresseURLGoogleAPI()).create(APIService.class);
        fuser = FirebaseAuth.getInstance().getCurrentUser();*/









        // Initializing a String Array
        /*String[] statut1 = new String[]{
                "Utilisateur",
                "Agent",
                "Administrateur",
                "Accepteur"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,statut1);
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        statut.setAdapter(spinnerArrayAdapter1);*/


        // Initializing a String Array
        /*String[] typeChauffeur1 = new String[]{
                "moto_taxi",
                "Chauffeur",
                "cargo",
                "bus inter urbain",
                "Commerçant",
                "restaurant etudiant",
                "Chauffeur",
                "autre"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,typeChauffeur1);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(spinnerArrayAdapter);*/


        // Initializing a String Array
        String[] sexe1 = new String[]{
                "Masculin",
                "Feminin"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,sexe1);
        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_item);
        sexe.setAdapter(spinnerArrayAdapter3);


        // Initializing a String Array
        String[] pieceJ = new String[]{
                "CNI",
                "passport",
                "recipissé",
                "carte de séjour",
                "carte d'étudiant"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,pieceJ);
        spinnerArrayAdapter4.setDropDownViewResource(R.layout.spinner_item);
        typePjustificative.setAdapter(spinnerArrayAdapter4);


        /////////////////////////////////LECTURE DES CONTENUS DES FICHIERS////////////////////
        try{
            FileInputStream fIn = getApplicationContext().openFileInput(file);
            while ((c = fIn.read()) != -1){
                temp_number = temp_number + Character.toString((char)c);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //VERIFICATION DE L'ETAT DU CHANGEMENT DE STATUT
        /*statut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(statut.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("accepteur")){
                    //typeChauffeur.setVisibility(View.VISIBLE);
                    addItemsOnSpinner2();
                }
                else if(statut.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("utilisateur")){
                    //typeChauffeur.setVisibility(View.VISIBLE);
                    addItemsOnSpinner3();
                }
                else {
                    addItemsOnSpinner1();
                    // typeChauffeur.setVisibility(View.GONE);
                    //typeChauffeur.setAd
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });*/

        statut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Accepteur id = 2
                if(idStatut.get(position).equalsIgnoreCase("2")){
                    addItemsOnSpinner2();
                    num_statut = idStatut.get(position);
                    //Toast.makeText(Souscription.this, num_statut, Toast.LENGTH_SHORT).show();
                }
                //Utilisateur id = 1
                else if(idStatut.get(position).equalsIgnoreCase("1")){
                    addItemsOnSpinner3();
                    num_statut = idStatut.get(position);
                    //Toast.makeText(Souscription.this, num_statut, Toast.LENGTH_SHORT).show();
                }
                //Adminitrateur id = 3
                else if(idStatut.get(position).equalsIgnoreCase("3")){
                    addItemsOnSpinner1();
                    num_statut = idStatut.get(position);
                    //Toast.makeText(Souscription.this, num_statut, Toast.LENGTH_SHORT).show();
                }
                //Agent id = 4 et autre
                else {
                    addItemsOnSpinner1();
                    num_statut = idStatut.get(position);
                    //Toast.makeText(Souscription.this, num_statut, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        typeChauffeur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                /*for(String idd : idUser){
                    if(idd.equalsIgnoreCase(idUser.get(position))){
                        num_categorie = idd;
                        Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                    }
                }*/

                if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("particulier")){
                    num_categorie = "41";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("étudiant")){
                    num_categorie = "42";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("élève")){
                    num_categorie = "43";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("moto_taxis")){
                    num_categorie = "7";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("chauffeur")){
                    num_categorie = "8";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("mini-bus")){
                    num_categorie = "9";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("bus inter urbain")){
                    num_categorie = "10";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("restaurant étudiant")){
                    num_categorie = "12";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                } else if(typeChauffeur.getSelectedItem().toString().toLowerCase().equalsIgnoreCase("smopaye")){
                    num_categorie = "33";
                    //Toast.makeText(Souscription.this, num_categorie, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        btnEnregistrer.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                if(nom.getText().toString().trim().equals("") || prenom.getText().toString().trim().equals("") || telephone.getText().toString().trim().equals("")
                        || cni.getText().toString().trim().equals("") || adresse.getText().toString().trim().equals("") || numCarte.getText().toString().trim().equals("")) {
                    Toast.makeText(Souscription.this, getString(R.string.champsVide), Toast.LENGTH_SHORT).show();

                    View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                    title.setText(getString(R.string.information));
                    imageButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                    statutOperation.setText(getString(R.string.champsVide));
                    build_error.setPositiveButton("OK", null);
                    build_error.setCancelable(false);
                    build_error.setView(view);
                    build_error.show();


                }else{
                    if (isValid(nom.getText().toString().trim()) && isValid(prenom.getText().toString().trim()) && isValid(cni.getText().toString().trim())
                            && isValid(adresse.getText().toString().trim())) {
                        if(telephone.length()==9){

                            //---------------DEBUT---------------------------------
                            //Toast.makeText(Souscription.this, nom.getText().toString() + " im here " + prenom.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        //********************DEBUT***********
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // On ajoute un message à notre progress dialog
                                                progressDialog.setMessage(getString(R.string.connexionserver));
                                                // On donne un titre à notre progress dialog
                                                progressDialog.setTitle(getString(R.string.attenteReponseServer));
                                                // On spécifie le style
                                                //  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                // On affiche notre message
                                                progressDialog.show();
                                                //build.setPositiveButton("ok", new View.OnClickListener()
                                            }
                                        });
                                        //*******************FIN*****

                                        Uri.Builder builder = new Uri.Builder();
                                        builder.appendQueryParameter("enregUser","users");
                                        builder.appendQueryParameter("enregReg", "register");
                                        builder.appendQueryParameter("NOM", nom.getText().toString().trim().toLowerCase());
                                        builder.appendQueryParameter("PRENOM", prenom.getText().toString().trim().toLowerCase());
                                        builder.appendQueryParameter("GENRE", sexe.getSelectedItem().toString().trim().toUpperCase());
                                        builder.appendQueryParameter("TELEPHONE", telephone.getText().toString().trim());
                                        builder.appendQueryParameter("CNI", typePjustificative.getSelectedItem().toString().trim()+"-"+cni.getText().toString().trim());
                                        //builder.appendQueryParameter("sessioncompte", statut.getSelectedItem().toString().trim());
                                        builder.appendQueryParameter("sessioncompte", num_statut);
                                        builder.appendQueryParameter("Adresse", adresse.getText().toString().trim());
                                        builder.appendQueryParameter("IDCARTE", numCarte.getText().toString().trim());
                                        //builder.appendQueryParameter("IDCathegorie", typeChauffeur.getSelectedItem().toString().trim());
                                        builder.appendQueryParameter("IDCathegorie", num_categorie);
                                        builder.appendQueryParameter("typeAbon", abonnement);
                                        builder.appendQueryParameter("uniquser", temp_number);

                                        builder.appendQueryParameter("fgfggergJHGS", ChaineConnexion.getEncrypted_password());
                                        builder.appendQueryParameter("uhtdgG18",ChaineConnexion.getSalt());
                                        //builder.appendQueryParameter("operateur", operateur.getSelectedItem().toString());

                                        //URL url = new URL("http://192.168.20.6:1234/index.php"+builder.build().toString());
                                        URL url = new URL(ChaineConnexion.getAdresseURLsmopayeServer() + builder.build().toString());

                                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                        httpURLConnection.setConnectTimeout(5000);
                                        httpURLConnection.setRequestMethod("POST");
                                        httpURLConnection.connect();


                                        InputStream inputStream = httpURLConnection.getInputStream();

                                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                                        String string = "";
                                        String data = "";
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Souscription.this, "Encours de traitement...", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        while (bufferedReader.ready() || data == "") {
                                            data += bufferedReader.readLine();
                                        }
                                        bufferedReader.close();
                                        inputStream.close();


                                        final String f = data.toLowerCase().trim();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();

                                                int pos = f.toLowerCase().indexOf("success");
                                                if (pos >= 0) {

                                                    /*registerGoogleFirebase(nom.getText().toString().trim(), prenom.getText().toString().trim(),sexe.getSelectedItem().toString().trim(),
                                                            telephone.getText().toString().trim(),  typePjustificative.getSelectedItem().toString().trim(), cni.getText().toString().trim(), statut.getSelectedItem().toString().trim(),
                                                            adresse.getText().toString().trim(), numCarte.getText().toString().trim(), typeChauffeur.getSelectedItem().toString().trim(),
                                                            "sm" + telephone.getText().toString().trim() + "@smopaye.cm", telephone.getText().toString().trim(), "default", "offline" ,f);*/



                                                    ////////////////////INITIALISATION DE LA BASE DE DONNEES LOCALE/////////////////////////
                                                    dbHandler = new DbHandler(getApplicationContext());
                                                    dbUser = new DbUser(getApplicationContext());
                                                    aujourdhui = new Date();
                                                    shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

                                                    //////////////////////////////////NOTIFICATIONS////////////////////////////////
                                                    LocalNotification("Souscription", f);
                                                    dbHandler.insertUserDetails("Souscription",f, "0", R.drawable.ic_notifications_black_48dp, shortDateFormat.format(aujourdhui));


                                                    ////////////////////INSERTION DES DONNEES UTILISATEURS DANS LA BD LOCALE/////////////////////////
                                                    dbUser.insertInfoUser(nom.getText().toString().trim(), prenom.getText().toString().trim(),sexe.getSelectedItem().toString().trim(),
                                                            telephone.getText().toString().trim(), cni.getText().toString().trim(), statut.getSelectedItem().toString().trim(),
                                                            adresse.getText().toString().trim(), numCarte.getText().toString().trim(), typeChauffeur.getSelectedItem().toString().trim(),
                                                            "default", "offline" , abonnement, shortDateFormat.format(aujourdhui));


                                                    String num_carte = numCarte.getText().toString().trim();

                                                    View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                                                    TextView title = (TextView) view.findViewById(R.id.title);
                                                    TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                                                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                                                    title.setText(getString(R.string.information));
                                                    imageButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                                                    statutOperation.setText(f);
                                                    build_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            Intent intent = new Intent(Souscription.this, QRCodeShow.class);
                                                            intent.putExtra("id_carte", "E-ZPASS" +num_carte + getsecurity_keys());
                                                            intent.putExtra("nom_prenom", nom_prenom);
                                                            startActivity(intent);

                                                        }
                                                    });
                                                    build_error.setCancelable(false);
                                                    build_error.setView(view);
                                                    build_error.show();

                                                    nom.setText("");
                                                    prenom.setText("");
                                                    telephone.setText("");
                                                    cni.setText("");
                                                    adresse.setText("");
                                                    numCarte.setText("");

                                                } else{

                                                    ////////////////////INITIALISATION DE LA BASE DE DONNEES LOCALE/////////////////////////
                                                    dbHandler = new DbHandler(getApplicationContext());
                                                    aujourdhui = new Date();
                                                    shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

                                                    //////////////////////////////////NOTIFICATIONS////////////////////////////////
                                                    LocalNotification("Souscription", f);
                                                    dbHandler.insertUserDetails("Souscription","carte non enregistré", "0", R.drawable.ic_notifications_red_48dp, shortDateFormat.format(aujourdhui));

                                                    build_error = new AlertDialog.Builder(Souscription.this);
                                                    View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                                                    TextView title = (TextView) view.findViewById(R.id.title);
                                                    TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                                                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                                                    title.setText(getString(R.string.information));
                                                    imageButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                                                    statutOperation.setText(f);
                                                    build_error.setPositiveButton("OK", null);
                                                    build_error.setCancelable(false);
                                                    build_error.setView(view);
                                                    build_error.show();

                                                    Toast.makeText(getApplicationContext(), f, Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });


                                        //    JSONObject jsonObject = new JSONObject(data);
                                        //  jsonObject.getString("status");
                                        JSONArray jsonArray = new JSONArray(data);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Toast.makeText(Souscription.this, jsonObject.getString("nom"), Toast.LENGTH_SHORT).show();
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

                                                // Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                //Check si la connexion existe
                                                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                                NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
                                                if(!(activeInfo != null && activeInfo.isConnected())){
                                                    progressDialog.dismiss();
                                                    authWindows.setVisibility(View.GONE);
                                                    internetIndisponible.setVisibility(View.VISIBLE);
                                                    Toast.makeText(Souscription.this, getString(R.string.pasDeConnexionInternet), Toast.LENGTH_SHORT).show();
                                                } else{
                                                    progressDialog.dismiss();
                                                    authWindows.setVisibility(View.GONE);
                                                    internetIndisponible.setVisibility(View.VISIBLE);
                                                    conStatusIv.setImageResource(R.drawable.ic_action_limited_network);
                                                    titleNetworkLimited.setText(getString(R.string.connexionLimite));
                                                    //msgNetworkLimited.setText();
                                                    Toast.makeText(Souscription.this, getString(R.string.connexionLimite), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                        e.printStackTrace();
                                        try {
                                            Thread.sleep(2000);
                                            //Toast.makeText(Inscription.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();


                            //----------------FIN-------------------------------------

                        }
                        else{
                            Toast.makeText(Souscription.this, "Votre Téléphone contient moin de 9 chiffres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Un ou plusieurs champs sont Invalid", Toast.LENGTH_SHORT).show();

                        View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                        TextView title = (TextView) view.findViewById(R.id.title);
                        TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                        ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                        title.setText(getString(R.string.information));
                        imageButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                        statutOperation.setText(getString(R.string.champsInvlid));
                        build_error.setPositiveButton("OK", null);
                        build_error.setCancelable(false);
                        build_error.setView(view);
                        build_error.show();
                    }
                }
            }

        });



        //PASSAGE DE LA CARTE
        btnOpenNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nfc.open();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // On ajoute un message à notre progress dialog
                            progressDialog.setMessage(getString(R.string.passerCarte));
                            // On donne un titre à notre progress dialog
                            progressDialog.setTitle(getString(R.string.attenteCarte));
                            // On spécifie le style
                            //  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            // On affiche notre message
                            progressDialog.show();
                            //build.setPositiveButton("ok", new View.OnClickListener()

                        }
                    });
                } catch (TelpoException e) {
                    e.printStackTrace();
                }
                readThread = new ReadThread();
                readThread.start();
            }
        });


        //DETECTION DE TYPE DE CARTE ET SON ID
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CHECK_NFC_TIMEOUT: {
                        Toast.makeText(getApplicationContext(), "Check card time out!", Toast.LENGTH_LONG).show();
                       /* open_btn.setEnabled(true);
                        close_btn.setEnabled(false);
                        check_btn.setEnabled(false);*/
                    }
                    break;
                    case SHOW_NFC_DATA: {
                        byte[] uid_data = (byte[]) msg.obj;
                        if (uid_data[0] == 0x42) {
                            // TYPE B类（暂时只支持cpu卡）
                            byte[] atqb = new byte[uid_data[16]];
                            byte[] pupi = new byte[4];
                            String type = null;

                            System.arraycopy(uid_data, 17, atqb, 0, uid_data[16]);
                            System.arraycopy(uid_data, 29, pupi, 0, 4);

                            if (uid_data[1] == B_CPU) {
                                type = "CPU";
                               /* sendApduBtn.setEnabled(true);
                                getAtsBtn.setEnabled(true);*/
                            } else {
                                type = "unknow";
                            }

                            new AlertDialog.Builder(Souscription.this)
                                    .setMessage(getString(R.string.card_type) + getString(R.string.type_b) + " " + type +
                                            "\r\n" + getString(R.string.atqb_data) + StringUtil.toHexString(atqb) +
                                            "\r\n" + getString(R.string.pupi_data) + StringUtil.toHexString(pupi))
                                    .setPositiveButton("OK", null)
                                    .setCancelable(false)
                                    .show();

                           /* uid_editText.setText(getString(R.string.card_type) + getString(R.string.type_b) + " " + type +
                                    "\r\n" + getString(R.string.atqb_data) + StringUtil.toHexString(atqb) +
                                    "\r\n" + getString(R.string.pupi_data) + StringUtil.toHexString(pupi));*/

                        } else if (uid_data[0] == 0x41) {
                            // TYPE A类（CPU, M1）
                            byte[] atqa = new byte[2];
                            byte[] sak = new byte[1];
                            byte[] uid = new byte[uid_data[5]];
                            String type = null;

                            System.arraycopy(uid_data, 2, atqa, 0, 2);
                            System.arraycopy(uid_data, 4, sak, 0, 1);
                            System.arraycopy(uid_data, 6, uid, 0, uid_data[5]);

                            if (uid_data[1] == A_CPU) {
                                type = "CPU";
                                /*sendApduBtn.setEnabled(true);
                                getAtsBtn.setEnabled(true);*/
                            } else if (uid_data[1] == A_M1) {
                                type = "M1";
                                // authenticateBtn.setEnabled(true);
                            } else {
                                type = "unknow";
                            }
                           /* new AlertDialog.Builder(Login.this)
                                    .setMessage(getString(R.string.card_type) + getString(R.string.type_a) + " " + type +
                                            "\r\n" + getString(R.string.atqa_data) + StringUtil.toHexString(atqa) +
                                            "\r\n" + getString(R.string.sak_data) + StringUtil.toHexString(sak) +
                                            "\r\n" + getString(R.string.uid_data) + StringUtil.toHexString(uid))
                                    .setPositiveButton("OK", null)
                                    .setCancelable(false)
                                    .show();*/
                            //numCarte.setText(StringUtil.toHexString(uid));
                            m1CardAuthenticate();
                            progressDialog.dismiss();
                            try {
                                nfc.close();
                            } catch (TelpoException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e(TAG, "unknow type card!!");
                        }
                    }
                    break;

                    default:
                        break;
                }
            }
        };


        btnReessayer.setOnClickListener(this::checkNetworkConnectionStatus);

    }

    public void checkNetworkConnectionStatus(View view) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

        if(activeInfo != null && activeInfo.isConnected()){

            ProgressDialog dialog = ProgressDialog.show(this, "Connexion", "Encours...", true);
            dialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    //this.recreate();
                    finish();
                    startActivity(getIntent());
                }
            }, 3000); // 3000 milliseconds delay

        } else{
            progressDialog.dismiss();
            authWindows.setVisibility(View.GONE);
            internetIndisponible.setVisibility(View.VISIBLE);
            Toast.makeText(Souscription.this, getString(R.string.connexionIntrouvable), Toast.LENGTH_SHORT).show();
        }
    }



    public class ReadThread extends Thread {
        byte[] nfcData = null;

        @Override
        public void run() {
            try {

                time1 = System.currentTimeMillis();
                nfcData = nfc.activate(10 * 1000); // 10s
                time2 = System.currentTimeMillis();
                Log.e("yw activate", (time2 - time1) + "");
                if (null != nfcData) {
                    handler.sendMessage(handler.obtainMessage(SHOW_NFC_DATA, nfcData));
                } else {
                    Log.d(TAG, "Check Card timeout...");
                    handler.sendMessage(handler.obtainMessage(CHECK_NFC_TIMEOUT, null));
                }
            } catch (TelpoException e) {
                Log.e("yw", e.toString());
                e.printStackTrace();
            }
        }
    }



    public static boolean isValid(String str)
    {
        boolean isValid = false;
        String expression = "^[a-z_A-Z0-9éèê'çà ]*$";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }


    public void addItemsOnSpinner3() {

        //statique
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        /*List<String> list = new ArrayList<String>();
        list.add("étudiant");
        list.add("élève");
        list.add("particulier");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(dataAdapter);*/

        //dynamique
        listUser.clear();
        idUser.clear();
        typeUser.clear();
        for(int i=0; i<NOMCath.length; i++){

            if(i>=6 && i<(NOMCath.length-1)){
                listUser.add(NOMCath[i]);
                idUser.add(IDCathegorie[i]);
                typeUser.add(typeuser[i]);

                //Toast.makeText(this, IDCathegorie[i], Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, listUser);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(dataAdapter);

    }


    public void addItemsOnSpinner2() {

        //statique
        /*List<String> list = new ArrayList<String>();
        list.add("moto_taxis");
        list.add("Chauffeur");
        list.add("mini-bus");
        list.add("bus inter urbain");
        list.add("restaurant étudiant");
        list.add("monetbil");
        list.add("moreals");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(dataAdapter);*/


        //LoadDBCategorieInpinner();
        //type de chauffeur

        //ejection de quelques éléments de le liste



        //dynamique
        listChauffeur.clear();
        idChauffeur.clear();
        typeUserChauffeur.clear();
        for(int i=0; i<NOMCath.length; i++){
            if(i<6){
                listChauffeur.add(NOMCath[i]);
                idChauffeur.add(IDCathegorie[i]);
                typeUserChauffeur.add(typeuser[i]);
                //Toast.makeText(this, IDCathegorie[i], Toast.LENGTH_SHORT).show();
            }
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, listChauffeur);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(spinnerArrayAdapter);
    }

    public void addItemsOnSpinner1() {

        //statique
        /*List<String> list = new ArrayList<String>();
        list.add("smopaye");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(dataAdapter);*/

        //dynamique
        autreUser.clear();
        idAutreUser.clear();
        typeAutreUser.clear();
        autreUser.add(NOMCath[NOMCath.length-1]);
        idAutreUser.add(IDCathegorie[NOMCath.length-1]);
        typeAutreUser.add(typeuser[NOMCath.length-1]);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, autreUser);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeChauffeur.setAdapter(dataAdapter);


    }




    public void m1CardAuthenticate() {
        Boolean status = true;
        byte[] passwd = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        try {

            time1 = System.currentTimeMillis();
            nfc.m1_authenticate(blockNum_1, (byte) 0x0B, passwd);//0x0B
            time2 = System.currentTimeMillis();
            Log.e("yw m1_authenticate", (time2 - time1) + "");


        } catch (TelpoException e) {
            status = false;
            e.printStackTrace();
            Log.e("yw", e.toString());
        }

        if (status) {
            Log.d(TAG, "m1CardAuthenticate success!");
            //writeBlockData();
            //readBlockData();

            //OwriteValueData();
            readValueDataCourt();
        } else {
            Toast.makeText(this, getString(R.string.operation_fail), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "m1CardAuthenticate fail!");
        }
    }


    public void readValueDataCourt() {
        byte[] data = null;
        try {
            data = nfc.m1_read_value(blockNum_2);
        } catch (TelpoException e) {
            e.printStackTrace();
        }

        if (null == data) {
            Log.e(TAG, "readValueBtn fail!");
            Toast.makeText(this, getString(R.string.operation_fail), Toast.LENGTH_SHORT).show();
        } else {
            numCarte.setText(StringUtil.toHexString(data));
        }
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        if (id == R.id.tuto) {
            Intent intent = new Intent(getApplicationContext(), TutorielUtilise.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        if(id == R.id.modifierCompte){
            Intent intent = new Intent(getApplicationContext(), ModifierCompte.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    /*private void registerGoogleFirebase(final String nom1, final String prenom1, final String sexe1,
                                        final String tel1, final String typePJ1, final String cni1, final String session1,
                                        final String adresse1, final String id_carte1, final String typeUser1,
                                        String email1, String password1, final String imageURL1, final String status1, final String f1)
    {

         nom_prenom = nom1 + " " + prenom1;

        auth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("nom", nom1);
                            hashMap.put("prenom", prenom1);
                            hashMap.put("sexe", sexe1);
                            hashMap.put("tel", tel1);
                            hashMap.put("cni", typePJ1 +"-"+ cni1);
                            hashMap.put("session", session1);
                            hashMap.put("adresse", adresse1);
                            hashMap.put("id_carte", id_carte1);
                            hashMap.put("typeUser", typeUser1);
                            hashMap.put("imageURL", imageURL1);
                            hashMap.put("status", status1);
                            hashMap.put("search", nom1.toLowerCase());
                            hashMap.put("abonnement", abonnement);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Souscription.this, "Opération Réussie", Toast.LENGTH_SHORT).show();

                                    /////////////////////SERVICE GOOGLE FIREBASE CLOUD MESSAGING///////////////////////////

                                    //SERVICE GOOGLE FIREBASE

                                    Query query = FirebaseDatabase.getInstance().getReference("Users")
                                            .orderByChild("id_carte")
                                            .equalTo(id_carte1);

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){
                                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                    User user = userSnapshot.getValue(User.class);
                                                    if (user.getId_carte().equals(id_carte1)) {
                                                        RemoteNotification(user.getId(), user.getPrenom(), "Souscription", f1, "success");
                                                        //Toast.makeText(RetraitAccepteur.this, "CARTE TROUVE", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Souscription.this, "Ce numéro de carte n'existe pas", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            else{
                                                Toast.makeText(Souscription.this, "Impossible d'envoyer votre Notification", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                    ////////////////////INITIALISATION DE LA BASE DE DONNEES LOCALE/////////////////////////
                                    dbHandler = new DbHandler(getApplicationContext());
                                    dbUser = new DbUser(getApplicationContext());
                                    aujourdhui = new Date();
                                    shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

                                    //////////////////////////////////NOTIFICATIONS////////////////////////////////
                                    LocalNotification("Souscription", f1);
                                    dbHandler.insertUserDetails("Souscription",f1, "0", R.drawable.ic_notifications_black_48dp, shortDateFormat.format(aujourdhui));


                                    ////////////////////INSERTION DES DONNEES UTILISATEURS DANS LA BD LOCALE/////////////////////////
                                    dbUser.insertInfoUser(nom.getText().toString().trim(), prenom.getText().toString().trim(),sexe.getSelectedItem().toString().trim(),
                                            telephone.getText().toString().trim(), cni.getText().toString().trim(), statut.getSelectedItem().toString().trim(),
                                            adresse.getText().toString().trim(), numCarte.getText().toString().trim(), typeChauffeur.getSelectedItem().toString().trim(),
                                            "default", "offline" , abonnement, shortDateFormat.format(aujourdhui));


                                    String num_carte = numCarte.getText().toString().trim();

                                    View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                                    TextView title = (TextView) view.findViewById(R.id.title);
                                    TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                                    title.setText(getString(R.string.information));
                                    imageButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                                    statutOperation.setText(f1);
                                    build_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(Souscription.this, QRCodeShow.class);
                                            intent.putExtra("id_carte", "E-ZPASS" +num_carte + getsecurity_keys());
                                            intent.putExtra("nom_prenom", nom_prenom);
                                            startActivity(intent);

                                        }
                                    });
                                    build_error.setCancelable(false);
                                    build_error.setView(view);
                                    build_error.show();

                                    nom.setText("");
                                    prenom.setText("");
                                    telephone.setText("");
                                    cni.setText("");
                                    adresse.setText("");
                                    numCarte.setText("");

                                }
                            });
                        }
                        else{

                            ////////////////////INITIALISATION DE LA BASE DE DONNEES LOCALE/////////////////////////
                            dbHandler = new DbHandler(getApplicationContext());
                            aujourdhui = new Date();
                            shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

                            //////////////////////////////////NOTIFICATIONS////////////////////////////////
                            LocalNotification("Souscription", getString(R.string.impossibleRegister));
                            dbHandler.insertUserDetails("Souscription",getString(R.string.impossibleRegister), "0", R.drawable.ic_notifications_red_48dp, shortDateFormat.format(aujourdhui));

                            Toast.makeText(Souscription.this, getString(R.string.impossibleRegister), Toast.LENGTH_SHORT).show();
                            View view = LayoutInflater.from(Souscription.this).inflate(R.layout.alert_dialog_success, null);
                            TextView title = (TextView) view.findViewById(R.id.title);
                            TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                            ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                            title.setText(getString(R.string.information));
                            imageButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                            statutOperation.setText(getString(R.string.impossibleRegister));
                            build_error.setPositiveButton("OK", null);
                            build_error.setCancelable(false);
                            build_error.setView(view);
                            build_error.show();
                        }
                    }
                });
    }*/

    /*private void RemoteNotification(final String receiver, final String username, final String title, final String message, final String statut_notif){

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(fuser.getUid(), R.drawable.logo2, username + ": " + message, title, receiver, statut_notif);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(Souscription.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    public void LocalNotification(String titles, String subtitles){

        ///////////////DEBUT NOTIFICATIONS///////////////////////////////
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notif_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notif_expanded);

        Intent clickIntent = new Intent(getApplicationContext(), NotifReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, clickIntent, 0);

        collapsedView.setTextViewText(R.id.text_view_collapsed_1, titles);
        collapsedView.setTextViewText(R.id.text_view_collapsed_2, subtitles);

        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.logo2);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo2)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManager.notify(new Random().nextInt(), notification);
        ////////////////////////////////////FIN NOTIFICATIONS/////////////////////
    }


    //gestion des abonnements
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.AbonnementMensuel:
                if (checked)
                {
                    Toast.makeText(this, AbonnementMensuel.getText().toString(), Toast.LENGTH_SHORT).show();
                    AbonnementHebdomadaire.setChecked(false);
                    AbonnementService.setChecked(false);
                    //AbonnementMensuel.setBackgroundColor(Color.parseColor("#039BE5"));
                    abonnement = "mensuel";
                }
                else{
                    AbonnementMensuel.setChecked(true);
                    AbonnementHebdomadaire.setChecked(false);
                    AbonnementService.setChecked(false);
                    abonnement = "mensuel";
                }
                break;
            case R.id.AbonnementHebdomadaire:
                if (checked)
                {
                    Toast.makeText(this, AbonnementHebdomadaire.getText().toString(), Toast.LENGTH_SHORT).show();
                    AbonnementMensuel.setChecked(false);
                    AbonnementService.setChecked(false);
                    abonnement = "hebdomadaire";
                }
                else{
                    AbonnementHebdomadaire.setChecked(true);
                    AbonnementMensuel.setChecked(false);
                    AbonnementService.setChecked(false);
                    abonnement = "hebdomadaire";
                }
                break;

            case R.id.AbonnementService:
                if (checked)
                {
                    Toast.makeText(this, AbonnementService.getText().toString(), Toast.LENGTH_SHORT).show();
                    AbonnementMensuel.setChecked(false);
                    AbonnementHebdomadaire.setChecked(false);
                    abonnement = "service";
                }
                else{
                    AbonnementService.setChecked(true);
                    AbonnementMensuel.setChecked(false);
                    AbonnementHebdomadaire.setChecked(false);
                    abonnement = "service";
                }
                break;
        }
    }






    public class loadDataSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LoadDBStatutInSpinner();
            LoadDBCategorieInpinner();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



    private void LoadDBStatutInSpinner(){
//connection
        try{
            final Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("auth","Users");
            builder.appendQueryParameter("login", "register");
            builder.appendQueryParameter("infoname", "status");
            builder.appendQueryParameter("uniquser", temp_number);
            builder.appendQueryParameter("fgfggergJHGS", ChaineConnexion.getEncrypted_password());
            builder.appendQueryParameter("uhtdgG18",ChaineConnexion.getSalt());

            URL url = new URL(ChaineConnexion.getAdresseURLsmopayeServer() + builder.build().toString());

            //URL url = new URL(adressUrl);
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

            listStatut.clear();
            idStatut.clear();

            id_session = new String[ja.length()];
            nom_session = new String[ja.length()];

            for(int i=0; i<=ja.length();i++){
                jo = ja.getJSONObject(i);
                id_session[i] = jo.getString("id_session");
                nom_session[i] = jo.getString("nom_session");
                listStatut.add(nom_session[i]);
                idStatut.add(id_session[i]);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void LoadDBCategorieInpinner(){
//connection

        try{
            final Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("auth","Users");
            builder.appendQueryParameter("login", "register");
            builder.appendQueryParameter("infoname", "cath");
            builder.appendQueryParameter("uniquser", temp_number);
            builder.appendQueryParameter("fgfggergJHGS", ChaineConnexion.getEncrypted_password());
            builder.appendQueryParameter("uhtdgG18",ChaineConnexion.getSalt());

            URL url = new URL(ChaineConnexion.getAdresseURLsmopayeServer() + builder.build().toString());

            //URL url = new URL(adressUrl);
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

            allListID.clear();

            IDCathegorie = new String[ja.length()];
            NOMCath = new String[ja.length()];
            typeuser = new String[ja.length()];
            allID = new String[ja.length()];

            for(int i=0; i<=ja.length();i++){
                jo = ja.getJSONObject(i);

                IDCathegorie[i] = jo.getString("IDCathegorie");
                NOMCath[i] = jo.getString("NOMCath");
                typeuser[i] = jo.getString("typeuser");

                allID[i] = jo.getString("IDCathegorie");
                allListID.add(allID[i]);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
