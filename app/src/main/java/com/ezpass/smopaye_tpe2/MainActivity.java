package com.ezpass.smopaye_tpe2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.ezpass.smopaye_tpe2.DBLocale_Notifications.DbHandler;
import com.ezpass.smopaye_tpe2.drawerNavigation.Accueil_OffreSmopaye;
import com.ezpass.smopaye_tpe2.drawerNavigation.WebSite;
import com.ezpass.smopaye_tpe2.vuesAdmin.AccueilFragmentAdmin;
import com.ezpass.smopaye_tpe2.vuesAgent.AccueilFragmentAgent;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.AccueilFragmentUser;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.ModifierCompte;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String resultat_bd, telephone;
    private AlertDialog.Builder build, build_error;
    private TextView statut, statut_nom;

    BottomBar bottomBar;
    DbHandler db;
    BottomBarTab nearby;

    FirebaseUser fuser;
    //DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());


        /*----------------------------------------------COMMUNICATION AVEC LOGIN ET BD ------------*/

        Intent intent = getIntent();
        resultat_bd = intent.getStringExtra("resultatBD");
        telephone = intent.getStringExtra("telephone");

        String[] parts = resultat_bd.split("-");
        String nom = parts[0]; //Nom
        String prenom = parts[1]; //Prenom
        String session = parts[2]; // Accepteur, Administrateur, Utilisateur, Agent
        String etat = parts[3]; // Actif, Inactif, Offline
        //String categorie = parts[4]; // chauffeur, moto_taxi, bus, cargo
        String nomComplet = prenom + " " + nom;

        /*---------------------------------------FIN--------------------------------*/





/**************************************************DEBUT**************************/

        //Appel de l' élément de navigations Par défaut au chargement (Accueil)
       /* BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);*/

/*****************************************FIN**************************************/

        Fragment selectedFragment1 = null;
        Bundle bundle1 = new Bundle();


        if(session.toLowerCase().equalsIgnoreCase("administrateur")){
            /*--------------------------------AJOUT DES ELEMENTS DANS LE HEADER DU  nav_header_main.xml---------------*/

        /*View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        statut = (TextView) header.findViewById(R.id.txt_statut);
        statut_nom = (TextView) header.findViewById(R.id.txt_statut_nom);
        statut_nom.setText("HELLO");
        statut.setText("HELLO");*/

            View header = navigationView.getHeaderView(0);
            statut = (TextView) header.findViewById(R.id.txt_statut);
            statut_nom = (TextView) header.findViewById(R.id.txt_statut_nom);
            statut_nom.setText(nomComplet);
            statut.setText(session); // Accepteur, Administrateur, Utilisateur, Agent
            /*--------------------------------FIN---------------*/

            if(etat.toLowerCase().equalsIgnoreCase("actif"))
                selectedFragment1 = new AccueilFragmentAdmin();
            else {
                selectedFragment1 = new AccueilFragmentUser();
                statut.setText(getString(R.string.utilisateur));
            }
        }
        else if(session.toLowerCase().equalsIgnoreCase("utilisateur")){
            /*--------------------------------AJOUT DES ELEMENTS DANS LE HEADER DU  nav_header_main.xml---------------*/
            View header = navigationView.getHeaderView(0);
            statut = (TextView) header.findViewById(R.id.txt_statut);
            statut_nom = (TextView) header.findViewById(R.id.txt_statut_nom);
            statut_nom.setText(nomComplet);
            statut.setText(session); // Accepteur, Administrateur, Utilisateur, Agent
            /*--------------------------------FIN---------------*/
            selectedFragment1 = new AccueilFragmentUser();
        }
        else if(session.toLowerCase().equalsIgnoreCase("agent"))
        {
            /*--------------------------------AJOUT DES ELEMENTS DANS LE HEADER DU  nav_header_main.xml---------------*/
            View header = navigationView.getHeaderView(0);
            statut = (TextView) header.findViewById(R.id.txt_statut);
            statut_nom = (TextView) header.findViewById(R.id.txt_statut_nom);
            statut_nom.setText(nomComplet);
            statut.setText(session); // Accepteur, Administrateur, Utilisateur, Agent
            /*--------------------------------FIN---------------*/
            if(etat.toLowerCase().equalsIgnoreCase("actif"))
            {
                bundle1.putString("result_BD", resultat_bd);
                selectedFragment1 = new AccueilFragmentAgent();
                selectedFragment1.setArguments(bundle1);
            }
            else {
                selectedFragment1 = new AccueilFragmentUser();
                statut.setText(getString(R.string.utilisateur));
            }
        }
        else if(session.toLowerCase().equalsIgnoreCase("accepteur"))  //il s 'agit d'un accepteur
        {
            /*--------------------------------AJOUT DES ELEMENTS DANS LE HEADER DU  nav_header_main.xml---------------*/
            View header = navigationView.getHeaderView(0);
            statut = (TextView) header.findViewById(R.id.txt_statut);
            statut_nom = (TextView) header.findViewById(R.id.txt_statut_nom);
            statut_nom.setText(nomComplet);
            statut.setText(session); // Accepteur, Administrateur, Utilisateur, Agent
            /*--------------------------------FIN---------------*/

            if(etat.toLowerCase().equalsIgnoreCase("actif")) {
                //bundle1.putString("telephone", telephone);
                //bundle1.putString("result_BD", resultat_bd);
                selectedFragment1 = new AccueilFragment();
                selectedFragment1.setArguments(bundle1);
            }
            else {
                selectedFragment1 = new AccueilFragmentUser();
                statut.setText(getString(R.string.utilisateur));
            }
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment1).commit();

        getSupportActionBar().setTitle("Accueil");









/**************************************************DEBUT**************************/


        //Instanciation de la base de données locale
        db = new DbHandler(getApplicationContext());
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        nearby = bottomBar.getTabWithId(R.id.nav_notifications);
        nearby.setBadgeCount(Integer.parseInt(db.GetNumNotifications()));

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {

                Fragment selectedFragment = null;
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                resultat_bd = intent.getStringExtra("resultatBD");
                String[] parts = resultat_bd.split("-");
                String session1 = parts[2]; // Accepteur, Administrateur, Utilisateur, Agent
                String etat1 = parts[3]; // Actif ou Inactif
                //String categorie = parts[4]; // chauffeur, moto_taxi, bus, cargo


                switch (tabId){
                    case R.id.nav_Accueil:
                        getSupportActionBar().setTitle("Accueil");
                        //selectedFragment = new AccueilFragment();
                        if(session1.toLowerCase().equalsIgnoreCase("administrateur"))
                        {
                            if(etat1.toLowerCase().equalsIgnoreCase("actif"))
                                selectedFragment = new AccueilFragmentAdmin();
                            else
                                selectedFragment = new AccueilFragmentUser();
                        }
                        else if(session1.toLowerCase().equalsIgnoreCase("utilisateur"))
                            selectedFragment = new AccueilFragmentUser();
                        else if(session1.toLowerCase().equalsIgnoreCase("agent"))
                        {
                            bundle.putString("result_BD", resultat_bd);
                            selectedFragment = new AccueilFragmentAgent();
                            selectedFragment.setArguments(bundle);
                        }
                        else //accepteur
                        {
                            if(etat1.toLowerCase().equalsIgnoreCase("actif")) {
                                bundle.putString("result_BD", resultat_bd);
                                selectedFragment = new AccueilFragment();
                                selectedFragment.setArguments(bundle);
                            }
                            else
                                selectedFragment = new AccueilFragmentUser();
                        }
                        break;

                    case R.id.nav_maCarte:
                        getSupportActionBar().setTitle("Ma Carte Smopaye");
                        bundle.putString("result_BD", resultat_bd);
                        bundle.putString("telephone", telephone);
                        selectedFragment = new CarteFragment();
                        selectedFragment.setArguments(bundle);
                        break;
                    case R.id.nav_pointSmopaye:
                        getSupportActionBar().setTitle("Point de Vente Smopaye");
                        selectedFragment = new PointSmopayeFragment();
                        break;
                    case R.id.nav_notifications:
                        getSupportActionBar().setTitle("Notifications");

                        nearby.setBadgeCount(Integer.parseInt(db.GetNumNotifications()));

                        DbHandler db1 = new DbHandler(getApplicationContext());
                        db1.UpdateNumNotification("1");

                        selectedFragment = new NotificationsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                //return true;
            }
        });

    }


    @Override
    protected void onPostResume() {
        nearby.setBadgeCount(Integer.parseInt(db.GetNumNotifications()));
        //status("online");
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
            intent.putExtra("resultatBD", resultat_bd);
            intent.putExtra("telephone", telephone);
            startActivity(intent);
        }

        if (id == R.id.tuto) {
            Intent intent = new Intent(getApplicationContext(), TutorielUtilise.class);
            startActivity(intent);
        }

        if(id == R.id.modifierCompte){
            //Toast.makeText(this, telephone, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ModifierCompte.class);
            intent.putExtra("telephone", telephone);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_offreSmopaye) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(), Accueil_OffreSmopaye.class));
        } else if (id == R.id.nav_siteWeb) {
            // Toast.makeText(this, "Site Web", Toast.LENGTH_SHORT).show();
            //Methode permettant l'ouverture d'une page web via un navigateur
            /*String url="http://www.google.fr";
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);*/

            Intent intent = new Intent(getApplicationContext(), WebSite.class);
            startActivity(intent);

            //startActivity(new Intent(getApplicationContext(), WebSite.class));

        }else if (id == R.id.nav_transfert){
            Intent intent = new Intent(getApplicationContext(), Transfert.class);
            intent.putExtra("telephone", telephone);
            startActivity(intent);
        } else if (id == R.id.nav_abonnement){
            Intent intent = new Intent(getApplicationContext(), PayerAbonnement.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_deconnexion) {

            ProgressDialog dialog = ProgressDialog.show(this, "Déconnexion", "Encours...", true);
            dialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    finish();
                }
            }, 3000); // 3000 milliseconds delay

        }

        else if (id == R.id.nav_QuestionFrequentes) {
            Toast.makeText(this, "Questions Fréquentes", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_parametres) {
            Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /* MENU Navigation Bottom */
    /*private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            Bundle bundle = new Bundle();


            Intent intent = getIntent();
            resultat_bd = intent.getStringExtra("resultatBD");

            String[] parts = resultat_bd.split("-");
            String session1 = parts[2]; // Accepteur, Administrateur, Utilisateur, Agent
            String etat1 = parts[3]; // Actif ou Inactif
            //String categorie = parts[4]; // chauffeur, moto_taxi, bus, cargo



              //bundle.putInt("Integer", Integer value);
             // bundle.putDouble("Double", Double value);
              //bundle.putBoolean("Boolean", Boolean value);

              //getArguments().getString("String");//String text
              //getArguments().getInt("Integer");//Integer value
             // getArguments().getDouble("Double");//Double value
              //getArguments().getBoolean("Boolean");//Boolean value


            switch (menuItem.getItemId()){
                case R.id.nav_Accueil:
                    getSupportActionBar().setTitle("Accueil");
                    //selectedFragment = new AccueilFragment();
                    if(session1.equalsIgnoreCase("Administrateur"))
                    {
                        if(etat1.equalsIgnoreCase("Actif"))
                            selectedFragment = new AccueilFragmentAdmin();
                        else
                            selectedFragment = new AccueilFragmentUser();
                    }
                    else if(session1.equalsIgnoreCase("Utilisateur"))
                        selectedFragment = new AccueilFragmentUser();
                    else if(session1.equalsIgnoreCase("Agent"))
                        selectedFragment = new AccueilFragmentAgent();
                    else //accepteur
                    {
                        if(etat1.equalsIgnoreCase("Actif")) {
                            bundle.putString("result_BD", resultat_bd);
                            selectedFragment = new AccueilFragment();
                            selectedFragment.setArguments(bundle);
                        }
                        else
                            selectedFragment = new AccueilFragmentUser();
                    }
                    break;

                case R.id.nav_maCarte:
                    getSupportActionBar().setTitle("Ma Carte Smopaye");
                    bundle.putString("result_BD", resultat_bd);
                    bundle.putString("telephone", telephone);
                    selectedFragment = new CarteFragment();
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.nav_pointSmopaye:
                    getSupportActionBar().setTitle("Point de Vente Smopaye");
                    selectedFragment = new PointSmopayeFragment();
                    break;
                case R.id.nav_notifications:
                    getSupportActionBar().setTitle("Notifications");
                    selectedFragment = new NotificationsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };*/

    /* ***END *** */

    /*private void status(String status){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }



    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }*/


  /* private void updateToken(String token){
       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Tokens");
       Token token1 = new Token(token);
       reference1.child(fuser.getUid()).setValue(token1);
   }*/


}
