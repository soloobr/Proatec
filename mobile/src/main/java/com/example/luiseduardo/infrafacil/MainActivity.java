package com.example.luiseduardo.infrafacil;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.NavUtils;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.SliceValue;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {




    public static ArrayList<Map<String, String>> mPeopleList;

    private static final int CONTACTS_PERMISSION_CODE = 101;
    private static final int CONTACTS_REQUEST = 1889;

    private ListView lv;
    ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> OcorList;
    SwipeRefreshLayout swipeLayout;

    private static String urlCont = "http://futsexta.16mb.com/Poker/ordem_cont.php";
    private static String urlContcli = "http://futsexta.16mb.com/Poker/cliente_cont.php";
    String contador;
    TextView NomeProatec;
    String contadorcli;

    public MainActivity() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        checkPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE);

        /*if (ContextCompat.checkSelfPermission (MainActivity.this, Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED)
        {
            // Permissão não concedida
            Toast.makeText(this, "Permissão não concedida " , Toast.LENGTH_LONG).show();
        }*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1 = new Intent(MainActivity.this, NewOrdem.class);
                startActivity(it1);
            }
        });



        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, Color.BLUE).setLabel("Compra"));
        pieData.add(new SliceValue(25, Color.GRAY).setLabel("Trabalhando"));
        pieData.add(new SliceValue(10, Color.RED).setLabel("Cancelado"));
        pieData.add(new SliceValue(60, Color.GREEN).setLabel("Comcluído"));




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



/*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1 = new Intent(MainActivity.this, NewOrdem.class);
                startActivity(it1);
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        OcorList = new ArrayList<>();
 //       lv = findViewById(R.id.list);
//        lv.setOnItemClickListener(this);



        newItemlist = new ArrayList<HashMap<String, String>>();

        mPeopleList = new ArrayList<Map<String, String>>();
        new PopulatePeopleList().executeOnExecutor(THREAD_POOL_EXECUTOR);
        new CountDados().execute();
        new CountCli().execute();


        swipeLayout = findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                new CountDados().execute();
                new CountCli().execute();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );


        DadosLogin dadosLogin = DadosLoginSingleton.getInstance().getDadosLogin();
        String nomeproatec = dadosLogin.getName();


        NomeProatec = (TextView) findViewById(R.id.NomeProatec);
        NomeProatec.setText(nomeproatec);

    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);



    }

    public void onClickItem(View v) {
        switch (v.getId()) {
            case R.id.listarchamados:

                Intent it1 = new Intent(MainActivity.this, Ordem.class);
                startActivity(it1);
                    break;
            case R.id.statisticas:

                Intent it2 = new Intent(MainActivity.this, Poker_main.class);
                startActivity(it2);
                //Toast.makeText(this, "Em Construção " , Toast.LENGTH_LONG).show();

                break;

            case R.id.clientes:

                Intent it3 = new Intent(MainActivity.this, Clientes.class);
                startActivity(it3);
                //Toast.makeText(this, "Logo Logo " , Toast.LENGTH_LONG).show();

                break;

            case R.id.financas:

                Intent it4 = new Intent(MainActivity.this, Financas.class);
                startActivity(it4);
                //Toast.makeText(this, "Em Construção " , Toast.LENGTH_LONG).show();

                break;
            case R.id.produtos:

                Intent i = new Intent(MainActivity.this, Produtos.class);
                String strName = null;

                i.putExtra("STRING_ORIGEM", "CONSULTA");

                startActivity(i);

                //Intent it5 = new Intent(MainActivity.this, Produtos.class);
                //startActivity(it5);

                break;
            default:
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chamado) {
            Intent it1 = new Intent(MainActivity.this, NewOrdem.class);
            startActivity(it1);
        } else if (id == R.id.nav_clientes) {
            Intent it1 = new Intent(MainActivity.this, Cadastro_Clientes.class);
            startActivity(it1);
        } else if (id == R.id.nav_listar_chamados) {
            Intent it1 = new Intent(MainActivity.this, Ordem.class);
            startActivity(it1);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    class PopulatePeopleList extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            mPeopleList.clear();
            ContentResolver cr = getContentResolver();

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_CODE);
                Toast.makeText(MainActivity.this, "sem permissão", Toast.LENGTH_SHORT).show();
            } else {
                //Intent cameraIntent = new Intent(ContactsContract.Contacts..MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, CONTACTS_REQUEST);
            }


            Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (people == null) {
                return null; //return; // can't do anything with a null cursor.
            }
            else {

               // Log.d("Não", "Contato não nulo");
            }

            try {
                while (people.moveToNext()) {
                    String contactName = people.getString(people.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = people.getString(people.getColumnIndex(
                            ContactsContract.Contacts._ID));
                    String hasPhone = people.getString(people.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // get the user's email address
                    String email = null;
                    Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);
                    if (ce != null && ce.moveToFirst()) {
                        email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        ce.close();
                    }

                    if ((Integer.parseInt(hasPhone) > 0)) {

                        // You know have the number so now query it like this
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null, null);

                        if (phones == null)
                            return null;// return; // can't do anything with a null cursor.
                        try {
                            while (phones.moveToNext()) {

                                //store numbers and display a dialog letting the user select which.
                                String phoneNumber = phones.getString(
                                        phones.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                                String numberType = phones.getString(phones.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE));

                                Map<String, String> NamePhoneType = new HashMap<String, String>();


                                if (email == null){
                                    email = "não cadastrado";
                                }

                                NamePhoneType.put("Name", contactName);
                                NamePhoneType.put("Phone", phoneNumber);
                                NamePhoneType.put("Type", "Mobile");
                                NamePhoneType.put("Email", email);

                                //Then add this map to the list.
                                mPeopleList.add(NamePhoneType);
                            }

                        } finally {

                            phones.close();
                        }

                    }
                }
            } finally {
                //Log.e("Contao", "Contatos Carregados");
                people.close();
            }
            return null;
        }

    }

    private class CountDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlCont);


            if (jsonStr != null) {
                try {
                    //Log.e(TAG, "Não nulo");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("ocorencia");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        int id1 = c.getInt("NUM_Ocorrencia");
                        contador = String.valueOf(id1);
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView Contar = (TextView)findViewById(R.id.progress_circle_text);
            //Contar.setText(contador);
        }

    }
    private class CountCli extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlContcli);


            if (jsonStr != null) {
                try {
                    //Log.e(TAG, "Não nulo");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("ocorencia");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        int id1 = c.getInt("Id");
                        contadorcli = String.valueOf(id1);
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView Contarcli = (TextView)findViewById(R.id.progress_circle_text1);
            //Contarcli.setText(contadorcli);
        }

    }



}
