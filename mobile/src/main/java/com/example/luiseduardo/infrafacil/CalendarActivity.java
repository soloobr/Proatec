package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.JSONParser.json;
import static com.example.luiseduardo.infrafacil.Poker.lsplayer;
import static com.example.luiseduardo.infrafacil.Poker.recyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Mateusz Kornakiewicz on 26.05.2017.
 */

public class CalendarActivity extends AppCompatActivity implements ItemClickListener {

    private static String urlJogos = "http://futsexta.16mb.com/Poker/poker_get_datas_jogo.php";
    private static String urlGetJogoFordate = "http://futsexta.16mb.com/Poker/poker_get_jogo_date.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private AdapterListViewPoker adapterListView;
    private ListView lv;
    ArrayList<JogosListView> itens = new ArrayList<>();
    public static List<JogosListView> lsjogo;

    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    List<EventDay> events = new ArrayList<>();

    public static RecyclerView recyclerView;
    public static customAdapter_Poker  mAdapter;
    public static List<PlayersListView> lsplayer;

    public static String Origem, idjogo,descrijogo,qtdplayers;
    public static  String  sUsername, ssData, sVldentrada,sVldrebuy,sVldaddon,sQtdentrada,sQtdrebuy,sQtdaddon,sImg;
    public String  searchidata = "0";

    private String TAG = CalendarActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        /*
        lv = (ListView) findViewById(R.id.listviwerjogosdate);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Intent intent = new Intent(CalendarActivity.this, Poker.class);
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
        });*/

        new GetDados_jogos().execute();
        /*
        List<EventDay> events = new ArrayList<>();
*/

    }

    private List<Calendar> getDisabledDays() {
        Calendar firstDisabled = DateUtils.getCalendar();
        firstDisabled.add(Calendar.DAY_OF_MONTH, 2);

        Calendar secondDisabled = DateUtils.getCalendar();
        secondDisabled.add(Calendar.DAY_OF_MONTH, 1);

        Calendar thirdDisabled = DateUtils.getCalendar();
        thirdDisabled.add(Calendar.DAY_OF_MONTH, 18);

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(firstDisabled);
        calendars.add(secondDisabled);
        calendars.add(thirdDisabled);
        return calendars;
    }

    private Calendar getRandomCalendar() {
        Random random = new Random();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, random.nextInt(99));

        return calendar;
    }

    @Override
    public void onClick(View view, int position) {

    }

    class GetDados_jogos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            List params = new ArrayList();
            params.add(new BasicNameValuePair("idjogo","0"));


            JSONObject json = new JSONObject();
            json = jsonParser.makeHttpRequest(urlJogos,"POST",
                    params);

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("jogo");

                    events = new ArrayList<>();
                    //newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String id  = object.getString("id");
                        String Descricao = object.getString("Descricao");
                        String Data = object.getString("Data");

                        String quantidade_dias = object.getString("quantidade_dias");

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.add(Calendar.DAY_OF_MONTH, Integer.parseInt(quantidade_dias));
                        events.add(new EventDay(calendar1, R.drawable.chip));


                        //descrijogo = object.getString("Descricao");
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

            if (json.isEmpty()) {

            }else {
                Calendar calendar = Calendar.getInstance();
                events.add(new EventDay(calendar, com.example.luiseduardo.infrafacil.DrawableUtils.getCircleDrawableWithText(CalendarActivity.this, "H")));
/*
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, -10);
        events.add(new EventDay(calendar1, R.drawable.chip));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 10);
        events.add(new EventDay(calendar2, R.drawable.sample_icon_3, Color.parseColor("#228B22")));

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_MONTH, 7);
        events.add(new EventDay(calendar3, R.drawable.sample_four_icons));

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_MONTH, 13);
        events.add(new EventDay(calendar4, DrawableUtils.getThreeDots(this)));
*/
                CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

                Calendar min = Calendar.getInstance();
                min.add(Calendar.MONTH, -2);

                Calendar max = Calendar.getInstance();
                max.add(Calendar.MONTH, 2);

                calendarView.setMinimumDate(min);
                calendarView.setMaximumDate(max);

                calendarView.setEvents(events);

                //calendarView.setDisabledDays(getDisabledDays());

                calendarView.setOnDayClickListener(eventDay ->{

                    //Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String strDate = sdf.format(eventDay.getCalendar().getTime());
                    searchidata = strDate;
                    new  GetDados_jogos_from_date().execute(); }
                        /*Toast.makeText(getApplicationContext(),
                                eventDay.getCalendar().getTime().toString() + " "
                                        + eventDay.isEnabled() ,
                                Toast.LENGTH_SHORT).show()*/


                );
/*
                Button setDateButton = (Button) findViewById(R.id.setDateButton);
                setDateButton.setOnClickListener(v -> {
                    try {
                        Calendar randomCalendar = getRandomCalendar();
                        String text = randomCalendar.getTime().toString();
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        calendarView.setDate(randomCalendar);
                    } catch (OutOfDateRangeException exception) {
                        exception.printStackTrace();

                        Toast.makeText(getApplicationContext(),
                                "Date is out of range",
                                Toast.LENGTH_LONG).show();
                    }
                });
                */
            }


        }
    }

    class GetDados_jogos_from_date extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                    lsjogo = new ArrayList<JogosListView>();
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

                        JogosListView item1 = new JogosListView(id,Descricao, Data, vlentrada,qtdfichaentrada,vlrebuy,qtdficharebuy,vladdon,qtdfichaaddon,ttplayers,img);
                        lsjogo.add(item1);
                        descrijogo = object.getString("Descricao");
                    }


                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("successo!", json.toString());
                        return json.getString(TAG_MESSAGE);

                    } else {
                        Log.d("Jogo não Encontrado", json.getString(TAG_MESSAGE));
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

                    Toast.makeText(CalendarActivity.this, "Não foi encontrado jogos nesta data!", Toast.LENGTH_LONG).show();
                };
            }

            if (json.isEmpty()) {

            }else {

                int spanCount = 1;
                recyclerView = (RecyclerView) findViewById(R.id.recycleitemjogosdate);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CalendarActivity.this, spanCount);
                //--RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new customAdapter_Poker(lsjogo, R.layout.list_item_jogospoker, CalendarActivity.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.setClickListener(CalendarActivity.this);


/*
                adapterListView = new AdapterListViewPoker(CalendarActivity.this, itens);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);*/
            }

        }
    }
}