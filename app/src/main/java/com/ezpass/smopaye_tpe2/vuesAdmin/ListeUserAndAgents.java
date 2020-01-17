package com.ezpass.smopaye_tpe2.vuesAdmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ezpass.smopaye_tpe2.Apropos.Apropos;
import com.ezpass.smopaye_tpe2.ChaineConnexion;
import com.ezpass.smopaye_tpe2.R;
import com.ezpass.smopaye_tpe2.TutorielUtilise;
import com.ezpass.smopaye_tpe2.vuesUtilisateur.ModifierCompte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListeUserAndAgents extends AppCompatActivity {

    ListView listView;
    String [] data1, data2;
    ArrayAdapter<String> adapter;
    ProgressBar progressBar;
    List listUser = new ArrayList();
    EditText editText ;
    /////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_user_and_agents);


        //getSupportActionBar().setTitle("Utilisateurs & Agents");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listViewContent);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        editText = (EditText)findViewById(R.id.edittext1);


        // Calling Method to Parese JSON data into listView.
        new GetHttpResponse(ListeUserAndAgents.this).execute();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Updating Array Adapter ListView after typing inside EditText.
                ListeUserAndAgents.this.adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    ListeUserAndAgents.this.adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Creating GetHttpResponse message to parse JSON.
    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        // Creating context.
        public Context context;

        // Creating string to hold Http response result.
        String ResultHolder;

        // Creating constructor .
        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            getData();
            return null;
        }

        // This block will execute after done all background processing.
        @Override
        protected void onPostExecute(Void result)

        {
            // Hiding the progress bar after done loading JSON.
            //progressBar.setVisibility(View.GONE);

            // Showing the ListView after done loading JSON.
            //listView.setVisibility(View.VISIBLE);

            // Setting up the SubjectArrayList into Array Adapter.
            // arrayAdapter = new ArrayAdapter(ViewListContents.this,android.R.layout.simple_list_item_1, android.R.id.text1, SubjectArrayList);

            // Passing the Array Adapter into ListView.
            //listView.setAdapter(arrayAdapter);


            //ADAPTER
            //Toast.makeText(ViewListContents.this, listUser.toString(), Toast.LENGTH_SHORT).show();
            // adapter = new ArrayAdapter<String>(ViewListContents.this, android.R.layout.simple_list_item_1, android.R.id.text1, listUser);
            //listView.setAdapter(adapter);

        }
    }


    private void getData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Uri.Builder builder = new Uri.Builder();
                    builder.appendQueryParameter("auth","Users");
                    builder.appendQueryParameter("login", "listingAgent");
                    builder.appendQueryParameter("fgfggergJHGS", ChaineConnexion.getEncrypted_password());
                    builder.appendQueryParameter("uhtdgG18", ChaineConnexion.getSalt());




                    //Connexion au serveur
                    //URL url = new URL("http://192.168.20.11:1234/listing.php"+builder.build().toString());
                    URL url = new URL(ChaineConnexion.getAdresseURLsmopayeServer() + builder.build().toString());
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
                            Toast.makeText(getApplicationContext(), "Encours de traitement...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    while (bufferedReader.ready() || data==""){
                        data+=bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    inputStream.close();


                    final String f = data.trim();
                    final String[] parts = f.split("-");
                    // boolean d=data;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Toast.makeText(getApplicationContext(), f, Toast.LENGTH_LONG).show();

                            //PARSE JSON DATA
                            try{
                                JSONArray ja = new JSONArray(parts[3]);
                                JSONObject jo = null;
                                List listUser2 = new ArrayList();

                                data1 = new String[ja.length()];
                                data2 = new String[ja.length()];

                                for(int i=0; i<ja.length(); i++){
                                    jo = ja.getJSONObject(i);
                                    data1[i] = jo.getString("NOM");
                                    data2[i] = jo.getString("PRENOM");
                                    //listUser.add(data1[i]);
                                    //Toast.makeText(ViewListContents.this, listUser.toString(), Toast.LENGTH_SHORT).show();
                                    //listUser2.add(data1[i] +  "-" +  data2[i]);
                                    listUser2.add(data2[i]+" "+ data1[i]);
                                }


                                adapter = new ArrayAdapter<String>(ListeUserAndAgents.this, android.R.layout.simple_list_item_1, android.R.id.text1, listUser2);
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }


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
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("montant"), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                    try {
                        Thread.sleep(2000);
                        //Toast.makeText(Inscription.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

        return super.onOptionsItemSelected(item);
    }

}
