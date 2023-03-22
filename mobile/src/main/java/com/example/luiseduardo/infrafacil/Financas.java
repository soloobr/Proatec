package com.example.luiseduardo.infrafacil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Financas extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://futsexta.16mb.com/InfraFacil/Infra_Get_volores.php";
    private static String urlpc = "http://futsexta.16mb.com/InfraFacil/Infra_Get_volorlucropc.php";
    public String ntotalfatu;
    public String ntotalmo;
    public String ntotalpc;
    public String ntotallucropc;
    public static ProgressDialog pDialog;
   


    private TextView txvalorfaturado, totalpc, totallpc, totalmo,totalliq,detalhafatdisp;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Spinner mySpinneryear, mySpinner;

    public static String totaldisp, dataini, datafinal, year, Month, ntoquantidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financas);

        Locale local = new Locale("pt", "BR");

        mySpinneryear = (Spinner) findViewById(R.id.yearSelector);
        detalhafatdisp = (TextView) findViewById(R.id.tvdetalhafatdisp);
        Calendar calendaryear = Calendar.getInstance(local);
        int thisYear = calendaryear.get(Calendar.YEAR);
        ArrayAdapter<String> adapteryear;
        List<String> listyear;

        listyear = new ArrayList<>();

        for (int i = 2015; i <= thisYear; i++) {
            listyear.add(Integer.toString(i));
        }

        adapteryear = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listyear);
        adapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinneryear.setAdapter(adapteryear);

        Calendar y = Calendar.getInstance(local);
        int thisYear1 = calendaryear.get(Calendar.YEAR);
        String YEAR = Integer.toString(thisYear1);
        mySpinneryear.setSelection (getIndexyear(mySpinneryear, YEAR));


         mySpinner = (Spinner) findViewById(R.id.MONTHSelector);
        Calendar calendar = Calendar.getInstance(local);
        int month = calendar.get(Calendar.MONTH)+1;
        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<>();

        for (int i = 1; i <=12; i++) {
            if (i ==1){
                list.add("Janeiro");
            }
            if (i ==2){
                list.add("Fevereiro");
            }
            if (i ==3){
                list.add("Março");
            }
            if (i ==4){
                list.add("Abril");
            }
            if (i ==5){
                list.add("Maio");
            }
            if (i ==6){
                list.add("Junho");
            }
            if (i ==7){
                list.add("Julho");
            }
            if (i ==8){
                list.add("Agosto");
            }
            if (i ==9){
                list.add("Setembro");
            }
            if (i ==10){
                list.add("Outubro");
            }
            if (i ==11){
                list.add("Novembro");
            }
            if (i ==12){
                list.add("Dezembro");
            }
        }

       /* for(int i = 0; i <= month; i++)
        {
            //list.add(i);
            if (i ==1){
                list.add("janeiro");
             
            }
            if (i ==2){
                list.add("fevereiro");
            }
            if (i ==3){
                list.add("março");
            }
            if (i ==4){
                list.add("abril");
            }
            if (i ==5){
                list.add("maio");
            }
            if (i ==6){
                list.add("junho");
            }
            if (i ==7){
                list.add("julho");
            }
            if (i ==8){
                list.add("agosto");
            }
            if (i ==9){
                list.add("setembro");
            }
            if (i ==10){
                list.add("outubro");
            }
            if (i ==11){
                list.add("novembro");
            }
            if (i ==12){
                list.add("dezembro");
            }
        }*/

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        Calendar c = Calendar.getInstance(local);
       

        String MES = (c.getDisplayName(Calendar.MONTH, Calendar.LONG, local ) );

        mySpinner.setSelection (getIndexMes(mySpinner, MES));

        //selectSpinnerItemByValue(mySpinner, Long.parseLong(MES));

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                String selectedItem = (String) mySpinner.getSelectedItem().toString();
                if(selectedItem.equals("Janeiro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "01";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 1);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();


                }
                if(selectedItem.equals("Fevereiro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "02";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 2);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();



                }
                if(selectedItem.equals("Março"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "03";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 3);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Abril"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "04";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 4);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Maio"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "05";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 5);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Junho"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "06";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 6);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Julho"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "07";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 7);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Agosto"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "08";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 8);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Setembro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "09";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 9);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Outubro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "10";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 10);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Novembro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "11";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 11);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
                if(selectedItem.equals("Dezembro"))
                {
                    //System.out.println("FOI" );
                    //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                    String Month = "12";
                    dataini = currentYear+Month+"01";
                    YearMonth yearMonthObject = YearMonth.of(1999, 12);
                    int daysInMonth = yearMonthObject.lengthOfMonth();
                    datafinal = currentYear+Month+daysInMonth;

                    new GetValores().execute();
                    new GetValorlucropc().execute();

                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mySpinneryear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //System.out.println("FOI" );
                //String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                String currentYear = String.valueOf(mySpinneryear.getSelectedItem());
                 Month =String.valueOf(mySpinner.getSelectedItem());


                if(mySpinner.getSelectedItem().equals("janeiro"))
                {
                     Month = "01";
                }
                if(mySpinner.getSelectedItem().equals("fevereiro"))
                {
                     Month = "02";
                }
                if(mySpinner.getSelectedItem().equals("março"))
                {
                     Month = "03";
                }
                if(mySpinner.getSelectedItem().equals("abril"))
                {
                     Month = "04";
                }
                if(mySpinner.getSelectedItem().equals("maio"))
                {
                     Month = "05";
                }
                if(mySpinner.getSelectedItem().equals("junho"))
                {
                     Month = "06";
                }
                if(mySpinner.getSelectedItem().equals("julho"))
                {
                     Month = "07";
                }
                if(mySpinner.getSelectedItem().equals("agosto"))
                {
                     Month = "08";
                }
                if(mySpinner.getSelectedItem().equals("setembro"))
                {
                     Month = "09";
                }
                if(mySpinner.getSelectedItem().equals("outubro"))
                {
                     Month = "10";
                }
                if(mySpinner.getSelectedItem().equals("novembro"))
                {
                     Month = "11";
                }
                if(mySpinner.getSelectedItem().equals("dezembro"))
                {
                     Month = "12";
                }



                dataini = currentYear+Month+"01";
                YearMonth yearMonthObject = YearMonth.of(1999, 1);
                int daysInMonth = yearMonthObject.lengthOfMonth();
                datafinal = currentYear+Month+daysInMonth;
                //new GetValores().execute();
                //new GetValorlucropc().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            });



    }
    public static void selectSpinnerItemByValue(Spinner spnr, long value) {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItemId(position) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }
    private int getIndexyear(Spinner spinneryear, String myString){

        int index = 0;

        for (int i=0;i<spinneryear.getCount();i++){
            if (spinneryear.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getIndexMes(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if ((spinner.getItemAtPosition(i).toString()).toUpperCase().equals(myString.toUpperCase())){
                index = i;
            }
        }
        return index;
    }


    @Override
    public void onClick(View v) {
        if (v == detalhafatdisp) {

            Intent it1 = new Intent(Financas.this, Despesas.class);
            startActivity(it1);
        }
    }

    public class GetValores extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            if (dataini == null) {

                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

                //String currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

                DateFormat df = new SimpleDateFormat("MM");
                Date d = new Date();
                String Month = df.format(d);

                dataini = currentYear+Month+"01";
            }
            if (datafinal == null) {
                SimpleDateFormat month_date = new SimpleDateFormat("MM");
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                //String currentMonth = String.valueOf(month_date.format(Calendar.getInstance().get(Calendar.MONTH))+1);
                String currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
                String currentmaxday = String.valueOf(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

                DateFormat df = new SimpleDateFormat("MM");
                Date d = new Date();
                String Month = df.format(d);
                datafinal = currentYear+Month+currentmaxday;

            }
            System.out.println(dataini );
            System.out.println(datafinal );
            List params = new ArrayList();
            params.add(new BasicNameValuePair("dataini", dataini));
            params.add(new BasicNameValuePair("datafinal", datafinal));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(json));
                    JSONArray contacts = jsonObj.getJSONArray("valores");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String stotalfatu = c.getString("totalfatu");
                        String stotalmo = c.getString("totalmo");
                        String stotalpc = c.getString("totalpc");
                        String stoqtd = c.getString("quantidade");

                        ntotalfatu = stotalfatu;
                        ntotalmo = stotalmo;
                        //ntotalpc = stotalpc;
                        //ntoquantidade = stoqtd;

                        Log.e("Editar Status!", ntotalfatu);
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

            //if (pDialog.isShowing()) {
            //    pDialog.dismiss();
           // }

            //txvalorfaturado = (TextView) findViewById(R.id.txvalorfaturado);
            //totalpc = (TextView) findViewById(R.id.txvalorfaturadopc);
            //totalmo = (TextView) findViewById(R.id.txvalorfaturadoliqd);



            //if (ntotalfatu == "0" ) {
                //Log.e("Total Faturado null", ntotalfatu);
                //int fat = Integer.parseInt("0");
                //int pc = Integer.parseInt("0");
                //int lq = Integer.parseInt("0");
                //DecimalFormat decFormat = new DecimalFormat("'R$ ' ,##0,00");
                //txvalorfaturado.setText(decFormat.format(fat));
                //totalmo.setText(decFormat.format(lq));
                //totalpc.setText(decFormat.format(pc));

            //}else{
                //Log.e("Total Faturado", ntotalfatu);

                //int fat = Integer.parseInt(ntotalfatu);
                //int pc = Integer.parseInt(ntotalpc);
                //int lq = Integer.parseInt(ntotalmo);
                //int lpc = Integer.parseInt(ntotallucropc);
                //int tlq = lpc + lq;
                //DecimalFormat decFormat = new DecimalFormat("'R$ ' ,##0,00");
                //txvalorfaturado.setText(decFormat.format(fat));
                //totalmo.setText(decFormat.format(lq));
                //totalpc.setText(decFormat.format(pc));
                //totaldisp = decFormat.format(pc);

            //}
        }
    }

    public class GetValorlucropc extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Financas.this);
            pDialog.setMessage("Aguarde Calculando... ");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            if (dataini == null) {

                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

                //String currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

                DateFormat df = new SimpleDateFormat("MM");
                Date d = new Date();
                String Month = df.format(d);

                dataini = currentYear+Month+"01";
            }
            if (datafinal == null) {
                SimpleDateFormat month_date = new SimpleDateFormat("MM");
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                //String currentMonth = String.valueOf(month_date.format(Calendar.getInstance().get(Calendar.MONTH))+1);
                String currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
                String currentmaxday = String.valueOf(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

                DateFormat df = new SimpleDateFormat("MM");
                Date d = new Date();
                String Month = df.format(d);
                datafinal = currentYear+Month+currentmaxday;

            }
            System.out.println(dataini );
            System.out.println(datafinal );
            List params = new ArrayList();
            params.add(new BasicNameValuePair("dataini", dataini));
            params.add(new BasicNameValuePair("datafinal", datafinal));

            JSONObject json = jsonParser.makeHttpRequest(urlpc, "POST",
                    params);

            Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(json));
                    JSONArray contacts = jsonObj.getJSONArray("valores");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String stotallucropc = c.getString("valorlucro");
                        String stotalpc = c.getString("valortotal");

                        ntotallucropc = stotallucropc;
                        ntotalpc = stotalpc;


                        Log.e("Editar Status! Total Lucros Peças:", ntotallucropc + " Total Peça Vendida: " + ntotalpc);
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
            pDialog.dismiss();
            if (TextUtils.isEmpty(ntotalfatu) || ntotalfatu == "0") {
                txvalorfaturado.setText("R$ 0,00");
                totalmo.setText("R$ 0,00");
                totalpc.setText("R$ 0,00");
                totallpc.setText("R$ 0,00");
                totalliq.setText("R$ 0,00");
            }else{
                DecimalFormat decFormat = new DecimalFormat("'R$ ' ,##0,00");
                int fat = Integer.parseInt(ntotalfatu);
                txvalorfaturado = (TextView) findViewById(R.id.txvalorfaturado);
                txvalorfaturado.setText(decFormat.format(fat));

                int pc = Integer.parseInt(ntotalpc);
                totalpc = (TextView) findViewById(R.id.txvalorfaturadopc);
                totalpc.setText(decFormat.format(pc));

                int lupc = Integer.parseInt(ntotallucropc);
                totallpc = (TextView) findViewById(R.id.txvalorlucropc);
                totallpc.setText(decFormat.format(lupc));

                int mo = Integer.parseInt(ntotalmo);
                totalmo = (TextView) findViewById(R.id.txvalormo);
                totalmo.setText(decFormat.format(mo));

                totalliq = (TextView) findViewById(R.id.txvalorfaturadoliqd);

                if (TextUtils.isEmpty(ntotallucropc) || ntotallucropc == "0") {
                    int lpc = Integer.parseInt("0");
                    int lq = Integer.parseInt(ntotalmo);
                    int lucro = lq+lpc;
                    totalliq.setText(decFormat.format(lucro));
                }else{
                    int lpc = Integer.parseInt(ntotallucropc);
                    int lq = Integer.parseInt(ntotalmo);
                    int lucro = lq+lpc;
                    totalliq.setText(decFormat.format(lucro));
                }


            }

        }
    }
}