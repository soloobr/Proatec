package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.JSONParser.json;
import static com.example.luiseduardo.infrafacil.Poker_new.MoneyTextWatcher.getCurrencySymbol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Poker_main extends AppCompatActivity implements AdapterView.OnItemClickListener,DatePickerDialog.OnDateSetListener {
    private ProgressDialog pDialog;
    private static String urlAll = "http://futsexta.16mb.com/Poker/poker_get_jogo.php";
    private static String urlGetJogoFordate = "http://futsexta.16mb.com/Poker/poker_get_jogo_date.php";
    private static String url = "http://futsexta.16mb.com/Poker/ordem_servicomobile.php";
    private static String urlvenda = "http://festabrinka.com.br/Infra_Get_produtosvendido.php";
    ArrayList<HashMap<String, String>> OcorList;
    private static String urlFornecedor = "http://futsexta.16mb.com/Poker/Infra_Get_fornecedor.php";
    private AdapterListViewPoker adapterListView;
    //public static ArrayList itens = null;
    ArrayList<ItemListViewPoker> itens = new ArrayList<>();
    ArrayList<ItemListViewFornecedor> itensfornecedor = new ArrayList<>();
    //ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    //List<ItemListViewFornecedor> rowItems;
    Bitmap bitmap;
    private ListView lv;
    List<ItemListViewFornecedor> rowItems;
    String[] Itemtar1 = { "Adicionar Tarefa", "Formatação", "Visita Técnica", "Conf. Router", "Instalar Office", "Outro"};

    private String TAG = Produtos.class.getSimpleName();
    private String qtdvendalaste, qtdvendanow, somaqtdnew,qtdprodvend, idvenda, idprod,  qtd,    idocor, datavenda,  idforne,  valoruni, valorpago,  valortotal,  formadepagamento,  status,  parcela, qtdparcel,  valorparcela,  name, descri;
    public String  searchidata = "0";
    public static String Origem, idjogo,descrijogo,qtdplayers;
    private RadioButton buttonavista, buttonparcelado;

    private  ArrayList<ItemListViewFornecedor> mFornecedorList;
    private  AdapterSpinnerFornecedor mAdapter;
    private Spinner spinnerteste;
    private String idf;
    SearchView searchView;
    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    JSONObject objectP =null;
    ListView listView;
    ArrayList<String> list;
    ArrayList<String> listfornecedor;
    ArrayAdapter<String > adapter;
    private static String IsertItem = "http://futsexta.16mb.com/Poker/IsertItem_OrdemMobile.php";
    private static String GETINFO_URL = "http://futsexta.16mb.com/Poker/Poker_insert_Jogo.php";
    private static String URLDELETE = "http://futsexta.16mb.com/Poker/Poker_Delete_Jogo.php";
    private static String URLUPJOGO = "http://futsexta.16mb.com/Poker/Poker_Edit_Jogo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ImageView imgaddnew, btnRefresh;
    private ImageButton btnDatePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static String Mes,Dia,Ano,sData;
    ImageButton btndata;
    TextView dateEditText;

    private View v;
    public static String IDORDEM = Status_Ordem.IDORDEM;
    public static String IDCLIENTE = Status_Ordem.idcliente;
    public VendasAdapter vendasAdapter;
    //public static RecyclerView myrecyclerview;
    //public static List<Vendas> lsvendas;
    private  int Pozi;
    static Boolean Delete = false;
    static Boolean Edit = false;
    public static  String  sUsername, ssData, sVldentrada,sVldrebuy,sVldaddon,sQtdentrada,sQtdrebuy,sQtdaddon,sImg;
    Button btncanceljogo, btnsavejogo;
    //TextView dateEditText;
    //private EditText editvalorentrada,editvalorrebuy,editvaloraddon;
    public static String datejogo ;
    private  Runnable run;

    private Context mjContext;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpoker);
        searchView = (SearchView) findViewById (R.id.searchjogo);
        imgaddnew = (ImageView) findViewById(R.id.imgaddnewjogo);
        btnRefresh = (ImageView) findViewById(R.id.btnrefresh);

        btnDatePicker = (ImageButton) findViewById (R.id.btn_searchedata);
        btnDatePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               /* final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Poker_main.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if(dayOfMonth < 10){
                                    Dia = "0"+ (dayOfMonth);
                                }else {
                                    Dia = String.valueOf(dayOfMonth);
                                }
                                if(monthOfYear < 10){
                                    Mes = "0"+ (monthOfYear + 1);
                                }else{
                                    Mes = String.valueOf(monthOfYear + 1);
                                }
                                //searchView.setQuery( year + "-" + Mes + "-" + Dia,false);
                                searchidata = String.valueOf(year)+Mes+Dia;

                                new GetDados_jogos_from_date().execute();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                /*
                */
                //closeKeyboard();
                Intent intent = new Intent(Poker_main.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
        imgaddnew.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.v(TAG, " click");
                Intent it5 = new Intent(Poker_main.this, Poker_new.class);
                startActivityForResult(it5,2);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                idjogo = "id";
                descrijogo = "";
                new GetDados_jogos().execute();
            }
        });

/*
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            Origem =(String) b.get("STRING_ORIGEM");
        }else{
            Origem = "ADDITEM";
        }*/
        //idjogo = "0";
        //OcorList = new ArrayList<>();
        new GetDados_jogos().execute();

        lv = (ListView) findViewById(R.id.listviwerjogos);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Intent intent = new Intent(Poker_main.this, Poker.class);
                idjogo = itens.get(position).getId();
                descrijogo = itens.get(position).getDescricao();

                sVldentrada = itens.get(position).getVlentrada();
                sVldrebuy = itens.get(position).getVlrebuy();
                sVldaddon = itens.get(position).getVladdon();

                intent.putExtra("id", idjogo);
                intent.putExtra("Drisc", descrijogo);
                intent.putExtra("ENTRADA", sVldentrada);
                intent.putExtra("REBUY", sVldrebuy);
                intent.putExtra("ADDON", sVldaddon);
                startActivityForResult(intent,2);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idjogo = itens.get(i).getId();
                datejogo = itens.get(i).getData();
                descrijogo = itens.get(i).getDescricao();
                sVldentrada = itens.get(i).getVlentrada();
                sVldrebuy = itens.get(i).getVlrebuy();
                sVldaddon = itens.get(i).getVladdon();
                sQtdentrada = itens.get(i).getQtdfichaentrada();
                sQtdrebuy = itens.get(i).getQtdficharebuy();
                sQtdaddon = itens.get(i).getQtdfichaaddon();
                sData = itens.get(i).getData();
                sImg = itens.get(i).getImage_path();

               // Pozi = itens.get(i).getId();
                myPopupMenu(view);

                return true;
            }
        });

        //newItemlist = new ArrayList<HashMap<String, String>>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                descrijogo = searchView.getQuery().toString();

                new GetDados_jogos_from_search().execute();

                return false;
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.layoutlistjogos);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                idjogo = "0";
                descrijogo = "";
                new GetDados_jogos().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void onClickDATA(View v) {
        //Toast.makeText(this, "Date", Toast.LENGTH_SHORT).show();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Poker_main.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ;
                        if(dayOfMonth < 10){
                            Dia = "0"+ (dayOfMonth);
                        }else {
                            Dia = String.valueOf(dayOfMonth);
                        }
                        if(monthOfYear < 10){
                            Mes = "0"+ (monthOfYear + 1);
                        }else{
                            Mes = String.valueOf(monthOfYear + 1);
                        }
                        sData = String.valueOf(year)+Mes+Dia;
                        //new Poker_main.GetDados_jogos_from_date().execute();
                        String ds1  = String.valueOf(year)+"-"+Mes+"-"+Dia;

                        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            datejogo = sdf2.format(sdf1.parse(ds1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                         TextView dateEditText = (TextView) findViewById(R.id.editTextDate);
                        dateEditText.setText(datejogo);  ;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void myPopupMenu(View v) {

        PopupMenu popupMenu = new PopupMenu(Poker_main.this, v);

        /*  The below code in try catch is responsible to display icons*/
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.getMenu().findItem(R.id.delete).setTitle("Excluir Jogo");
        popupMenu.getMenu().findItem(R.id.edite).setTitle("Editar Jogo");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast. Maketext (mainactivity. This, "clicked" + item. Gettitle(), toast. Length_short). Show();

                switch (item.getItemId()) {
                    case R.id.delete:
                        //Toast.makeText(mContext, "clicked delete" + idplayer + " "+idjogo, Toast.LENGTH_SHORT).show();
                        showAlertDelete(Poker_main.this, "Deletar", "Deletando Jogo");
                        //new DeletePlayer().execute();
                        return true;
                    case R.id.edite:
                        //showAlert(Poker_main.this, "Editar", "Editando Jogo");
                        FragmentTransaction ft = ((FragmentActivity)Poker_main.this).getSupportFragmentManager().beginTransaction();
                        MyDialogFragment_Jogo df = new MyDialogFragment_Jogo();
                        df.show(ft,"dialog");
                        //Toast.makeText(mContext, "clicked edite" + idplayer, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return true;
                }
            }
        });
        //Show menu
        popupMenu.show();
    }
    public void showAlert(Context context, String title, String message){


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.activity_newpoker, null);

        final TextView tvTitle = (TextView) promptView.findViewById(R.id.tvTitle);
        final TextView editTextDate = (TextView) promptView.findViewById(R.id.editTextDate);

        String ds1  = datejogo;

        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            datejogo = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editTextDate.setText(datejogo);
        final EditText editvalorentrada = (EditText) promptView.findViewById(R.id.edvalorentrada);
        final EditText editvalorrebuy = (EditText) promptView.findViewById(R.id.edvalorrebuy);
        final EditText editvaloraddon = (EditText) promptView.findViewById(R.id.edvaloraddon);
        final EditText editqtdentrada = (EditText) promptView.findViewById(R.id.edqtdentrada);
        final EditText editqtdrebuy = (EditText) promptView.findViewById(R.id.edqtdrebuy);
        final EditText editqtdaddon = (EditText) promptView.findViewById(R.id.edqtdaddon);
        final TextView textViewNumJogo = (TextView) promptView.findViewById(R.id.textViewNumJogo);


        editvalorentrada.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvalorentrada));
        editvalorrebuy.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvalorrebuy));
        editvaloraddon.addTextChangedListener(new Poker_main.MoneyTextWatcher(editvaloraddon));

        //editqtdentrada.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdentrada));
        editqtdentrada.addTextChangedListener(MaskEditUtil.mask(editqtdentrada, MaskEditUtil.FORMAT_MILHAR));
        //editqtdrebuy.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdrebuy));
        editqtdrebuy.addTextChangedListener(MaskEditUtil.mask(editqtdrebuy, MaskEditUtil.FORMAT_MILHAR));
        //editqtdaddon.addTextChangedListener(new Poker_main.MilharTextWatcher(editqtdaddon));
        editqtdaddon.addTextChangedListener(MaskEditUtil.mask(editqtdaddon, MaskEditUtil.FORMAT_MILHAR));


        editvalorentrada.setText(sVldentrada);
        editvalorrebuy.setText(sVldrebuy);
        editvaloraddon.setText(sVldaddon);
        editqtdentrada.setText(sQtdentrada);
        editqtdrebuy.setText(sQtdrebuy);
        editqtdaddon.setText(sQtdaddon);

        textViewNumJogo.setText(idjogo);
        tvTitle.setText("Editar Jogo");

       /* if (title.equals("Deletar")){

        }*/

        //if (title.equals("Editar")){

        final CircleImageView img = promptView.findViewById(R.id.imgjogo);
        if(sImg.equals("0")){
            Picasso.with(this).load("http://futsexta.16mb.com/Poker/imgjogo/default.png").into(img);
        }else{
            if(Poker.reload){
                Picasso.with(this).load(sImg).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);

            }else{
                Picasso.with(this).load(sImg).into(img);
            }
        }
        final CircleImageView imgPhoto = promptView.findViewById(R.id.imgfhoto);
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Poker_main.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Poker_main.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Toast.makeText(Poker_main.this, "sem permissão", Toast.LENGTH_SHORT).show();
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
           // img.setImageResource(R.mipmap.usercirclegear128);

            //final TextView tvaction = promptView.findViewById(R.id.tvactionplayer);

            final EditText ednome = promptView.findViewById(R.id.namejogo);
            ednome.setText(descrijogo);
            ednome.setSelection(ednome.getText().length());
            ednome.setEnabled(true);


            final ImageButton btndata = promptView.findViewById(R.id.btn_date);

            btndata.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    //showDatePickerDialog();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Poker_main.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    if(dayOfMonth < 10){
                                        Dia = "0"+ (dayOfMonth);
                                    }else {
                                        Dia = String.valueOf(dayOfMonth);
                                    }
                                    if(monthOfYear < 10){
                                        Mes = "0"+ (monthOfYear + 1);
                                    }else{
                                        Mes = String.valueOf(monthOfYear + 1);
                                    }
                                    sData = String.valueOf(year)+Mes+Dia;
                                    //new Poker_main.GetDados_jogos_from_date().execute();
                                    String ds1  = String.valueOf(year)+"-"+Mes+"-"+Dia;

                                    SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                                    try {
                                        datejogo = sdf2.format(sdf1.parse(ds1));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    final TextView editTextDate = (TextView) promptView.findViewById(R.id.editTextDate);
                                    editTextDate.setText(datejogo);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            });

            Edit = true;
        //}



        alert.setView(promptView);
        alert.setCancelable(false);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Delete = false;
                Edit = false;
                closeKeyboard();
            }
        });

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                closeKeyboard();
            }

        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean dt = false, nm = false, vle = false,vlr = false,vla = false,qtde = false,qtdr = false,qtda = false;
                final TextView dateEditText = (TextView) promptView.findViewById(R.id.editTextDate);
                final ImageButton btndataa = (ImageButton) promptView.findViewById(R.id.btn_date);

                ssData = dateEditText.getText().toString();
                if (ssData.matches("")) {
                    Toast.makeText(Poker_main.this, "Favor Selecionar a data do Jogo", Toast.LENGTH_SHORT).show();
                    btndataa.performClick();
                    dt = false;
                }else{

                    dt = true;
                }

                final AutoCompleteTextView usernameEditText = (AutoCompleteTextView) promptView.findViewById(R.id.namejogo);
                sUsername = usernameEditText.getText().toString();
                if (sUsername.matches("")) {
                    Toast.makeText(Poker_main.this, "Favor Preencher o nome do Jogo", Toast.LENGTH_SHORT).show();
                    usernameEditText.requestFocus();
                    nm = false;
                }else{
                    nm = true;
                }
                final TextView Vldentrada = (TextView) promptView.findViewById(R.id.edvalorentrada);
                sVldentrada = Vldentrada.getText().toString();

                String replaceable = String.format("[%s\\s]", getCurrencySymbol());
                sVldentrada = sVldentrada.replaceAll(replaceable, "");
                sVldentrada = sVldentrada.replaceAll(",", "");

                if ( (sVldentrada.matches("")) || (sVldentrada.matches("000") )) {
                    Toast.makeText(Poker_main.this, "Favor preencher o valor da Entrada", Toast.LENGTH_SHORT).show();
                    Vldentrada.requestFocus();//dialog.dismiss();
                    vle = false;
                }else{
                    vle = true;
                }
                final EditText Qtdentrada = (EditText) promptView.findViewById(R.id.edqtdentrada);
                sQtdentrada = Qtdentrada.getText().toString();
                sQtdentrada = MaskEditUtil.unmask(Qtdentrada.getText().toString());
                if ( (sQtdentrada.matches("")) || (sQtdentrada.matches("0") )) {
                    //dialog.dismiss();
                    Toast.makeText(Poker_main.this, "Favor preencher a quantidade de ficha da Entrada", Toast.LENGTH_SHORT).show();
                    Qtdentrada.requestFocus();
                    qtde = false;
                }else{
                    qtde = true;
                }

                final TextView Vldrebuy = (TextView) promptView.findViewById(R.id.edvalorrebuy);
                sVldrebuy = Vldrebuy.getText().toString();
                sVldrebuy = sVldrebuy.replaceAll(replaceable, "");
                sVldrebuy = sVldrebuy.replaceAll(",", "");
                if ((sVldrebuy.matches("")) || (sVldrebuy.matches("000")) ) {
                    Toast.makeText(Poker_main.this, "Favor preencher o valor da Rebuy", Toast.LENGTH_SHORT).show();
                    Vldrebuy.requestFocus();
                    vlr = false;
                }else{
                    vlr = true;
                }
                final TextView Qtdrebuy = (TextView) promptView.findViewById(R.id.edqtdrebuy);
                sQtdrebuy = Qtdrebuy.getText().toString();
                sQtdrebuy = MaskEditUtil.unmask(Qtdrebuy.getText().toString());
                if ( (sQtdrebuy.matches("")) || (sQtdrebuy.matches("0") )) {
                    Toast.makeText(Poker_main.this, "Favor preencher a quantidade de ficha do Rebuy", Toast.LENGTH_SHORT).show();
                    Qtdrebuy.requestFocus();
                    qtdr = false;
                }else{
                    qtdr = true;
                }

                final TextView Vldaddon = (TextView) promptView.findViewById(R.id.edvaloraddon);
                sVldaddon = Vldaddon.getText().toString();
                sVldaddon = sVldaddon.replaceAll(replaceable, "");
                sVldaddon = sVldaddon.replaceAll(",", "");
                if ((sVldaddon.matches("")) || (sVldaddon.matches("000")) ) {
                    Toast.makeText(Poker_main.this, "Favor preencher o valor da Addon", Toast.LENGTH_SHORT).show();
                    Vldaddon.requestFocus();
                    vla = false;
                }else{
                    vla = true;
                }

                final TextView Qtdaddon = (TextView) promptView.findViewById(R.id.edqtdaddon);
                sQtdaddon = Qtdaddon.getText().toString();
                sQtdaddon = MaskEditUtil.unmask(Qtdaddon.getText().toString());
                if ( (sQtdaddon.matches("")) || (sQtdaddon.matches("0") )) {
                    Toast.makeText(Poker_main.this, "Favor preencher a quantidade de ficha do Addon", Toast.LENGTH_SHORT).show();
                    Qtdaddon.requestFocus();
                    qtda = false;
                }else{
                    qtda = true;
                }

                if((dt) && (nm) && (vle)&& (vlr)&& (vla)&& (qtde)&& (qtdr)&& (qtda)){
                    new UpdatetJogo().execute();
                    adapterListView.notifyDataSetChanged();
                    dialog.dismiss();

                    //View vieww = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        //inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                closeKeyboard();
                //InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(dialog.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog.dismiss();
                new GetDados_jogos().execute();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                closeKeyboard();
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
    public void showAlertDelete(Context context, String title, String message){


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.custom_alertnewplayer, null);



        //if (title.equals("Deletar")){
        CircleImageView img = (CircleImageView) promptView.findViewById(R.id.imgaddplayers);
        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((Poker) getActivity()).turnOffFrag();
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(Poker_main.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) Poker_main.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Toast.makeText(Poker_main.this, "sem permissão", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });*/

        if(sImg.equals("0")){
            Picasso.with(Poker_main.this).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(img);
        }else{
            if(Poker.reload){
                Picasso.with(Poker_main.this).load(sImg).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);

            }else{
                Picasso.with(Poker_main.this).load(sImg).into(img);
            }


            //Picasso.with(getContext()).load(data.get(pos).getFeed_thumb_image()).memoryPolicy(MemoryPolicy.NO_CACHE).into(img);
        }
            //final ImageView img = promptView.findViewById(R.id.imgaddplayers);
            //img.setImageResource(R.mipmap.usercircledelete);

        final TextView nameview = promptView.findViewById(R.id.tvname);
        nameview.setText(descrijogo);

        final TextView tvaction = promptView.findViewById(R.id.tvactionplayer);
        tvaction.setVisibility(View.VISIBLE);
        tvaction.setText("Excluir Jogo?");



        final TextView txnome = promptView.findViewById(R.id.txnomep);
        txnome.setVisibility(View.GONE);
        final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
        //ednome.setText(sUsername);
        //ednome.setEnabled(false);
        //ednome.setFocusable(false);
        ednome.setVisibility(View.GONE);




            //final TextView tvaction = promptView.findViewById(R.id.tvname);
            //tvaction.setText("Excluir Jogo");

            //final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
            //ednome.setText(descrijogo);
            //ednome.setEnabled(false);
            //ednome.setFocusable(false);
            Delete = true;


       // }

       /* if (title.equals("Editar")){

            final ImageView img = promptView.findViewById(R.id.imgaddplayers);
            img.setImageResource(R.mipmap.usercirclegear128);

            final TextView tvaction = promptView.findViewById(R.id.tvactionplayer);
            tvaction.setText("Editando Player");



            final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
            ednome.setText(sUsername);
            ednome.setSelection(ednome.getText().length());
            ednome.setEnabled(true);

            Edit = true;
        }*/

        alert.setView(promptView);
        alert.setCancelable(false);
        //Poker.this.setFinishOnTouchOutside(false);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                Delete = false;
                //Edit = false;
                Toast.makeText(context, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
                new GetDados_jogos().execute();

            }
        });

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Delete){

                    new DeleteJogo().execute();

                    //itens.remove(Pozi);
                    //notifyItemRemoved(Pozi);
                    Toast.makeText(context, "Jogo Excluido", Toast.LENGTH_SHORT).show();
                    Delete = false;
                }
                if (Edit){
                    final EditText ednome = promptView.findViewById(R.id.ednomePlayer);
                    sUsername = String.valueOf(ednome.getText());

                    //new customAdapter.UpdatePlayer().execute();

                    ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ednome.getWindowToken(), 0);
                    Edit = false;
                }


            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @SuppressLint("ValidFragment")
    private static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }

    }
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
    public int getAdapterItemPosition(long id){

        for (int position = 0; position < PecaFragment.lsvendas.size(); position++)
            if (Integer.parseInt(PecaFragment.lsvendas.get(position).getIdprod()) == id)
                return position;
        return -1;
    }
    public int getAdapterItemPositiondel(int id) {

        for (int position = 0; position < PecaFragment.lsvendas.size(); position++){
            if (Integer.parseInt(PecaFragment.lsvendas.get(position).getIdprod()) == id)
            {
                //String sdtd = PecaFragment.lsvendas.get(position).getQtdparcel();
                // if (sdtd.equals("2")){
                //if (Integer.parseInt(sdtd) > 1){

                  //  return 0;
                //}
                return -1;
            }
        }
        return -1;

    }
    class GetDados_jogos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker_main.this);
            pDialog.setMessage("Buscando Jogos...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("idjogo",idjogo));
            params.add(new BasicNameValuePair("descri",descrijogo));
            params.add(new BasicNameValuePair("searchedata",searchidata));

            JSONObject json = new JSONObject();
             json = jsonParser.makeHttpRequest(urlAll,"POST",
                    params);

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    itens = new ArrayList<ItemListViewPoker>();
                    //newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String id  = object.getString("id");
                        String Descricao = object.getString("Descricao");
                        String Data = object.getString("Data");
                        String vlentrada = object.getString("vlentrada");
                        String qtdfichaentrada = object.getString("qtdfichaentrada");
                        String vlrebuy = object.getString("vlrebuy");
                        String qtdficharebuy = object.getString("qtdficharebuy");
                        String vladdon = object.getString("vladdon");
                        String qtdfichaaddon = object.getString("qtdfichaaddon");
                        String ttplayers = object.getString("ttplayers");
                        String img = object.getString("image_path");

                        ItemListViewPoker item1 = new ItemListViewPoker(id,Descricao, Data, vlentrada,qtdfichaentrada,vlrebuy,qtdficharebuy,vladdon,qtdfichaaddon,ttplayers,img);
                        itens.add(item1);
                        descrijogo = object.getString("Descricao");
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Jogos não encontrado." ,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else{
                Toast.makeText(getApplicationContext(),
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG)
                        .show();
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

            }else {
                adapterListView = new AdapterListViewPoker(Poker_main.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);
            }
            closeKeyboard();
        }
    }
    class GetDados_jogos_from_search extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker_main.this);
            pDialog.setMessage("Buscando Jogos...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("idjogo",idjogo));
            params.add(new BasicNameValuePair("descri",descrijogo));
            params.add(new BasicNameValuePair("searchedata",searchidata));

            JSONObject json = new JSONObject();
            json = jsonParser.makeHttpRequest(urlAll,"POST",
                    params);

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    itens = new ArrayList<ItemListViewPoker>();
                    //newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String id  = object.getString("id");
                        String Descricao = object.getString("Descricao");
                        String Data = object.getString("Data");
                        String vlentrada = object.getString("vlentrada");
                        String qtdfichaentrada = object.getString("qtdfichaentrada");
                        String vlrebuy = object.getString("vlrebuy");
                        String qtdficharebuy = object.getString("qtdficharebuy");
                        String vladdon = object.getString("vladdon");
                        String qtdfichaaddon = object.getString("qtdfichaaddon");
                        String ttplayers = object.getString("ttplayers");
                        String img = object.getString("image_path");

                        ItemListViewPoker item1 = new ItemListViewPoker(id,Descricao, Data, vlentrada,qtdfichaentrada,vlrebuy,qtdficharebuy,vladdon,qtdfichaaddon,ttplayers,img);
                        itens.add(item1);
                        descrijogo = object.getString("Descricao");
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                            "Jogos não encontrado." ,
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else{
                Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                        .show();
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

            }else {
                adapterListView = new AdapterListViewPoker(Poker_main.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);
            }

        }
    }
    class GetDados_jogos_from_date extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker_main.this);
            pDialog.setMessage("Buscando Jogos...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("idjogo",idjogo));
            //params.add(new BasicNameValuePair("descri",descrijogo));
            params.add(new BasicNameValuePair("searchedata",searchidata));

            JSONObject json = new JSONObject();
            json = jsonParser.makeHttpRequest(urlGetJogoFordate,"POST",
                    params);

            if (json != null) {
                int success;
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    itens = new ArrayList<ItemListViewPoker>();
                    //newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String id  = object.getString("id");
                        String Descricao = object.getString("Descricao");
                        String Data = object.getString("Data");
                        String vlentrada = object.getString("vlentrada");
                        String qtdfichaentrada = object.getString("qtdfichaentrada");
                        String vlrebuy = object.getString("vlrebuy");
                        String qtdficharebuy = object.getString("qtdficharebuy");
                        String vladdon = object.getString("vladdon");
                        String qtdfichaaddon = object.getString("qtdfichaaddon");
                        String ttplayers = object.getString("ttplayers");
                        String img = object.getString("image_path");

                        ItemListViewPoker item1 = new ItemListViewPoker(id,Descricao, Data, vlentrada,qtdfichaentrada,vlrebuy,qtdficharebuy,vladdon,qtdfichaaddon,ttplayers,img);
                        itens.add(item1);
                        descrijogo = object.getString("Descricao");
                    }


                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("successo!", json.toString());
                        return json.getString(TAG_MESSAGE);

                    } else {
                        Log.d("Jogo não Atualizado", json.getString(TAG_MESSAGE));
                        return json.getString(TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                        .show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

                if(result.equals("Jogo Não Localizado")){
                    //LoadPhotos();
                    // if(bitmap != null){

                    //   ImageUploadToServerFunctionNewGame();
                    // }

                    Toast.makeText(Poker_main.this, result, Toast.LENGTH_LONG).show();
                };
            }
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json.isEmpty()) {

            }else {
                adapterListView = new AdapterListViewPoker(Poker_main.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);
            }

        }
    }
    class UpdatetJogo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker_main.this);
            pDialog.setMessage("Atualizando Jogo");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", idjogo));
                params.add(new BasicNameValuePair("Descricao", sUsername));
                params.add(new BasicNameValuePair("Data", sData));
                params.add(new BasicNameValuePair("vlentrada", sVldentrada));
                params.add(new BasicNameValuePair("qtdfichaentrada", sQtdentrada));
                params.add(new BasicNameValuePair("vlrebuy", sVldrebuy));
                params.add(new BasicNameValuePair("qtdficharebuy", sQtdrebuy));
                params.add(new BasicNameValuePair("vladdon", sVldaddon));
                params.add(new BasicNameValuePair("qtdfichaaddon", sQtdaddon));

                JSONObject json = jsonParser.makeHttpRequest(URLUPJOGO, "POST",
                        params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("successo!", json.toString());
                    return json.getString(TAG_MESSAGE);

                } else {
                    Log.d("Jogo não Atualizado", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Poker_main.this, file_url, Toast.LENGTH_LONG).show();
            }
            new Poker_main.GetDados_jogos().execute();
        }


    }
    public class DeleteJogo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {

            int success;
            try {

                List params = new ArrayList();

                //params.add(new BasicNameValuePair("id", idplayer));
                params.add(new BasicNameValuePair("idjogo", idjogo));

                JSONObject newjson = jsonParser.makeHttpRequest(URLDELETE, "POST",
                        params);

                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Jogo Deletado com successo!", newjson.toString());
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Jogo Não Deletado", newjson.getString(TAG_MESSAGE));
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            idjogo = "0";
            descrijogo = "";
            new GetDados_jogos().execute();
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
            DecimalFormat df = new DecimalFormat("0.00");
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
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
        }
    }
    public static class MilharTextWatcher implements TextWatcher {
        private WeakReference<EditText> editTextWeakReference;
        private final Locale locale = Locale.getDefault();

        public MilharTextWatcher(EditText editText) {
            this.editTextWeakReference = new WeakReference<>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            editText.removeTextChangedListener(this);

            String formatted = editable.toString();
            DecimalFormat df = new DecimalFormat("0.000");
            String dx = df.format(formatted);

            //DecimalFormat  parsed = new DecimalFormat(editable.toString());
            //String formatted;
            //formatted = NumberFormat.getIntegerInstance(dx);

            //Remove o símbolo da moeda e espaçamento pra evitar bug
            //String replaceable = String.format("[%s\\s]", getCurrencySymbol());
            //String cleanString = formatted.replaceAll(replaceable, "");

            //editText.setText(cleanString);
            editText.setText(dx);
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
            DecimalFormat df = new DecimalFormat("0.###");
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
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
        }
    }
    public static class MaskEditUtil {

        public static final String FORMAT_CPF = "###.###.###-##";
        public static final String FORMAT_FONE = "(###)####-#####";
        public static final String FORMAT_CEP = "#####-###";
        public static final String FORMAT_DATE = "##/##/####";
        public static final String FORMAT_HOUR = "##:##";
        public static final String FORMAT_MILHAR = "##.###";

        /**
         * Método que deve ser chamado para realizar a formatação
         *
         * @param ediTxt
         * @param mask
         * @return
         */
        public static TextWatcher mask(final EditText ediTxt, final String mask) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                @Override
                public void afterTextChanged(final Editable s) {}

                @Override
                public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

                @Override
                public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                    final String str = MaskEditUtil.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (final char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (final Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }
            };
        }

        public static String unmask(final String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
        }
    }

    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

            if (adapterListView != null) {
                adapterListView.notifyDataSetChanged();
            }

            lv.setAdapter(adapterListView);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //Bitmap
                    bitmap = bundle.getParcelable("data");
                    CircleImageView imageView = (CircleImageView) findViewById(R.id.imgjogo);
                    ImageView imageView1 = (ImageView) findViewById(R.id.imgaddnewjogo);

                    imageView1.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if(resultCode == 2){
            //Intent intent= data;
            final String result = data.getStringExtra("ID");
            //Bundle b = data.getExtras();

            //if(b!=null)
            //{
             idjogo = result;
            descrijogo = "0";//b.getString("ID");
            //}else{
                //idjogo = "0";
            //}
            new GetDados_jogos().execute();
        }
    }
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        //if (view != null) {

            // now assign the system
            // service to InputMethodManagerapp:srcCompat="@mipmap/usercircle128"
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
       // }
    }
    @SuppressLint("ResourceAsColor")
    public void LoadPhoto(View view) {



    }
    public void turnOffFrag(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyDialogFragment_Jogo df = (MyDialogFragment_Jogo) getSupportFragmentManager().findFragmentByTag("dialog");
        if(df !=  null ){
            //ImageView img = (ImageView) df.getView().findViewById(R.id.imgaddplayers);
            //img.setImageBitmap(photo);
            //new GetDados().execute();
            //idjogo = "0";
            //descrijogo = "";
            df.dismiss();
            ft.remove(df);

        }
        new GetDados_jogos().execute();
        //new Poker_main().GetTotais().execute();
    }
}
