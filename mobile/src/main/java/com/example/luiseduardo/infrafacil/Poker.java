package com.example.luiseduardo.infrafacil;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Poker extends FragmentActivity implements ItemClickListener{

    private AdapterListViewPlayers adapterListViewPlayers;
    private static String urlplayers = "http://futsexta.16mb.com/Poker/poker_get_playersjogo.php";
    private static String URTOTAIS = "http://futsexta.16mb.com/Poker/Poker_Get_Totais.php";
    private static String urladdplayers = "http://futsexta.16mb.com/Poker/Poker_insert_Players_Jogo.php";



    public static List<PlayersListView> lsplayer;
    //ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    private ListView lv;
    private ProgressDialog pDialog;
    public static RecyclerView myrecyclerview;

    public static RecyclerView recyclerView;
    public static customAdapter  mAdapter;
    //ArrayList<PlayersListView> list = new ArrayList<>();
    private List<PlayersListView> cities;

    private boolean PLAYERS = false;

    ImageButton btnRebuy;

    static View v;

    JSONParser jsonParserR = new JSONParser();
    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    private final Locale locale = Locale.getDefault();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static String  idjogo,idplayer, rebuy, addon,descricao,sUsername;
    public static String  valor, vlrebuy,vladdon,vlentrada,total,totalrebuy,totaladdon,totalplayers;

    public static TextView vlrentrada, vlrrebuy, vlraddon,vltotaljogo,ttrebuy,ttaddon,ttplayers,primeiro,segundo,terceiro,noplayers,driscrijogo;

    public static Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
    public static boolean reload = false;

    private static final int CAMERA_REQUEST = 1888;

    private OnIntentReceived mIntentListener;

    private WeakReference<CircleImageView> imageViewReference;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_poker);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("id");
            String D = (String) b.get("Drisc");
            String E = (String) b.get("ENTRADA");
            String RE = (String) b.get("REBUY");
            String A = (String) b.get("ADDON");

            idjogo = j;
            descricao = D;



            vlrentrada = (TextView) findViewById(R.id.tvvalorentrada);
            vlrrebuy = (TextView) findViewById(R.id.tvvlrebuy);
            vlraddon = (TextView) findViewById(R.id.tvvladdon);

            BigDecimal parsedent = parseToBigDecimal(E);
            String formattedent;
            formattedent = NumberFormat.getCurrencyInstance(locale).format(parsedent);
            vlrentrada.setText(formattedent);

            BigDecimal parsedreb = parseToBigDecimal(RE);
            String formattedreb;
            formattedreb = NumberFormat.getCurrencyInstance(locale).format(parsedreb);
            vlrrebuy.setText(formattedreb);

            BigDecimal parsedadd = parseToBigDecimal(A);
            String formattedadd;
            formattedadd = NumberFormat.getCurrencyInstance(locale).format(parsedadd);
            vlraddon.setText(formattedadd);


        }else{
            idjogo = "0";

        }

        lsplayer = new ArrayList<>();

        new GetDados().execute();

        vltotaljogo = (TextView) findViewById(R.id.tvvalortotaljogo);
        ttplayers = (TextView) findViewById(R.id.tqtdent);
        ttrebuy = (TextView) findViewById(R.id.tqtdrebuys);
        ttaddon = (TextView) findViewById(R.id.tqtdaddon);
        noplayers = (TextView) findViewById(R.id.tvsemplayers);



        primeiro = (TextView) findViewById(R.id.vlprimeiro);
        segundo = (TextView) findViewById(R.id.vlsegundo);
        terceiro = (TextView) findViewById(R.id.vlterceiro);
        driscrijogo = (TextView) findViewById(R.id.tvdescrijogo);
        driscrijogo.setText(descricao);

        new GetTotais().execute();

    }

    @Override
    public void onClick(View view, int position) {
        //mAdapter.notifyDataSetChanged();
        final PlayersListView city = lsplayer.get(position);
        //btnRebuy = (ImageButton) findViewById(R.id.bntaddon);
        btnRebuy.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
        //list.add("Mammahe");
          mAdapter.notifyDataSetChanged();
              Toast.makeText(getApplicationContext(),
                      "Atualizou " + city.getNome(),
                      Toast.LENGTH_LONG)
                      .show();
        }
        });

        //btnRebuy = (ImageButton) findViewById(R.id.bntaddon);
        Toast.makeText(getApplicationContext(),
                "Atualizou " + view.getId(),
                Toast.LENGTH_LONG)
                .show();
    }
    @SuppressLint("ResourceAsColor")
    public void onClickNewPlayer(View v) {
        //openDialogFragment(v);
        AlertDialog.Builder alert = new AlertDialog.Builder(Poker.this);
        LayoutInflater layoutInflater = LayoutInflater.from(Poker.this);
        View promptView = layoutInflater.inflate(R.layout.custom_alertnewplayer, null);

        final EditText ednome = promptView.findViewById(R.id.ednomePlayer);

        final ImageView img = promptView.findViewById(R.id.imgaddplayers);
        img.setImageResource(R.mipmap.usercircle128);

        alert.setView(promptView);
        alert.setCancelable(false);
        //Poker.this.setFinishOnTouchOutside(false);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                 Toast.makeText(Poker.this, "Adição cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Poker.sUsername = String.valueOf(ednome.getText());

                new InsertPlayer().execute();

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();
    }
    public static void methodOnBtnClick(int position){

    }
    class GetTotais extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String  doInBackground(String... args) {


            List params = new ArrayList();
            params.add(new BasicNameValuePair("id",idjogo));

            JSONObject json = jsonParserR.makeHttpRequest(URTOTAIS,"POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String tt = object.getString("total");
                        String ttr = object.getString("totalrebuy");
                        String tta = object.getString("totaladdon");
                        String ttp = object.getString("totalplayers");


                        /*int tr = (int)Double.parseDouble(ttr);
                        int ta = (int)Double.parseDouble(tta);
                        int tp = (int)Double.parseDouble(ttp);
                        int vt = (ent * vent);
                        String tt = String.valueOf(vt);*/

                        total = tt;
                        totalplayers = ttp;
                        totalrebuy = ttr;
                        totaladdon = tta;

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
            if (PLAYERS) {
                BigDecimal parsed = parseToBigDecimal(total);
                String formatted;
                formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

                Poker.vltotaljogo.setText(formatted);
                ttplayers.setText(totalplayers);
                ttrebuy.setText(totalrebuy);
                ttaddon.setText(totaladdon);

                Premiacao();
            }else{
                total ="0";
                BigDecimal parsed = parseToBigDecimal(total);
                String formatted;
                formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

                Poker.vltotaljogo.setText(formatted);
                //ttplayers.setText(totalplayers);
                //ttrebuy.setText(totalrebuy);
                //ttaddon.setText(totaladdon);

                Premiacao();
            }

        }
    }
    public  void Premiacao() {

        int vl = (int)Double.parseDouble(total);

        int pri = (vl / 100) * 50;
        int seg = (vl / 100) * 30;
        int ter = (vl / 100) * 20;

        BigDecimal prim = parseToBigDecimal(String.valueOf(pri));
        String sprimeiro;
        sprimeiro = NumberFormat.getCurrencyInstance(locale).format(prim);
                primeiro.setText(sprimeiro);

        BigDecimal segu = parseToBigDecimal(String.valueOf(seg));
        String ssegundoo;
        ssegundoo = NumberFormat.getCurrencyInstance(locale).format(segu);
                segundo.setText(String.valueOf(ssegundoo));

        BigDecimal terc = parseToBigDecimal(String.valueOf(ter));
        String sterceiro;
        sterceiro = NumberFormat.getCurrencyInstance(locale).format(terc);
                terceiro.setText(String.valueOf(sterceiro));
    }
    public class GetDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker.this);
            pDialog.setMessage("Buscando Jogadores...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            //String jsonStr = sh.makeServiceCall(url);
            int success = 0;
            List params = new ArrayList();
            params.add(new BasicNameValuePair("id",idjogo));

            JSONObject jsonStrT = jsonParserR.makeHttpRequest(urlplayers,"POST",
                    params);

            try {
                success = jsonStrT.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (success == 1) {
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(jsonStrT));

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("jogo");

                    lsplayer = new ArrayList<PlayersListView>();

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String idjogo1 = c.getString("idjogo");
                        String id = c.getString("id");
                        String name = c.getString("Nome_Player");
                        String rebuy = c.getString("rebuy");
                        String addon = c.getString("addon");
                        //String valor = c.getString("Valor");
                        String vlentrada = c.getString("vlentrada");
                        String vlrebuy = c.getString("vlrebuy");
                        String vladdon = c.getString("vladdon");
                        String img = c.getString("image_path");

                        int ent = 1;
                        int vent = (int)Double.parseDouble(vlentrada);
                        int vtent = (ent * vent);

                        int reb = (int)Double.parseDouble(rebuy);
                        int vreb = (int)Double.parseDouble(vlrebuy);
                        int vtreb = (reb * vreb);

                        int add = (int)Double.parseDouble(addon);
                        int vadd = (int)Double.parseDouble(vladdon);
                        int vtadd = (add * vadd);

                        int total = (vtent+vtreb+vtadd);
                        String valor = String.valueOf(total);


                        if (valor != null) {
                            PLAYERS = true;
                            lsplayer.add(new PlayersListView(id, idjogo1, name,  rebuy,  addon, valor,vlentrada,vlrebuy,vladdon,1,img));
                        }

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
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (PLAYERS) {
                int spanCount = 2;
                recyclerView = (RecyclerView) findViewById(R.id.listviwerplayers);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(Poker.this, spanCount);
                //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new customAdapter(lsplayer, R.layout.item_players, Poker.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.setClickListener(Poker.this);
                noplayers.setVisibility(View.GONE);
                new GetTotais().execute();
            }else{
                noplayers.setVisibility(View.VISIBLE);
            }


        }
    }
    class InsertPlayer extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Poker.this);
            pDialog.setMessage("Adicionando Jogador");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {

            int success;
            try {
                List params = new ArrayList();

                //params.add(new BasicNameValuePair("id", Data_Local));
                params.add(new BasicNameValuePair("idjogo", idjogo));
                params.add(new BasicNameValuePair("nomeplayer", sUsername));

                JSONObject json = jsonParser.makeHttpRequest(urladdplayers, "POST",
                        params);


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("successo!", json.toString());
                    //finish();
                    return json.getString(TAG_MESSAGE);

                } else {
                    Log.d("Jogador não  adicionado", json.getString(TAG_MESSAGE));
                    //finish();
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
                new GetDados().execute();
                new GetTotais().execute();
                Toast.makeText(Poker.this, file_url, Toast.LENGTH_LONG).show();
            }
            //new Poker_main.GetDados_jogos();
        }


    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Intent intent=new Intent();
        //intent.putExtra("MESSAGE",message);
        setResult(2,intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {

            try{
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = bundle.getParcelable("data");
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    //ImageView imageView = (ImageView)findViewById(R.id.imageView7);

                    CircleImageView imageView = (CircleImageView) findViewById(R.id.imgaddplayers);
                    //Picasso.with(this).load("http://futsexta.16mb.com/Poker/imgplayer/useredit.png").into(imageView);
                    imageView.setImageBitmap(photo);
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        }

        //if (requestCode == 100 && resultCode == RESULT_OK) {
       // if (requestCode == 100 && data != null) {

            //Toast.makeText(Poker.this, "Camera not supported", Toast.LENGTH_LONG).show();


/*
            selectedImage = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            CircleImageView imageView = (CircleImageView)findViewById(R.id.imgaddplayers);
            //imageView.setImageBitmap(photo);
          //  imageView.setImageResource(R.mipmap.boxout128);*/

       // }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation changes
        outState.putParcelable("file_uri", fileUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)         {
        super.onRestoreInstanceState(savedInstanceState);
// get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
/*
    public static class MyDialogFragment extends DialogFragment {
        static MyDialogFragment newInstance() {
            return new MyDialogFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.custom_alertnewplayer, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView)tv).setText("This is an instance of MyDialogFragment");
            return v;
        }
        void showDialog() {
            // Create the fragment and show it as a dialog.
            DialogFragment newFragment = MyDialogFragment.newInstance();
            newFragment.show(getFragmentManager(), "dialog");
        }
    }*/
    public void openDialogFragment(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyDialogFragment df = new MyDialogFragment();
        df.show(ft,"dialog");
    }
    public void turnOffFrag(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyDialogFragment df = (MyDialogFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if(df !=  null ){
            //ImageView img = (ImageView) df.getView().findViewById(R.id.imgaddplayers);
            //img.setImageBitmap(photo);
            //new GetDados().execute();
        df.dismiss();
        ft.remove(df);

        }
        new GetDados().execute();
        new GetTotais().execute();
    }


}

