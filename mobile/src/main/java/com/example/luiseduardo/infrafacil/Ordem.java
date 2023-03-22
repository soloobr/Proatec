package com.example.luiseduardo.infrafacil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Luis Eduardo on 04/11/2017.
 */

public class Ordem extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView lv;
    private String TAG = Ordem.class.getSimpleName();
    private ProgressDialog pDialog;
    private Context context;

    private AdapterListView adapterListView;
    private ArrayList<ItemListView> itens;

    ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();


    private Button btnAll, btnAberto;
    private TextView tx;
    String contador;

    private static String url = "http://futsexta.16mb.com/Poker/ordem_servicomobile.php";
    private static String urlAll = "http://futsexta.16mb.com/Proatec/ordem_servicomobile_GetAll.php";
    private static String urlCont = "http://futsexta.16mb.com/Poker/ordem_cont.php";

    ArrayList<HashMap<String, String>> OcorList;

    private static final String NUM_Ocorrencia = "NUM_Ocorrencia";
    private static final String Nome = "Nome";
    private static final String Descri_Servi = "Descri_Servi";
    private static final String status = "status";

    public static String IDORDEM = null;

    private View v;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_ordemserv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OcorList = new ArrayList<>();
        //tx =(TextView)findViewById(R.id.TextViwerOrdem);
        //
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(this);

        btnAll = (Button) findViewById(R.id.btntodos);
        btnAberto = (Button) findViewById(R.id.btnAberto);

        newItemlist = new ArrayList<HashMap<String, String>>();

       // new GetDados().execute();
       // new CountDados().execute();


    }


    public void onClickOrdem(View v) {
        if (v == btnAll) {

            new GetAllDados().execute();
            new CountDados().execute();
        }
        if (v == btnAberto) {

            new GetDados().execute();
            new CountDados().execute();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //Pega o item que foi selecionado.
        ItemListView item = adapterListView.getItem(arg2);

        //Demostração
        Intent it1 = new Intent(Ordem.this, Status_Ordem.class);

        //IDORDEM = newItemlist.get(+position).get("jogador");
        it1.putExtra("idOrdem", arg2);

        //it1.putExtra("key", IDORDEM);
        startActivity(it1);
        Toast.makeText(this, "Você Clicou em: " + item.getName(), Toast.LENGTH_LONG).show();
        finish();



    }

    @Override
    protected void onStart() {
        //super.onStart();
        super.onStart();
        new GetDados().execute();
        new CountDados().execute();

    }

    class GetDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Ordem.this);
            pDialog.setMessage("Buscando Ocorrências...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlAll);


           // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //Log.e(TAG, "Não nulo");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("ocorencia");

                    //Log.e(TAG, "Count : " + contacts.length());

                    //contador = String.valueOf(contacts.length()+1);
                    OcorList.clear();

                    itens = new ArrayList<ItemListView>();
                    newItemlist.clear();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String id = c.getString("Id");
                        String name = c.getString("Nome");
                        String descri = c.getString("Descricao");
                        String dataentrada = c.getString("Data_Entrada");
                        String datasaida = c.getString("Data_Saida");
                        String status = c.getString("Status");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("numero",id);
                        newItemlist.add(map);

                        //Log.e(TAG, "Status = " + status);

                        if (status.equals("Aguardando retorno")) {
                            ItemListView item1 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.boxout);

                            itens.add(item1);
                        }

                        if (status.equals("Concluido")) {
                            ItemListView item2 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.checkok);

                            itens.add(item2);
                        }
                        if (status.equals("Aguardando Compra")) {
                            ItemListView item3 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.trabalho100);

                            itens.add(item3);
                        }
                        if (status.equals("Aguardando Pag.")) {
                            ItemListView item4 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.money100);

                            itens.add(item4);
                        }
                        if (status.equals("Aguardando Retirada")) {
                            ItemListView item5 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.boxout128);

                            itens.add(item5);
                        }
                        if (status.equals("Orçamento não aprovado")) {
                            ItemListView item6 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.unlike);

                            itens.add(item6);
                        }
                        if (status.equals("Cancelado")) {
                            ItemListView item7 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.camcel);

                            itens.add(item7);
                        }

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("numero", id);
                        contact.put("nome", name);
                        contact.put("descri", descri);

                        // adding contact to contact list
                        OcorList.add(contact);
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
               // Log.e(TAG, "Couldn't get json from server.");
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
            // Dismiss the progress dialog

            //tx.setText(contador);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            /**
             * Updating parsed JSON data into ListView
             * */

            /**
             ListAdapter adapter = new SimpleAdapter(
             Ordem.this, OcorList,
             R.layout.list_item, new String[]{"numero", "nome", "descri"}, new int[]{R.id.numero, R.id.nome, R.id.descri});

             lv.setAdapter(adapter);
             **/

            adapterListView = new AdapterListView(Ordem.this, itens);
            lv.setAdapter(adapterListView);
            lv.setCacheColorHint(Color.TRANSPARENT);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //Toast.makeText(Ordem.this, "You Clicked at "+newItemlist.get(+position).get("numero"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Ordem.this, Status_Ordem.class);
                    IDORDEM = newItemlist.get(+position).get("numero");
                    intent.putExtra("key", IDORDEM);
                    startActivity(intent);
                }
            });
        }

    }
    private class GetAllDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Ordem.this);
            pDialog.setMessage("Aguarde Por Favor...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlAll);


            //Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //Log.e(TAG, "Não nulo");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("ocorencia");

                    Log.e(TAG, "Count : " + contacts.length());
                    Log.e(TAG, "JASON : " + jsonObj.getJSONArray("ocorencia"));

                    //contador = String.valueOf(contacts.length()+1);
                    OcorList.clear();

                    itens = new ArrayList<ItemListView>();
                    // looping through All Contacts
                    newItemlist.clear();
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String id = c.getString("Id");
                        String name = c.getString("Nome");
                        String descri = c.getString("Descricao");
                        String dataentrada = c.getString("Data_Entrada");
                        String datasaida = c.getString("Data_Saida");
                        String status = c.getString("Status");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("numero",id);
                        newItemlist.add(map);

                        if (status.equals("Aguardando retorno")) {
                            ItemListView item1 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.boxout);

                            itens.add(item1);
                        }
                        if (status.equals("Concluido")) {
                            ItemListView item2 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.checkok);

                            itens.add(item2);
                        }
                        if (status.equals("Aguardando Compra")) {
                            ItemListView item3 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.trabalho100);

                            itens.add(item3);
                        }
                        if (status.equals("Aguardando Pag.")) {
                            ItemListView item4 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.money100);

                            itens.add(item4);
                        }
                        if (status.equals("Aguardando Retirada")) {
                            ItemListView item5 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.boxout128);

                            itens.add(item5);
                        }
                        if (status.equals("Orçamento não aprovado")) {
                            ItemListView item6 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.unlike);

                            itens.add(item6);
                        }
                        if (status.equals("Cancelado")) {
                            ItemListView item7 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.camcel);

                            itens.add(item7);
                        }
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("numero", id);
                        contact.put("nome", name);
                        contact.put("descri", descri);

                        //Log.e(TAG, "Descri:" + contact);
                        // adding contact to contact list
                        OcorList.add(contact);
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

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            adapterListView = new AdapterListView(Ordem.this, itens);
            lv.setAdapter(adapterListView);
            lv.setCacheColorHint(Color.TRANSPARENT);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //Toast.makeText(Ordem.this, "You Clicked at "+newItemlist.get(+position).get("numero"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Ordem.this, Status_Ordem.class);
                    IDORDEM = newItemlist.get(+position).get("numero");
                    intent.putExtra("key", IDORDEM);
                    startActivity(intent);
                }
            });

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

                //tx.setText(contador);
                btnAll.setText("Todos " + contador);
            }

        }
    }

