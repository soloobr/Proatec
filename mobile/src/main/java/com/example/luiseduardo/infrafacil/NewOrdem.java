package com.example.luiseduardo.infrafacil;

import static android.R.layout.simple_spinner_item;

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
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

        private Button  btnabrir;

        private Button btnteste;

        private String TAG = NewOrdem.class.getSimpleName();
        private ProgressDialog pDialog;
        private Context context;

        EditText editDescri, editDesc, editData;
        AutoCompleteTextView autoNome ;
        AutoCompleteTextView autoTec;
        ImageButton btnDatePicker;

        private int mYear, mMonth, mDay, mHour, mMinute;


        private static String GETINFO_URL = "http://futsexta.16mb.com/Proatec/insert_OrdemMobile.php";
        private static String GETINFO_USER = "http://futsexta.16mb.com/Proatec/Infra_Get_clientes.php";
        private static String urlCont = "http://futsexta.16mb.com/Proatec/ordem_cont.php";
        private static String urlfunc = "http://futsexta.16mb.com/Proatec/Infra_Get_clientes.php";



        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        private static TextView labelvine ;
        private static String Descri_Servi = "";
        private static String Id_Prof = "";
        private static String Id_Proatec = "";

        private static String Tec_Resp = "";

        private static String Data_Previ = "";
        private static String Nome = "";
        private static String Data_Local = "";
        private static String Nomeok = "";
        public static String USERNAME = null;

        public  static List<String> categories;

        private SearchView searchView;
        private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private String[] options;
    private int selectedOptionIndex = 0;

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


        @SuppressLint({"WrongViewCast", "SuspiciousIndentation", "MissingInflatedId"})
        @Override
        protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_newordem);

        //AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        //acTextView.setAdapter(new SuggestionAdapter(this, acTextView.getText().toString()));

        //Spinner acTextView1 = (Spinner) findViewById(R.id.spinnertecnico);
        //acTextView1.setAdapter(new SuggestionAdapterTec(this, acTextView1.getText().toString()));

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //autoNome = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        //editDesc = (EditText) findViewById(R.id.editDescri);
        autoTec = (AutoCompleteTextView) findViewById(R.id.search_auto_complete_text);
        autoTec.setOnItemSelectedListener(this);

        //editData = (EditText) findViewById(R.id.editEmailcli);
        //editDescri = (EditText) findViewById(R.id.editDescri);

        tx = (TextView) findViewById(R.id.textViewNumCli);


            //searchView = findViewById(R.id.search_view);
            spinner = findViewById(R.id.search_spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.search_options, simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // lógica de filtragem aqui
                    Toast.makeText(NewOrdem.this, "Filtrando por: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    selectedOptionIndex = position;
                    adapter.getFilter().filter(searchView.getQuery());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // não é necessário implementar
                }
            });

//            searchView.setOnSearchClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    spinner.setVisibility(View.VISIBLE);
//                }
//            });

//            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//                    spinner.setVisibility(View.GONE);
//                    return false;
//                }
//            });

//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    // não é necessário implementar
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    // chama o método para filtrar os itens do spinner
//                    ((ArrayAdapter<Category>) spinner.getAdapter()).getFilter().filter(newText);
//                    return false;
//                }
//            });


            new CountDados().execute();
            //new NewOrdem.GetFunc().execute();

            new ExemploAsyncTask(NewOrdem.this,spinner).execute();


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

       // AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.autoComplete);
       // nome = usernameEditText.getText().toString();


        switch (v.getId()) {

            case R.id.btnSalvar:
                if (autoTec.toString().matches("")) {

                    AlertDialog ad = new AlertDialog.Builder(this).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button
                    ad.setMessage("Favor Preencher o Nome do Professor!");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                    break;
                } else {
                    {
                    }
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

      //  AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.autoComplete);
      //  nome = usernameEditText.getText().toString();



                if (autoTec.getText().toString().matches("")) {

                    AlertDialog ad = new AlertDialog.Builder(this).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button
                    ad.setMessage("Favor Preencher o Nome do Professor!");
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


            String nome = ((EditText) findViewById(R.id.search_auto_complete_text)).getText().toString();

            if (nome != null) {
                Nome = ((AutoCompleteTextView)findViewById(R.id.search_auto_complete_text)).getText().toString();
              //  Descri_Servi = ((EditText) findViewById(R.id.editDescri)).getText().toString();
                Tec_Resp = ((AutoCompleteTextView) findViewById(R.id.search_auto_complete_text)).toString();
                //Data_Previ = ((EditText) findViewById(R.id.editEmailcli)).getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Data_Local = date;

                // PEGAR DADOS DO LOGIN
                DadosLogin dadosLogin = DadosLoginSingleton.getInstance().getDadosLogin();
                Id_Proatec = dadosLogin.getName();


                //String message = "Name: " + savedUsername + ", Password: " + savedPassword + ", Remember password: " + rememberPassword;
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {

                // Building Parameters
                List params = new ArrayList();

                params.add(new BasicNameValuePair("nome", nome));
                params.add(new BasicNameValuePair("Data_Local", Data_Local));
                params.add(new BasicNameValuePair("Id_Prof", Id_Prof));
                params.add(new BasicNameValuePair("Id_Proatec", Id_Proatec));
                params.add(new BasicNameValuePair("Status", "Aguardando retorno"));


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


            //String nome = ((EditText) findViewById(R.id.autoComplete)).getText().toString();

            if (autoTec.getText().toString() != null) {
                Nome = ((AutoCompleteTextView)findViewById(R.id.search_auto_complete_text)).getText().toString();

            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {


            // Building Parameters
            List params = new ArrayList();

            params.add(new BasicNameValuePair("Nomecli", Nome));

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
                alerta.setTitle(Html.fromHtml("<font color='#FF7F27'>Professor não encontrado!</font>"));
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
                       // EditText editText = (EditText) findViewById(R.id.autoComplete);
                        USERNAME = autoTec.getText().toString();
                        intent.putExtra("STRING_NOME", USERNAME);
                        intent.putExtra("STRING_ATIVO", "Ativo");

                        startActivity(intent);


                    }
                });
                alerta.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        autoTec.requestFocus();
                        Toast.makeText(NewOrdem.this, "Verifique o nome do Professor", Toast.LENGTH_SHORT).show();

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

    class GetFunc extends AsyncTask<String, String,  String> implements AdapterView.OnItemSelectedListener {

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
            params.add(new BasicNameValuePair("Nomecli","%%"));

            JSONObject json = jsonParser.makeHttpRequest(urlfunc,"POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("clientes");

                    categories = new ArrayList<String>();
                    List<Category> categories = new ArrayList<>();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String tecresp = object.getString("Nome");

                        //categories.add(tecresp);
                        categories.add(new Category("1",tecresp));

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


//            //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewOrdem.this, android.R.layout.simple_spinner_item, categories);
//            //ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, simple_spinner_item, categories);
//            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
//            //ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(this, simple_spinner_item, categories);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            spinner.setOnItemSelectedListener(this);





        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    public class ExemploAsyncTask extends AsyncTask<Void, Void, List<Category>> implements AdapterView.OnItemSelectedListener {
        private Context context;
        private Spinner spinner;

        public ExemploAsyncTask(Context context, Spinner spinner) {
            this.context = context;
            this.spinner = spinner;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            List params = new ArrayList();
            params.add(new BasicNameValuePair("Nomecli","%%"));

            JSONObject json = jsonParser.makeHttpRequest(urlfunc,"POST",
                    params);

            Log.e("Profile JSON: ", json.toString());
            List<Category> categories = new ArrayList<>();

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("clientes");

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String tecresp = object.getString("Nome");
                        String id = object.getString("Id");
                        categories.add(new Category(id,tecresp));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {

            ArrayAdapter<Category> adapterr = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, categories);
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_auto_complete_text);
            autoCompleteTextView.setAdapter(adapterr);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Category selectedCategory = (Category) parent.getItemAtPosition(position);
                    String categoryName = selectedCategory.getName();
                    String categoryId = selectedCategory.getId();
                    Id_Prof = categoryId;
                    Toast.makeText(getApplicationContext(), "Selected: ID: "+categoryId+" - " + categoryName  , Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // lógica para quando um item for selecionado
            Toast.makeText(NewOrdem.this, "Filtrando por: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // lógica para quando nenhum item for selecionado
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
    public void showSearchDialog(final List<Category> categories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pesquisar");

        // Infla o layout customizado
        View customLayout = getLayoutInflater().inflate(R.layout.search_dialog_layout, null);
        builder.setView(customLayout);

        final EditText editText = customLayout.findViewById(R.id.edit_text);
        final ListView listView = customLayout.findViewById(R.id.list_view);
        //final ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, categories);
        //listView.setAdapter(adapter);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Fecha o dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Fecha o dialog
                dialog.dismiss();
            }
        });

        // Configura o filtro para o ListView
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra a lista
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não faz nada
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
class CustomFilter extends Filter {
    private ArrayAdapter<Category> adapter;
    private List<Category> originalList;
    private List<Category> filteredList;

    public CustomFilter(ArrayAdapter<Category> adapter, List<Category> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (TextUtils.isEmpty(constraint)) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final Category category : originalList) {
                if (category.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(category);
                }
            }
        }

        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.clear();
        adapter.addAll((List<Category>) results.values);
        adapter.notifyDataSetChanged();
    }
}

