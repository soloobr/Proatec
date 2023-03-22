package com.example.luiseduardo.infrafacil.ui.main;

import android.app.ProgressDialog;
//import android.arch.lifecycle.Observer;
//import android.arch.lifecycle.ViewModelProviders;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.luiseduardo.infrafacil.JSONParser;
import com.example.luiseduardo.infrafacil.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private final Locale locale = Locale.getDefault();
    static View v;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://futsexta.16mb.com/Poker/Infra_Get_volores.php";
    String ntotalfatu, ntotalmo,ntotalpc;

    private TextView txvalorfaturado, totalpc, totalmo;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    public static String dataini, datafinal;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        new GetValores().execute();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_financas, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);
        final TextView textView =  v.findViewById(R.id.txvalorfaturado);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // txvalor.setText(s);
            }
        });
        return v;
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

                        ntotalfatu = stotalfatu;
                        ntotalmo = stotalmo;
                        ntotalpc = stotalpc;

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

            txvalorfaturado = (TextView) v.findViewById(R.id.txvalorfaturado);
            totalmo = (TextView) v.findViewById(R.id.txvalorfaturadoliqd);
            totalpc = (TextView) v.findViewById(R.id.txvalorfaturadopc);

            int fat=Integer.parseInt(ntotalfatu);
            int pc=Integer.parseInt(ntotalpc);
            int lq=Integer.parseInt(ntotalmo);
            DecimalFormat decFormat = new DecimalFormat("'R$ ' ,##0,00");
            txvalorfaturado.setText(decFormat.format(fat));
            totalmo.setText(decFormat.format(lq));
            totalpc.setText(decFormat.format(pc));
        }
    }


}
