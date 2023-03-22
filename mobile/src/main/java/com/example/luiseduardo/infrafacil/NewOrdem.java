package com.example.luiseduardo.infrafacil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Luis Eduardo on 09/11/2017.
 */

public class NewOrdem extends AppCompatActivity implements
        OnClickListener, AdapterView.OnItemSelectedListener {

        private String TAG = NewOrdem.class.getSimpleName();
        private ProgressDialog pDialog;
        private Context context;

        EditText editDescri, editDesc, editData;
        AutoCompleteTextView autoNome ;
        Spinner autoTec;
        ImageButton btnDatePicker;

        private int mYear, mMonth, mDay, mHour, mMinute;


        private static String GETINFO_URL = "http://futsexta.16mb.com/Poker/insert_OrdemMobile.php";
        private static String GETINFO_USER = "http://futsexta.16mb.com/Poker/Get_UserMobile.php";
        private static String urlCont = "http://futsexta.16mb.com/Poker/ordem_cont.php";
        private static String urlfunc = "http://futsexta.16mb.com/Poker/Infra_Get_func.php";



        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";


        private static String Descri_Servi = "";
        private static String Tec_Resp = "";
        private static String Data_Previ = "";
        private static String Nome = "";
        private static String Data_Local = "";
        private static String Nomeok = "";
        public static String USERNAME = null;

        public  static List<String> categories;


        JSONParser jsonParser = new JSONParser();
        JSONObject object =null;

        Spinner  spin;
        ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();

        private static ArrayList<ItemListView> itens;
        String[] Itemtar = { "Adicionar Tarefa", "Formatação", "Visita Técnica", "Conf. Router", "Instalar Office", "Outro"};
        ImageButton btnaddtar;

        private TextView tx;
        String contador, nnome;
        String getuser;
        String nome ;

        @SuppressLint("WrongViewCast")
        @Override
        protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_newordem);

        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        acTextView.setAdapter(new SuggestionAdapter(this, acTextView.getText().toString()));

        //Spinner acTextView1 = (Spinner) findViewById(R.id.spinnertecnico);
        //acTextView1.setAdapter(new SuggestionAdapterTec(this, acTextView1.getText().toString()));

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        autoNome = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        editDesc = (EditText) findViewById(R.id.editDescri);
        autoTec = (Spinner) findViewById(R.id.spinnertecnico);
        autoTec.setOnItemSelectedListener(this);

        editData = (EditText) findViewById(R.id.editEmailcli);
        editDescri = (EditText) findViewById(R.id.editDescri);

        tx = (TextView) findViewById(R.id.textViewNumCli);

        btnDatePicker = (ImageButton) findViewById (R.id.btn_date);
        btnDatePicker.setOnClickListener(this);
        btnaddtar = (ImageButton) findViewById(R.id.btnaddtar);

        spin = (Spinner) findViewById(R.id.spinnertarefa);

        spin.setEnabled(true);

            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Itemtar);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            newItemlist = new ArrayList<HashMap<String, String>>();


            spin.setAdapter(aa);
            spin.setSelection(0);

            new NewOrdem.CountDados().execute();
            new NewOrdem.GetFunc().execute();



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

    public void onClickAddtar(View v) {
        editDescri.setText(editDescri.getText().toString()+"\n"+spin.getSelectedItem().toString());
    }

    @SuppressLint("ResourceAsColor")
    public void onClickNEW(View v) {

        AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        nome = usernameEditText.getText().toString();


        switch (v.getId()) {

            case R.id.btnSalvar:
                if (nome.matches("")) {

                    AlertDialog ad = new AlertDialog.Builder(this).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button
                    ad.setMessage("Favor Preencher o Nome!");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                    break;
                } else {
                    new NewOrdem.GetUser().execute();
                    break;
                }

            case R.id.btnCancelar:
                finish();
                Toast.makeText(NewOrdem.this, "Chamado Cancelada", Toast.LENGTH_LONG).show();
                break;


            default:
                break;
        }
    }
    @SuppressLint("ResourceAsColor")
    public void onClickSAVE(View v) {

        AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        nome = usernameEditText.getText().toString();



                if (nome.matches("")) {

                    AlertDialog ad = new AlertDialog.Builder(this).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button
                    ad.setMessage("Favor Preencher o Nome!");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                } else {
                    new NewOrdem.GetUser().execute();
                }

    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date// Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            editData.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    class InsertDadosChamado extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewOrdem.this);
            pDialog.setMessage("Iniciando Chamado");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {


            String nome = ((EditText) findViewById(R.id.autoComplete)).getText().toString();



            if (nome != null) {
                Nome = ((AutoCompleteTextView)findViewById(R.id.autoComplete)).getText().toString();
                Descri_Servi = ((EditText) findViewById(R.id.editDescri)).getText().toString();
                Tec_Resp = ((Spinner) findViewById(R.id.spinnertecnico)).getSelectedItem().toString();
                Data_Previ = ((EditText) findViewById(R.id.editEmailcli)).getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Data_Local = date;
            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {

                // Building Parameters
                List params = new ArrayList();

                params.add(new BasicNameValuePair("Data_Local", Data_Local));
                params.add(new BasicNameValuePair("Data_Previ", Data_Previ));
                params.add(new BasicNameValuePair("nome", Nome));
                params.add(new BasicNameValuePair("Descri_Servi", Descri_Servi));
                params.add(new BasicNameValuePair("Tec_Resp", Tec_Resp));

                Log.d("Debug!", "starting");

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(GETINFO_URL, "POST",
                        params);


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("successo!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);

                } else {
                    Log.d("Ordem não inserida", json.getString(TAG_MESSAGE));
                    finish();
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(NewOrdem.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }


    class GetUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*            pDialog = new ProgressDialog(NewOrdem.this);
            pDialog.setMessage("Procurando Cliente");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }


        @Override
        protected String doInBackground(String... args) {


            String nome = ((EditText) findViewById(R.id.autoComplete)).getText().toString();

            if (nome != null) {
                Nome = ((AutoCompleteTextView)findViewById(R.id.autoComplete)).getText().toString();

            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {


            // Building Parameters
            List params = new ArrayList();

            params.add(new BasicNameValuePair("user", Nome));

            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GETINFO_USER, "POST",
                    params);

                success = json.getInt(TAG_SUCCESS);

                if (success == 0) {
                    Log.e("Usuario ", json.toString());
                    nnome = "NULO";
                   // finish();
                    return json.getString(TAG_MESSAGE);


                } else {
                    Log.e("Usuario  encontrado", json.getString(TAG_MESSAGE));
                    nnome = "OK";
                   // finish();
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }


        protected void onPostExecute(String result) {
           //Dialog.dismiss();
            Log.e("Usuario  encontrado", nnome);

            if (nnome.equals("NULO") ) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(NewOrdem.this);
                //alerta.setTitle("Usuário não encontrado");
                alerta.setTitle(Html.fromHtml("<font color='#FF7F27'>Cliente não encontrado!</font>"));
                alerta.setIcon(R.mipmap.newuser1);
                alerta.setMessage("Deseja cadastra-lo ?");

                alerta.setCancelable(false);

/*                alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                         Toast.makeText(NewOrdem.this, "Cancelar Escolhido", Toast.LENGTH_SHORT).show();

                    }
                });*/
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                       // Toast.makeText(NewOrdem.this, "Abrir Cadastro", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewOrdem.this, Cadastro_Clientes.class);
                        EditText editText = (EditText) findViewById(R.id.autoComplete);
                         USERNAME = editText.getText().toString();
                        intent.putExtra("key", USERNAME);
                        startActivity(intent);


                    }
                });
                alerta.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        autoNome.requestFocus();
                        Toast.makeText(NewOrdem.this, "Ajuste o nome do Cliente", Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog alertDialog =alerta.create();
                alertDialog.show();

                //Toast.makeText(NewOrdem.this, "Cliente Não encontrado", Toast.LENGTH_LONG).show();
            } else {
                new InsertDadosChamado().execute();
            }

        }





    }

    class GetFunc extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(Status_Ordem.this);
            //pDialog.setMessage("Aguarde Por Favor... ");
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected String  doInBackground(String... args) {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("NUM_Ocor","%%"));

            JSONObject json = jsonParser.makeHttpRequest(urlfunc,"GET",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("funcionario");

                    categories = new ArrayList<String>();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String tecresp = object.getString("nome");

                        categories.add(tecresp);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url) {
            //super.onPostExecute(result);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewOrdem.this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            autoTec.setAdapter(dataAdapter);
        }
    }

    private class CountDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlCont);


            if (jsonStr != null) {
                try {
                    Log.e(TAG,"Contador Não nulo");
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("ocorencia");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("NUM_Ocorrencia");
                        int id1 = c.getInt("NUM_Ocorrencia");
                        contador = String.valueOf(id1+1);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
                Log.e(TAG, "Couldn't get json from server.");
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

            tx.setText(contador);
         }

    }


}
