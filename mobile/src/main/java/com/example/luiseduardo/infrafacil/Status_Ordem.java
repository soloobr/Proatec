package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.ServicoFragment.editServRealizado;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabItem;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Windows 10 on 13/12/2017.
 */

public class Status_Ordem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final int TIMEOUT_MILLISEC = 600;
    public static EditText editServRealizado2;


    public static String TAG = Ordem.class.getSimpleName();
    private ProgressDialog pDialog;
    private Context context;

    public static ArrayList<ItemListViewVendas> itens;
    public static ArrayList<ItemListViewVendas> itensvendas;
    String[] Itemtar = { "Adicionar Tarefa", "Formatação", "Visita Técnica", "Conf. Router", "Instalar Office", "Outro"};

    TextView numocor, status  ;
    EditText editDescri, DataPrev, DataDevol, editData;

    //EditText editServRealizado2;
    long idOrdem;

    AutoCompleteTextView edtnome, editTec;
    public  static EditText editValor,editValorpca,editValormo,editValorTotal ;

    JSONObject object =null;

    private Button btnedit, btnfinaliza;
    //private TextView tx;
    String nid, nnome, neditDescri, nDataPrev,  neditObs,  neditTec, neditValorP , neditValorM, neditValorTotal, neditStatus, nidcliocor;
    String ncomp;

    private static String url = "http://futsexta.16mb.com/Proatec/Get_OrdemM.php";
    //private static String urlvenda = "http://futsexta.16mb.com/Poker/Infra_Get_produtosvendido.php";
    private static String urldelvenda = "http://futsexta.16mb.com/Poker/Infra_Delete_produtosvendido.php";

    private static String GETINFO_URL = "http://futsexta.16mb.com/Proatec/Infra_Edit_OrdemMobile.php";
    private static String COMCLUIR_URL = "http://futsexta.16mb.com/InfraFacil/Infra_ordem_servico_Comcluir.php";
    private static String urlAll = "http://futsexta.16mb.com/Poker/ordem_servicomobile_GetAll.php";
    private static String IsertItem = "http://futsexta.16mb.com/P/IsertItem_OrdemMobile.php";
    private static String urlfunc = "http://futsexta.16mb.com/Proatec/Infra_Get_func.php";

    ArrayList<HashMap<String, String>> OcorList;
    ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    private ListView lv;

    private ImageButton btnDatePicker, btn_date, btnadd;
    private ImageButton btnaddtar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private AdapterListView adapterListView;
    Spinner spinner, editStatus, spin, spintec;

    public  static List<String> categories;

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem tabservico;
    TabItem tabpeca;

    JSONParser jsonParser = new JSONParser();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //private static final String TAG_PRODUCT = "ocorencia";
    //private static final String TAG_ID = "NUM_Ocorrencia";
    FragmentPagerAdapter adapterViewPager;

    public static String IDORDEM ;

    private static String Descri_Servi = "";
    private static String Tec_Resp = "";
    private static String Data_Previ = "";

    private static String Data_Devol = "";

    private static String nDataConc ="";
    private static String ID_Cliente_Ocor = "";
    private static String Nome = "";
    private static String Data_Local = "";
    private static String Servi_realizado = "";
    private static String Status = "";
    private static String Vlr_Total = "";
    public static String Vlr_MO = "";
    public static String Vlr_Peca = "";
    private static String Vlr_Parc = "";
    private static String datavenda = "";

    private static String SStatus = "";
    private static String SParcial = "";

    private static String Status_Comp = "";
    private static String Id_Comp = "";
    private static int number1, number2;

    public static String idvenda, idprod, idforne, valorvenda, valorcompra, qtdprodvend, descri, parcela, qtdparcela, valorparcela, idcliente,formadepagamento,valortotal,dataprev;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusordem);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viwerpage);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //TarefaName=new ArrayList<>();
        //spin = (Spinner) findViewById(R.id.spinnertarefa);
        //spin.setEnabled(false);

        //spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Itemtar);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        newItemlist = new ArrayList<HashMap<String, String>>();


//        spin.setAdapter(aa);
 //       spin.setSelection(0);

        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.editNome);
        acTextView.setAdapter(new SuggestionAdapter(this, acTextView.getText().toString()));


        spintec = (Spinner) findViewById(R.id.spinnertecnicostts);
        spintec.setEnabled(false);
        new GetFunc().execute();
        //AutoCompleteTextView acTextView1 = (AutoCompleteTextView) findViewById(R.id.editTecResp);
        //acTextView1.setAdapter(new SuggestionAdapterTec(this, acTextView1.getText().toString()));

        btnedit = (Button) findViewById(R.id.btnedit);
        btnedit.requestFocus();
        btnfinaliza = (Button) findViewById(R.id.btnfinaliza);
        //btnDatePicker = (ImageButton) findViewById(R.id.btn_date);
        //btnaddtar = (ImageButton) findViewById(R.id.btnaddtar);
        //btn_date = (ImageButton) findViewById(R.id.btn_date);
        //btnadd = (ImageButton) findViewById(R.id.itemadd);

        //btnDatePicker.setOnClickListener(this);

        numocor = (TextView) findViewById(R.id.textViewNumCli);
        status = (TextView) findViewById(R.id.textViewStatus);


        //Edittext
        //editDescri = (EditText) findViewById(R.id.editDescri);
        DataPrev = (EditText) findViewById(R.id.editEmailcli);
        //editServRealizado = (EditText) findViewById(R.id.editServRealizado);
        //editServRealizado2 = (EditText) findViewById(R.id.editServRealizado);
        editData = (EditText) findViewById(R.id.editEmailcli);
        DataDevol = (EditText) findViewById(R.id.datadevol);


        //autotext
        edtnome = (AutoCompleteTextView) findViewById(R.id.editNome);
       // editTec = (AutoCompleteTextView) findViewById(R.id.editTecResp);
        //editValorpca = (EditText) findViewById(R.id.editValor);
        //editValormo = (EditText) findViewById(R.id.editValormo);
        //editValorTotal = (EditText) findViewById(R.id.editValorTotal);
        //editStatus = (AutoCompleteTextView)findViewById(R.id.editStatus);
        //editStatus = (Spinner) findViewById(R.id.editStatus);


        //tabLayout = findViewById(R.id.tablayout);
        //tabpeca = findViewById(R.id.tabpeca);
        //tabservico = findViewById(R.id.tabservico);
        viewPager = findViewById(R.id.viwerpage);


        //pagerAdapter = new com.example.luiseduardo.infrafacil.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        //viewPager.setAdapter(pagerAdapter);

       /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //editServRealizado2.setEnabled(true);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

//        editValorpca.addTextChangedListener(new MoneyTextWatcher(editValorpca));
//        editValormo.addTextChangedListener(new MoneyTextWatcher(editValormo));
//        editValorTotal.addTextChangedListener(new MoneyTextWatcher(editValorTotal));

        Intent iin= getIntent();
        Bundle bb = iin.getExtras();

        if(bb!=null)
        {
            String j =(String) bb.get("key");
            IDORDEM = j;
        }

        new GetDados().execute();


        //new GetDadosVenda().execute();

        // Spinner element
        //final Spinner spinner = (Spinner) findViewById(R.id.editStatus);

        // Spinner click listener


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Concluido");
        categories.add("Executando");
        categories.add("Aguardando Compra");
        categories.add("Aguardando Pag.");
        categories.add("Aguardando Retirada");
        categories.add("Orçamento não aprovado");
        categories.add("Cancelado");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

//        editStatus.setEnabled(false);
  //      editStatus.setClickable(false);
  //      editStatus.setAdapter(dataAdapter);


        //Toast.makeText(Status_Ordem.this, "Item selecionado: " + SStatus, Toast.LENGTH_LONG).show();
        String valorBanco = SStatus;
        int posicaoArray = 0;

        for (int i = 0; (i <= categories.size() - 1); i++) {

            if (categories.get(i).equals(valorBanco)) {

                posicaoArray = i;
                break;
            } else {
                posicaoArray = 0;
            }
        }
        //editStatus.setSelection(posicaoArray);


       // spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       //     @Override
       //     public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

//
        //        if ("Concluido".matches(spinner.getSelectedItem().toString())) {
       //            // SStatus = spinner.getSelectedItem().toString();

       //         }
       //     }

       //     @Override
       //     public void onNothingSelected(AdapterView<?> parentView) {


        //    }

       // });

        //spinner.setSelection(getIndex(spinner, SStatus));
        //Toast.makeText(Status_Ordem.this, "Item selecionado: " + neditStatus, Toast.LENGTH_LONG).show();

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

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Status_Ordem.this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spintec.setAdapter(dataAdapter);
        }
    }

    public void onClickAddtar(View v) {
        editDescri.setText(editDescri.getText().toString()+"\n"+spin.getSelectedItem().toString());
    }
    @SuppressLint("ResourceAsColor")
    public void onClickEdit(View v) throws JSONException, IOException {
        switch (v.getId()) {

            case R.id.btnedit:

                String nome ;

                AutoCompleteTextView usernameEditText = (AutoCompleteTextView) findViewById(R.id.editNome);
                nome = usernameEditText.getText().toString();
                if ("SALVAR".matches(btnedit.getText().toString())) {
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
                    else{
                        new InserirPecanew().execute();

                        edtnome.setEnabled(false);
                        //editDescri.setEnabled(false);
                        //editTec.setEnabled(false);
                        spintec.setEnabled(false);
                        editServRealizado.setEnabled(false);

                       // editValorpca.setEnabled(false);
                       // editValormo.setEnabled(false);
                       // editValorTotal.setEnabled(false);
                        editStatus.setEnabled(false);
                        btnDatePicker.setEnabled(false);
                        btnedit.setText("Editar");

                        btnedit.setBackgroundColor(Color.GRAY);
                        btnedit.setTextColor(Color.BLACK);
//                        spin.setEnabled(false);

                        //Status_Comp = "";
                       // Spinner spinner = (Spinner)findViewById(R.id.editStatus);
                        //SStatus = spinner.getSelectedItem().toString();

                        new EditarChamado().execute();

                    }
                    break;
                }
                else {



                    edtnome.setEnabled(true);
                   // editDescri.setEnabled(true);
                    spintec.setEnabled(true);
                    //editServRealizado.setEnabled(true);
                    //editServRealizado2.setEnabled(true);
                  //  editValorpca.setEnabled(true);
                  //  editValormo.setEnabled(true);
                  //  editValorTotal.setEnabled(true);
//                    editStatus.setEnabled(true);
//                    btn_date.setSaveEnabled(true);
                  //  btnaddtar.setEnabled(true);
//                    btnDatePicker.setEnabled(true);
                    btnedit.setText("SALVAR");
                    editServRealizado.setEnabled(true);
                    PecaFragment.btnsaida.setEnabled(true);
                    PecaFragment.myrecyclerview.setEnabled(true);
                    btnfinaliza.setText("CANCELAR");
                    btnfinaliza.setEnabled(true);
                    btnedit.setBackgroundColor(Color.GREEN);
                    btnedit.setTextColor(Color.BLACK);
                    btnfinaliza.setBackgroundColor(Color.RED);
                    btnfinaliza.setTextColor(Color.WHITE);
                    //spin.setEnabled(true);

                    break;

                }

            case R.id.btnfinaliza:
                if ("CANCELAR".matches(btnfinaliza.getText().toString())) {
                    btnfinaliza.setText("FINALIZAR");
                    finish();
                    Toast.makeText(Status_Ordem.this, "Edição Cancelada", Toast.LENGTH_LONG).show();
                    break;
                }else{

                    AlertDialog.Builder alerta = new AlertDialog.Builder(Status_Ordem.this);
                    alerta.setTitle("Confirmar Devolução do Item?");
                    alerta.setIcon(R.mipmap.ic_launcher);
                    alerta.setMessage("Confirmar Devolução?");
                    //Vlr_Total = (((EditText)findViewById(R.id.editValorTotal)).getText().toString());
                    //alerta.setMessage("Data: "+nDataPrev+"\n"+"Valor: "+Vlr_Total);
                    //alerta.setView(edittextdata);
                    alerta.setCancelable(false);

                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                           // Toast.makeText(Status_Ordem.this, "Cancelar Escolhido", Toast.LENGTH_SHORT).show();

                            final EditText edittext = new EditText(Status_Ordem.this);
                            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                            edittext.addTextChangedListener(new MoneyTextWatcher(edittext));

                            AlertDialog.Builder alerta = new AlertDialog.Builder(Status_Ordem.this);
                            alerta.setTitle("Obteve Pagamento ?");
                            alerta.setIcon(R.mipmap.ic_launcher);
                            alerta.setMessage("Qual valor foi pago?");
                            // alerta.setView(edittext);
                            alerta.setView(edittext);
                            //alerta.setView(edittext.addTextChangedListener(new MoneyTextWatcher('0000'));
                            alerta.setCancelable(false);


                            alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Status_Comp = "Aguardando Pag.";
                                    new ConcluirChamado().execute();
                                    String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                                    Data_Local = date;
                                    nDataConc = Data_Local;
                                    SStatus = "Aguardando Pag.";
                                    Id_Comp = "2";

                                    SParcial = edittext.getText().toString();

                                    String SParcialold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());

                                    SParcial = SParcial.replaceAll(SParcialold, "");
                                    SParcial = SParcial.replaceAll(",", "");

                                    //SParcial = edittext.getText().toString();

                                   //-- new EditarChamado().execute();

                                    // Toast.makeText(Status_Ordem.this, SParcial, Toast.LENGTH_SHORT).show();

                                }
                            });
                            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Status_Comp = "Aguardando Pag.";
                                    new ConcluirChamado().execute();
                                    String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                                    Data_Local = date;
                                    nDataConc = Data_Local;
                                    SStatus = "Aguardando Pag.";
                                    Id_Comp = "2";

                                   //-- new EditarChamado().execute();

                                    // Toast.makeText(Status_Ordem.this, "OK Escolhido", Toast.LENGTH_SHORT).show();

                                }
                            });
                            alerta.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                   // Toast.makeText(Status_Ordem.this, "Cancelado", Toast.LENGTH_SHORT).show();

                                }
                            });
                            AlertDialog alertDialog =alerta.create();
                            alertDialog.show();

                        }
                    });
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {


                            Status_Comp = "Compensado";
                            new ConcluirChamado().execute();
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                            Data_Local = date;
                            nDataConc = Data_Local;
                            SStatus = "Concluido";
                            Id_Comp = "1";
                            ncomp = "1";
                           //-- new EditarChamado().execute();

                        }
                    });
                    alerta.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                             //Toast.makeText(Status_Ordem.this, "Cancelado", Toast.LENGTH_SHORT).show();

                        }
                    });
                    AlertDialog alertDialog =alerta.create();
                    alertDialog.show();



                }


                break;
            case R.id.btn_date:
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
                break;
            default:
                break;
        }
    }

    public void onClickDate(View v) {


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class GetDados extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Status_Ordem.this);
            pDialog.setMessage("Aguarde Por Favor... ");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String  doInBackground(String... args) {

            //String NUM_Ocor = "2";
            String NUM_Ocor = IDORDEM;

            List params = new ArrayList();
            params.add(new BasicNameValuePair("NUM_Ocor",NUM_Ocor));

            JSONObject json = jsonParser.makeHttpRequest(url,"GET",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("ordens");

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        int id1 = object.getInt("NUM_Ocorrencia");
                        String idprofessor = object.getString("Id_Professor");
                        String nameprofessor = object.getString("Nome_Professor");
                        String idproatec = object.getString("Id_Proatec");
                        String nameproatec = object.getString("Nome_Proatec");
                        String datesaida = object.getString("Data_Saida");
                        String dateentrada = object.getString("Data_Entrada");
                        String status = object.getString("Status");
                        String obs = object.getString("Obs");

                        nid = String.valueOf(id1);
                        nnome = nameprofessor;
                        nidcliocor = idprofessor;
                        nDataPrev = datesaida;
                        nDataConc = dateentrada;
                        neditTec = nameproatec;
                        neditStatus = status;
                        neditObs = obs;
                        Log.e("Editar Status!", neditStatus);
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
            dataprev = nDataPrev;
            idcliente = nidcliocor;
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            numocor.setText(nid);
            edtnome.setText(nnome);

            if (nDataPrev == "null" || nDataPrev == "0000-00-00"){
                //meuLinearLayout.setVisibility(View.GONE);
                DataPrev.setText("__/__/____");
            }else {
                DataPrev.setText(nDataPrev);
            }
            if (nDataConc == "null" || nDataConc == "0000-00-00"){
                //meuLinearLayout.setVisibility(View.GONE);
                DataDevol.setText("__/__/____");
            }else {
                DataDevol.setText(nDataConc);
            }
            editServRealizado.setText(neditObs);
            //ServicoFragment.editServRealizado2.setEnabled(false);
            PecaFragment.btnsaida.setEnabled(false);
            PecaFragment.myrecyclerview.setEnabled(false);

            //---editTec.setText(neditTec);
            selectValue(spintec,neditTec);
            //editValorpca.setText(neditValorP);

            //editValormo.setText(neditValorM);
            //editValorTotal.setText(neditValorTotal);
            //editValorTotal.setText("200");

            //Vlr_Peca = (((EditText)findViewById(R.id.editValor)).getText().toString());
            //String Vlr_Pexaold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            //Vlr_Peca = Vlr_Peca.replaceAll(Vlr_Pexaold, "");
            //Vlr_Peca = Vlr_Peca.replaceAll("[.]", "");
            //Vlr_Peca = Vlr_Peca.replaceAll("[,]", "");

            //Vlr_MO = ((EditText)findViewById(R.id.editValormo)).getText().toString();
            //String Vlr_MOold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            //Vlr_MO = Vlr_MO.replaceAll(Vlr_MOold, "");
            //Vlr_MO = Vlr_MO.replaceAll("[.]", "");
            //Vlr_MO = Vlr_MO.replaceAll("[,]", "");

            //Vlr_Total = ((EditText)findViewById(R.id.editValorTotal)).getText().toString();
            //String Vlr_Totalold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            //Vlr_Total = Vlr_Total.replaceAll(Vlr_Totalold, "");
            //Vlr_Total = Vlr_Total.replaceAll("[.]", "");
            //Vlr_Total = Vlr_Total.replaceAll("[,]", "");



            //int number1 = (int)Double.parseDouble(Vlr_Peca);
            //int number2 = (int)Double.parseDouble(Vlr_MO);

           // int res = number2 + number1;
            // editValorTotal.setText(res);
            // editValorTotal.setText(String.valueOf(res));
            // -- editValormo.setText(String.valueOf(res));
            //editStatus.setText(neditStatus);
            status.setText(neditStatus);
            SStatus = neditStatus;
            //setSpinText(spinner,neditStatus);
            //spinner.setSelection(getIndex(spinner, SStatus));

            //if (neditStatus.equals("Concluido")) {
               // if (ncomp == "2") {
               //     btnfinaliza.setText("Conf. Pag.");
                  //  status.setTextColor(Color.parseColor("#ff669900"));
                    //Toast.makeText(Status_Ordem.this, "Concluir", Toast.LENGTH_SHORT).show();
               // } else {
            //        btnfinaliza.setText("Concluído");
            //        status.setTextColor(Color.parseColor("#ff669900"));
            //        btnfinaliza.setEnabled(false);
                //}
            //}else{
            //    if (neditStatus.equals("Aguardando Pag.")){
            //        btnfinaliza.setText("Conf. Pag.");
            //        status.setTextColor(Color.parseColor("#ff669900"));
            //    }

            //}

                    List<String> categories = new ArrayList<String>();
                    categories.add("Concluido");
                    categories.add("Executando");
                    categories.add("Aguardando Compra");
                    categories.add("Aguardando Pag.");
                    categories.add("Aguardando Retirada");
                    categories.add("Orçamento não aprovado");
                    categories.add("Cancelado");

                    String valorBanco = SStatus;
                    int posicaoArray = 0;

                    for(int i=0; (i <= categories.size()-1) ; i++){

                        if(categories.get(i).equals(valorBanco)){

                            posicaoArray = i;
                            break;
                        }else{
                            posicaoArray = 0;
                        }
                    }
                   // editStatus.setSelection(posicaoArray);
         }
    }


    class EditarChamado extends AsyncTask<String, String, String>  {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Status_Ordem.this);
            pDialog.setMessage("Atualizando Chamado");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {


            String nome = ((EditText) findViewById(R.id.editNome)).getText().toString();
            String status = SStatus;
            String parcial = SParcial;


            if (nome != null) {


                Nome = ((AutoCompleteTextView)findViewById(R.id.editNome)).getText().toString();
                //Descri_Servi = ((EditText) findViewById(R.id.editDescri)).getText().toString();
                //Tec_Resp = ((EditText) findViewById(R.id.editTecResp)).getText().toString();
                Tec_Resp = ((Spinner) findViewById(R.id.spinnertecnicostts)).getSelectedItem().toString();
                Data_Previ = ((EditText) findViewById(R.id.editEmailcli)).getText().toString();
                Data_Devol = ((EditText) findViewById(R.id.datadevol)).getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Data_Local = date;
                Servi_realizado = ((EditText) findViewById(R.id.editServRealizado)).getText().toString();
                Servi_realizado = editServRealizado.getText().toString();
                Status = status;//((AutoCompleteTextView)findViewById(R.id.editStatus)).getText().toString();


                //Vlr_Peca = (((EditText)findViewById(R.id.editValor)).getText().toString());

                //String Vlr_Pexaold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
                //Vlr_Peca = Vlr_Peca.replaceAll(Vlr_Pexaold, "");
                //Vlr_Peca = Vlr_Peca.replaceAll(",", "");


               //Vlr_MO = ((EditText)findViewById(R.id.editValormo)).getText().toString();

                String Vlr_MOold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
                Vlr_MO = Vlr_MO.replaceAll(Vlr_MOold, "");
                Vlr_MO = Vlr_MO.replaceAll("[.]", "");
                Vlr_MO = Vlr_MO.replaceAll("[,]", "");

               // Vlr_Total = (((EditText)findViewById(R.id.editValorTotal)).getText().toString());

                String Vlr_Totalold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
                Vlr_Total = Vlr_Total.replaceAll(Vlr_Totalold, "");
                Vlr_Total = Vlr_Total.replaceAll("[.]", "");
                Vlr_Total = Vlr_Total.replaceAll("[,]", "");

                int number1 = (int)Double.parseDouble(Vlr_Peca);
                int number2 = (int)Double.parseDouble(Vlr_MO);

                int res = number2 + number1;
                // editValorTotal.setText(res);
                Vlr_Total = (String.valueOf(res));

                Vlr_Parc = parcial ;



            } else {

                Log.d("Não", "Nome não preenchido");
            }

            int success;
            try {

                String NUM_Ocor = IDORDEM;
                String Conclusao = nDataConc;
                String Id_Compensa = ncomp;

                // Building Parameters
                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", NUM_Ocor));
                params.add(new BasicNameValuePair("Data_Previ", Data_Previ));
                params.add(new BasicNameValuePair("Data_Conclusao", Conclusao));
                params.add(new BasicNameValuePair("ID_Cliente_Ocor", nidcliocor));
                params.add(new BasicNameValuePair("Descri_Servi", Descri_Servi));
                params.add(new BasicNameValuePair("Servi_realizado", Servi_realizado));
                params.add(new BasicNameValuePair("Status", Status));
                params.add(new BasicNameValuePair("Tec_Resp", Tec_Resp));
                params.add(new BasicNameValuePair("Vlr_Total", Vlr_Total));
                params.add(new BasicNameValuePair("Vlr_Peca", Vlr_Peca));
                params.add(new BasicNameValuePair("Vlr_MO", Vlr_MO));
                params.add(new BasicNameValuePair("Id_Compensa", Id_Compensa));
                params.add(new BasicNameValuePair("Vlr_Parc", Vlr_Parc));


                Log.d("Debug!", "starting");

                // getting product details by making HTTP request
                JSONObject newjson = jsonParser.makeHttpRequest(GETINFO_URL, "POST",
                        params);

                //Id_Comp = "0";
                // json success tag
                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Editar successo!", newjson.toString());
                    finish();
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Ordem não Atualizada", newjson.getString(TAG_MESSAGE));
                    finish();
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (file_url != null) {
                Toast.makeText(Status_Ordem.this,  file_url, Toast.LENGTH_LONG).show();
            }

        }

    }
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    public static class somatValue {

        public static void SomaVendas(int vlvenda) {

                String Vlr_Pexaold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
                Vlr_Peca = Vlr_Peca.replaceAll(Vlr_Pexaold, "");
                Vlr_Peca = Vlr_Peca.replaceAll("[.]", "");
                Vlr_Peca = Vlr_Peca.replaceAll("[,]", "");


                String Vlr_MOold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
                Vlr_MO = Vlr_MO.replaceAll(Vlr_MOold, "");
                Vlr_MO = Vlr_MO.replaceAll("[.]", "");
                Vlr_MO = Vlr_MO.replaceAll("[,]", "");

                int number1 = (int)Double.parseDouble(Vlr_Peca);
                int number2 = vlvenda;
                int number3 = (int)Double.parseDouble(Vlr_MO);

                int vlttvendas = number1 + vlvenda;

                int res = number1 + number2 + number3;

                Vlr_Total = (String.valueOf(res));
                editValorTotal.setText(String.valueOf(res));
                editValorpca.setText(String.valueOf(vlttvendas));

            return ;
        }

        public static void SubitraiVendas(int vlvenda) {

            Vlr_Peca = Status_Ordem.editValorpca.getText().toString();
            String Vlr_Pexaold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            Vlr_Peca = Vlr_Peca.replaceAll(Vlr_Pexaold, "");
            Vlr_Peca = Vlr_Peca.replaceAll("[.]", "");
            Vlr_Peca = Vlr_Peca.replaceAll("[,]", "");


            String Vlr_MOold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            Vlr_MO = Vlr_MO.replaceAll(Vlr_MOold, "");
            Vlr_MO = Vlr_MO.replaceAll("[.]", "");
            Vlr_MO = Vlr_MO.replaceAll("[,]", "");

            int number1 = (int)Double.parseDouble(Vlr_Peca);
            int number2 = vlvenda;
            int number3 = (int)Double.parseDouble(Vlr_MO);

            int vlttvendas = number1 - vlvenda;

            int res = vlttvendas + number3;

            Vlr_Total = (String.valueOf(res));
            editValorTotal.setText(String.valueOf(res));
            editValorpca.setText(String.valueOf(vlttvendas));

            return ;
        }
    }

    class ConcluirChamado extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Status_Ordem.this);
            pDialog.setMessage("Finalizando Chamado");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {

            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                Data_Local = date;
            int success;
            try {

                String NUM_Ocor = IDORDEM;

                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", NUM_Ocor));
                params.add(new BasicNameValuePair("Data_Comp", Data_Local));
                params.add(new BasicNameValuePair("Status_Comp", Status_Comp));

                Log.d("Debug!", "starting");

                // getting product details by making HTTP request
                JSONObject newjson = jsonParser.makeHttpRequest(COMCLUIR_URL, "POST",
                        params);


                // json success tag
                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Finalizado com successo!", newjson.toString());
                    finish();
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Ordem não Atualizada", newjson.getString(TAG_MESSAGE));
                    finish();
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }


    }

    public static class ExcluiDadosVenda extends AsyncTask<String, String, String> {

        private JSONParser jsonParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            
            String NUM_Ocor = IDORDEM;
            Log.e("Profile IDOCOR: ", IDORDEM);

            List params = new ArrayList();
            params.add(new BasicNameValuePair("IDOcor",NUM_Ocor));
            params.add(new BasicNameValuePair("idvenda",idvenda));
            params.add(new BasicNameValuePair("idproduto  ",idprod));

            JSONObject newjson = jsonParser.makeHttpRequest(urldelvenda,"POST",
                    params);
            Log.i("Profile JSON: ", newjson.toString());

            int success;
            if (newjson != null) {
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(newjson));
                    JSONArray contacts = jsonObj.getJSONArray("produtos");
                    //Log.e(TAG, "Count : " + contacts.length());

                    success = newjson.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Item Excluido!", newjson.toString());
                       // finish();
                        return newjson.getString(TAG_MESSAGE);

                    } else {
                        Log.d("Item nãoencontrado", newjson.getString(TAG_MESSAGE));
                        //finish();
                        return newjson.getString(TAG_MESSAGE);
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

            if (file_url != null) {
                //Toast.makeText(Context.this,  file_url, Toast.LENGTH_LONG).show();
            }
            VendasAdapter.notifyItemChanged();
        }
    }

    class InserirPeca extends AsyncTask<String, String, String>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                String NUM_Ocor = IDORDEM;
                List params = new ArrayList();
                params.add(new BasicNameValuePair("idvenda", idvenda));
                params.add(new BasicNameValuePair("NUM_Ocor", NUM_Ocor));
                params.add(new BasicNameValuePair("idprod", idprod));
                params.add(new BasicNameValuePair("quantidadeproduto", qtdprodvend));
                params.add(new BasicNameValuePair("idfornecedor", idforne));
                params.add(new BasicNameValuePair("valorvendido", valorvenda));
                params.add(new BasicNameValuePair("formadepagamento", "Dinheiro - Avista"));
                params.add(new BasicNameValuePair("datavenda", date));


                Log.e("Debug!", "starting");

                // getting product details by making HTTP request
                JSONObject newjson = jsonParser.makeHttpRequest(IsertItem, "POST",
                        params);
                // json success tag
                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Insert Item successo!", newjson.toString());
                    //finish();
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Item não Atualizado", newjson.getString(TAG_MESSAGE));
                    //finish();
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    class InserirPecanew extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            datavenda = date;
            String NUM_Ocor = IDORDEM;
            //Create JSON string start
            //String json_string = "{\"despesas\":[";
            String json_string = "[";
           // Log.e("PARCELAS: ",  String.valueOf(PecaFragment.lsvendas.size()));

            for (int counter = 0; counter < PecaFragment.lsvendas.size(); counter++) {

                //System.out.println(PecaFragment.lsvendas.get(counter));
                idvenda = PecaFragment.lsvendas.get(counter).getIdvenda();
                idprod = PecaFragment.lsvendas.get(counter).getIdprod();
                //qtdprodvend = PecaFragment.lsvendas.get(counter).getQtd();
                idcliente = idcliente;
                //IDORDEM
                //datavenda =  PecaFragment.lsvendas.get(counter).getDatavenda();
                //idforne = PecaFragment.lsvendas.get(counter).getIdforne();
                //valorvenda = PecaFragment.lsvendas.get(counter).getValoruni();
                //valorcompra = PecaFragment.lsvendas.get(counter).getValorpago();
                //valortotal = PecaFragment.lsvendas.get(counter).getValortotal();
                //formadepagamento = PecaFragment.lsvendas.get(counter).getFormadepagamento();
                //status
                //qtdparcela = PecaFragment.lsvendas.get(counter).getQtdparcel();
                //parcela = PecaFragment.lsvendas.get(counter).getParcela();

                //valorparcela = PecaFragment.lsvendas.get(counter).getValorparcela();
                descri = PecaFragment.lsvendas.get(counter).getDescricao();


                //if (qtdparcela == "0") {


                JSONObject obj_new = new JSONObject();
                try {
                    obj_new.put("idproduto", idprod);
                    obj_new.put("quantidadeproduto", qtdprodvend);
                    obj_new.put("idcliente", idcliente);
                    obj_new.put("idocorrencia", IDORDEM);
                    obj_new.put("datavenda", datavenda);
                    obj_new.put("idfornecedor", idforne);
                    obj_new.put("valorvendido", valorvenda);
                    obj_new.put("valorpago", valorcompra);
                    obj_new.put("valortotal", valortotal);
                    obj_new.put("formadepagamento", formadepagamento);
                    obj_new.put("status", "Executando");
                    obj_new.put("qtdparcela", qtdparcela);
                    obj_new.put("parcela", parcela);
                    obj_new.put("valorparcela", valorparcela);

                    json_string = json_string + obj_new.toString() + ",";
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                /*}else{
                    idvenda = PecaFragment.lsvendas.get(counter).getIdvenda();
                    idprod = PecaFragment.lsvendas.get(counter).getIdprod();
                    qtdprodvend = PecaFragment.lsvendas.get(counter).getQtd();
                    idcliente = PecaFragment.lsvendas.get(counter).getIdcliente();

                    datavenda =  PecaFragment.lsvendas.get(counter).getDatavenda();
                    idforne = PecaFragment.lsvendas.get(counter).getIdforne();
                    valorvenda = PecaFragment.lsvendas.get(counter).getValoruni();
                    valortotal = PecaFragment.lsvendas.get(counter).getValortotal();
                    //formadepagamento
                    //status
                    qtdparcela = PecaFragment.lsvendas.get(counter).getQtdparcel();
                    valorparcela = PecaFragment.lsvendas.get(counter).getValorparcela();
                    descri = PecaFragment.lsvendas.get(counter).getName();

                    //qtdparcela = PecaFragment.lsvendas.get(counter).getQtdparcela();

                    int i = Integer.parseInt (qtdparcela);
                    int contador = 1;

                    for (int counterr = 0; counterr < i; counterr++) {


                        String qtdparcelanew = String.valueOf(contador)+qtdparcela ;

                        JSONObject obj_new = new JSONObject();
                        try {
                            obj_new.put("idproduto", idprod);
                            obj_new.put("quantidadeproduto", qtdprodvend);
                            obj_new.put("idcliente", idcliente);
                            obj_new.put("idocorrencia", IDORDEM);
                            obj_new.put("datavenda", datavenda);
                            obj_new.put("idfornecedor", idforne);
                            obj_new.put("volorvendido", valorvenda);
                            obj_new.put("valortotal", valortotal);
                            obj_new.put("formadepagamento", "Dinheiro - Avista");
                            obj_new.put("status", "Executando");
                            obj_new.put("qtdparcela", qtdparcela);
                            obj_new.put("valorparcela", valorparcela);

                            json_string = json_string + obj_new.toString() + ",";
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        contador = contador+1;
                    }
                } */

            }

            //Close JSON string
            json_string = json_string.substring(0, json_string.length() - 1);
            json_string += "]";

            Log.d("FISHY",json_string);


            try {
                //json_string = "[{\"idproduto\":\"68\", \"quantidadeproduto\": \"1\", \"valorvenda\": \"1000\"},{\"idproduto\": \"68\", \"quantidadeproduto\": \"1\", \"valorvenda\": \"2000\"}]";
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                String url = "http://futsexta.16mb.com/InfraFacil/IsertItem_OrdemMobile1.php";

                HttpPost request = new HttpPost(url);
                request.setEntity(new ByteArrayEntity(json_string.getBytes("UTF8")));
                request.setHeader("json", json_string);
                HttpResponse response = client.execute(request);
                Log.d("FISHY", response.getStatusLine().toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return date;
        }
    }

    public static class MoneyTextWatcher implements TextWatcher {
        private WeakReference<EditText> editTextWeakReference;
        private final Locale locale = Locale.getDefault();

        public MoneyTextWatcher(EditText editText) {
            this.editTextWeakReference = new WeakReference<>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.equals("")){

            }else {
                if (s.length() > 2)
                {
                    //somatValue.SomaVendas(0);
                }

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            editText.removeTextChangedListener(this);

            BigDecimal parsed = parseToBigDecimal(editable.toString());
            String formatted;
            formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

            //Remove o símbolo da moeda e espaçamento pra evitar bug
            //String replaceable = String.format("[%s\\s]", getCurrencySymbol());
            //String cleanString = formatted.replaceAll(replaceable, "");

            //editText.setText(cleanString);
            editText.setText(formatted);
            //editText.setSelection(cleanString.length());
            editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);



        }


        public void afterTextChangeBanco(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            editText.removeTextChangedListener(this);

            BigDecimal parsed = parseToBigDecimal(editable.toString());
            String formatted;
            formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

            //Remove o símbolo da moeda e espaçamento pra evitar bug
            String replaceable = String.format("[%s\\s]", getCurrencySymbol());
            String cleanString = formatted.replaceAll(replaceable, "");

            editText.setText(cleanString);
            //editText.setText(formatted);
            editText.setSelection(cleanString.length());
            //editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);
        }

        private BigDecimal parseToBigDecimal(String value) {
            String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());

            String cleanString = value.replaceAll(replaceable, "");

            try {
                return new BigDecimal(cleanString).setScale(
                        2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
            } catch (NumberFormatException e) {
                //ao apagar todos valores de uma só vez dava erro
                //Com a exception o valor retornado é 0.00
                return new BigDecimal(0);

            }
        }

        public static String formatPrice(String price) {
            //Ex - price = 2222
            //retorno = 2222.00
            DecimalFormat df = new DecimalFormat("0,00");
            return String.valueOf(df.format(Double.valueOf(price)));

        }

        public static String formatTextPrice(String price) {
            //Ex - price = 3333.30
            //retorna formato monetário em Br = 3.333,30
            //retorna formato monetário EUA: 3,333.30
            //retornar formato monetário de alguns países europeu: 3 333,30
            BigDecimal bD = new BigDecimal(formatPriceSave(formatPrice(price)));
            String newFormat = null;
            newFormat = String.valueOf(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(bD));
            String replaceable = String.format("[%s]", getCurrencySymbol());
            return newFormat.replaceAll(replaceable, "");

        }

        static String formatPriceSave(String price) {
            //Ex - price = $ 5555555
            //return = 55555.55 para salvar no banco de dados
            String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
            String cleanString = price.replaceAll(replaceable, "");
            StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

            return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));

        }

        static String formatPriceSaveBanco(String price) {
            //Ex - price = $ 5555555
            //return = 55555.55 para salvar no banco de dados
            String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
            String cleanString = price.replaceAll(replaceable, "");
            StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

            return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));



        }

        public static String getCurrencySymbol() {
            Locale meuLocal = new Locale( "pt", "BR" );
            //return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
            return NumberFormat.getCurrencyInstance(meuLocal).getCurrency().getSymbol();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        btn_date.setEnabled(false);
        //btnaddtar.setEnabled(false);
        VendasAdapter.notifyItemChanged();
    }


    //@Override
    //public void onBackPressed() {
     //   somatValue.SomaVendas(0);
    //}
    @Override
    protected void onResume() {
        super.onResume();

    }



    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return PecaFragment.newInstance(1, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ServicoFragment.newInstance(0, "Page # 2");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return "ITENS";
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return "OBS.";
                default:
                    return "Page " + position;
            }

        }

    }
}
