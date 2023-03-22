package com.example.luiseduardo.infrafacil;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.luiseduardo.infrafacil.JSONParser.json;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by Luis Eduardo on 07/11/2017.
 */

public class Cadastro_Clientes extends AppCompatActivity implements AdapterView.OnItemClickListener {
// AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static ArrayList<Map<String, String>> mPeopleList = MainActivity.mPeopleList;


    private SimpleAdapter mAdapter;
    private AutoCompleteTextView mTxtPhoneNo;
    private EditText editPhoneNo;
    private  TextView tvalte,  txconthistorico;
    private Switch  switchstatus;

    private String TAG = Cadastro_Clientes.class.getSimpleName();
    private ProgressDialog pDialog;
    private Context context;

    EditText  editPhonecli, editEmailcli;
    AutoCompleteTextView mTxtNome;

    private static String GETINFO_URL = "http://futsexta.16mb.com/Proatec/insert_clienteMobile.php";
    private static String url = "http://futsexta.16mb.com/Poker/Infra_Get_ordem_servicomobilecliente.php";
    private String contador, tvaltebtcancel;

    ArrayList<HashMap<String, String>> OcorList;
    private ArrayList<ItemListView> itens;
    ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    private AdapterListViewHistoricoCli adapterListView;

    private ListView lv;

    public static String IDORDEM = null;

    private static Button clear, btnSalv;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String NOME = "";
    private static String TELEFONE = "";
    private static String EMAIL = "";
    private static String ID = "";
    private  static  TextView textViewNumCli;


    private static String USERNAME = NewOrdem.USERNAME;
    private static String IDUSER = null;
    private static String MAIL = null;
    private static String FONE = null;
    private static String ATIVO = null;
    public String idcli = "0";
    JSONParser jsonParser = new JSONParser();
    private AutoCompleteTextView editNome;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activitt_cadastro_clientes);

        if (ContextCompat.checkSelfPermission(Cadastro_Clientes.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Cadastro_Clientes.this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {

                ActivityCompat.requestPermissions(Cadastro_Clientes.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        OcorList = new ArrayList<>();


        lv = (ListView) findViewById(R.id.lvhistoricocli);
        lv.setOnItemClickListener(this);


        editNome = (AutoCompleteTextView) findViewById(R.id.autoeditNome);
        editPhonecli = (EditText) findViewById(R.id.editPhonecli);
        editEmailcli = (EditText) findViewById(R.id.editEmailcadcli);
        textViewNumCli = (TextView) findViewById(R.id.textViewNumcadCli);
        txconthistorico = (TextView) findViewById(R.id.txconthistorico);
        switchstatus = (Switch) findViewById(R.id.switchstatus);
        Boolean switchState = switchstatus.isChecked();


        switchstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchstatus.isChecked() == true) {
                    switchstatus.setChecked(true);
                    switchstatus.setText("Cliente Ativo");
                    ATIVO = "Ativo";
                   // Toast.makeText(Cadastro_Clientes.this, "Is checked? "+switchstatus.isChecked(), Toast.LENGTH_SHORT).show();
                }
                if (switchstatus.isChecked() == false) {
                    switchstatus.setChecked(false);
                    switchstatus.setText("Cliente Inativo");
                    ATIVO = "Inativo";
                   // Toast.makeText(Cadastro_Clientes.this, "Is checked? "+switchstatus.isChecked(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSalv  = (Button)findViewById(R.id.btnSalvar);

        clear = (Button) findViewById(R.id.clear);



        clear.setVisibility(View.INVISIBLE);

        //PopulatePeopleList();

        editNome = (AutoCompleteTextView) findViewById(R.id.autoeditNome);



        editNome.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    clear = (Button) findViewById(R.id.clear);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear = (Button) findViewById(R.id.clear);
                    clear.setVisibility(View.GONE);
                }
            }
        });


        mAdapter = new SimpleAdapter(this, mPeopleList, R.layout.custcontview,
                new String[] { "Name", "Phone","Email", "Type" }, new int[] {
                R.id.ccontName, R.id.ccontNo,  R.id.ccontEmail, R.id.ccontType });
        editNome.setThreshold(1);
        editNome.setAdapter(mAdapter);
        requestLayout(mAdapter);


        //close button visibility for autocomplete text view selection
        editNome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                clear = (Button) findViewById(R.id.clear);
                clear.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                clear = (Button) findViewById(R.id.clear);
                clear.setVisibility(View.GONE);
            }

        });

        editNome.setOnItemClickListener(this);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("STRING_NOME");
            String i =(String) b.get("STRING_ID");
            String f =(String) b.get("STRING_FONE");
            String m =(String) b.get("STRING_MAIL");
            String s =(String) b.get("STRING_STATUS");
            String a =(String) b.get("STRING_ATIVO");


            tvalte = (TextView) findViewById(R.id.TextView000);
            tvalte.setText(s);

            tvaltebtcancel = "Edição";


            USERNAME = j;
            IDUSER = i;
            MAIL = m;
            FONE = f;
            ATIVO = a;

            if (a.equals("Ativo")){

                switchstatus.setChecked(true);
                switchstatus.setText("Cliente Ativo");
            }else{
                switchstatus.setChecked(false);
                switchstatus.setText("Cliente Inativo");
            }

            //editNome = (EditText) findViewById(R.id.editNome);
            textViewNumCli.setText(IDUSER);

            mTxtNome = findViewById(R.id.autoeditNome);
            //mTxtNome.requestFocus();
            mTxtNome.setText(USERNAME);

            if (FONE != null){
                editPhoneNo = (EditText) findViewById(R.id.editPhonecli);
                editPhoneNo.setText(FONE);
            }
            if (MAIL != null){
                editEmailcli.setText(MAIL);
            }
            //editPhone.requestFocus();
        }else{
            lv.setVisibility(View.INVISIBLE);
            txconthistorico.setText("0");
        }
        new GetDados().execute();


    }

    private void requestLayout(SimpleAdapter mAdapter) {
    }

    public void clear(View view) {
        //mTxtPhoneNo.setText("");
        mAdapter = new SimpleAdapter(this, mPeopleList, R.layout.custcontview,
                new String[] { "Name", "Phone", "Type" }, new int[] {
                R.id.ccontName, R.id.ccontNo, R.id.ccontType });
        editNome.setThreshold(1);
        editNome.setAdapter(mAdapter);
        requestLayout(mAdapter);
        clear.setVisibility(View.GONE);
    }
    private static void setText(AutoCompleteTextView view, CharSequence text, boolean filter) {
        try {
            Method method = AutoCompleteTextView.class.getMethod("setText", CharSequence.class, boolean.class);
            method.setAccessible(true);
            method.invoke(view, text, filter);
        } catch (Exception e) {
            //Fallback to public API which hopefully does mostly the same thing
            view.setText(text);
        }
    }


    public void PopulatePeopleList() {

        mPeopleList.clear();

        Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (people == null)
            return; // can't do anything with a null cursor.
        try {
            while (people.moveToNext()) {
                String contactName = people.getString(people.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = people.getString(people.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String hasPhone = people.getString(people.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if ((Integer.parseInt(hasPhone) > 0)) {

                    // You know have the number so now query it like this
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null, null);
                    if (phones == null)
                        return; // can't do anything with a null cursor.
                    try {
                        while (phones.moveToNext()) {

                            //store numbers and display a dialog letting the user select which.
                            String phoneNumber = phones.getString(
                                    phones.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                            String numberType = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.TYPE));

                            Map<String, String> NamePhoneType = new HashMap<String, String>();

                            NamePhoneType.put("Name", contactName);
                            NamePhoneType.put("Phone", phoneNumber);
                            NamePhoneType.put("Type", "Mobile");
                            if (numberType.equals("0"))
                                NamePhoneType.put("Type", "Work");
                            else if (numberType.equals("1"))
                                NamePhoneType.put("Type", "Home");
                            else if (numberType.equals("2"))
                                NamePhoneType.put("Type", "Mobile");
                            else
                                NamePhoneType.put("Type", "Other");

                            //Then add this map to the list.
                            mPeopleList.add(NamePhoneType);
                        }

                    } finally {
                    phones.close();
                    }

                }
            }
            //people.close();
            if (people != null)
                people.close();

    } finally {
            people.close();
    }

    }

    public void onItemClick(AdapterView<?> av, View v, int index, long arg){

        Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
        Iterator<String> myVeryOwnIterator = map.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            String value=(String)map.get(key);

            String name  = map.get("Name");
            String number = map.get("Phone");
            String email = map.get("Email");

            editNome.setText(name);
            editPhonecli.setText(number);
            editEmailcli.setText(email);

            if(number!=null){
                if (email == "não cadastrado") {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Cadastro_Clientes.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            editNome.getWindowToken(), 0);

                    editEmailcli.requestFocus();
                    editEmailcli.setText("");
                }else{
                    //ocultar teclado
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Cadastro_Clientes.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            editNome.getWindowToken(), 0);

                    btnSalv.setFocusable(true);
                    btnSalv.setFocusableInTouchMode(true);
                    btnSalv.requestFocus();
                }

            }else{
                editPhonecli.requestFocus();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode)
        {
            case (1) :
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contctDataVar = data.getData();

                    Cursor contctCursorVar = getContentResolver().query(contctDataVar, null,
                            null, null, null);
                    if (contctCursorVar.getCount() > 0)
                    {
                        while (contctCursorVar.moveToNext())
                        {
                            String ContctUidVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts._ID));

                            String ContctNamVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                            Log.i("Names", ContctNamVar);

                            if (Integer.parseInt(contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {
                                // Query phone here. Covered next
                                String ContctMobVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.i("Number", ContctMobVar);
                            }

                        }
                    }
                }
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSalvar:

                String nome ;

                //EditText usernameEditText = (EditText) findViewById(R.id.editNomecli);
                editNome = (AutoCompleteTextView) findViewById(R.id.autoeditNome);
                nome = editNome.getText().toString();
                NewOrdem.USERNAME = nome;
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
                }
                else {

                    new InsertDados().execute();
                    break;


                }

            case R.id.btnCancelar:
                finish();
                if (tvaltebtcancel == "Edição"){
                        Toast.makeText(Cadastro_Clientes.this, "Alteração de Cadastro Cancelada", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Cadastro_Clientes.this, "Cadastro Cancelado", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    class InsertDados extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cadastro_Clientes.this);
            pDialog.setMessage("Registrando Cliente");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            String nome = ((AutoCompleteTextView) findViewById(R.id.autoeditNome)).getText().toString();
            String telefone = ((EditText) findViewById(R.id.editPhonecli)).getText().toString();
            String email = ((EditText) findViewById(R.id.editEmailcadcli)).getText().toString();


            if (nome != null) {

                NOME = ((AutoCompleteTextView) findViewById(R.id.autoeditNome)).getText().toString();
                TELEFONE = ((EditText) findViewById(R.id.editPhonecli)).getText().toString();
                EMAIL = ((EditText) findViewById(R.id.editEmailcadcli)).getText().toString();
                ID = IDUSER;
                if (ATIVO == null){
                    ATIVO = "Ativo";
                }

            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("id", ID));
                params.add(new BasicNameValuePair("nome", NOME));
                params.add(new BasicNameValuePair("email", EMAIL));
                params.add(new BasicNameValuePair("telefone", TELEFONE));
                params.add(new BasicNameValuePair("status", ATIVO));

                Log.d("request!", "starting");

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
                    Log.d("O Cadastro Foi Atualizado!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(Cadastro_Clientes.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }
    class GetDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Cadastro_Clientes.this);
            pDialog.setMessage("Buscando Ocorrências...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            idcli = IDUSER;

            List params = new ArrayList();
            params.add(new BasicNameValuePair("idcli",idcli));

            JSONObject json = jsonParser.makeHttpRequest(url,"GET",
                    params);

             //Log.e(TAG, "Response from : " + json);

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("ocorencia");
                    //Log.e(TAG, "Count : " + eventDetails.length());
                    OcorList.clear();
                    itens = new ArrayList<ItemListView>();
                    newItemlist.clear();
                    int id1 = 0;
                    contador = null;
                    for (int i = 0; i < eventDetails.length(); i++) {
                        JSONObject c = eventDetails.getJSONObject(i);


                        String id = c.getString("Id");
                        String name = c.getString("Nome");
                        String descri = c.getString("Descricao");
                        String dataentrada = c.getString("Data_Entrada");
                        String datasaida = c.getString("Data_Saida");
                        String status = c.getString("Status");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("numero",id);
                        newItemlist.add(map);

                        id1 = id1+1;
                        contador = String.valueOf(id1);

                        //Log.e(TAG, "Status = " + status);

                        if (status.equals("Executando")) {
                            ItemListView item1 = new ItemListView(name,datasaida,dataentrada,id,status, R.mipmap.trabalho100);

                            itens.add(item1);
                        }

                        if (status.equals("Concluido")) {
                            ItemListView item2 = new ItemListView(name,datasaida,dataentrada,id,status, R.drawable.checkmark);

                            itens.add(item2);
                        }
                        if (status.equals("Aguardando Compra")) {
                            ItemListView item3 = new ItemListView(name,datasaida,dataentrada,id,status, R.drawable.shopping128);

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
/*                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });*/

                }
            } else {
/*                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });*/

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

             if (json.isEmpty()) {
                OcorList.clear();
                itens = new ArrayList<ItemListView>();
                newItemlist.clear();
                ItemListView item7 = new ItemListView("0", "Cliente não possue Chamados", "0","0","0", android.R.drawable.presence_busy);
                itens.add(item7);
                adapterListView = new AdapterListViewHistoricoCli(Cadastro_Clientes.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);

            }else{
                 txconthistorico.setText(contador);
                adapterListView = new AdapterListViewHistoricoCli(Cadastro_Clientes.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        //Toast.makeText(Ordem.this, "You Clicked at "+newItemlist.get(+position).get("numero"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Cadastro_Clientes.this, Status_Ordem.class);
                        IDORDEM = newItemlist.get(+position).get("numero");
                        intent.putExtra("key", IDORDEM);
                        startActivity(intent);
                    }
                });
            }
        }

    }
    public void onStop() {
        super.onStop();
        IDUSER = null;
        tvaltebtcancel = null;
    }


}
