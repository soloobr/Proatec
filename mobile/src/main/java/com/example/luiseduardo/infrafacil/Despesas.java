package com.example.luiseduardo.infrafacil;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.luiseduardo.infrafacil.JSONParser.json;

import androidx.appcompat.app.AppCompatActivity;

public class Despesas extends AppCompatActivity {

    private ListView lv;
    ArrayList<ItemListViewDespesas> itenss = new ArrayList<>();
     JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    private AdapterListViewDespesas adapterListView;
    private String datainicial, datafinal,vltotal, stoqtd;
    private TextView valortotal, txdatainicial, txdatafinal, txqtddisp;

    private static String urlAll = "http://festabrinka.com.br/Infra_Get_Dispesas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        lv = (ListView) findViewById(R.id.listviwerdisp);
        valortotal = (TextView) findViewById(R.id.tvdetalhedisp);
        txdatainicial = (TextView) findViewById(R.id.txdatainidisp);
        txdatafinal = (TextView) findViewById(R.id.txdatafinaldisp);
        txqtddisp = (TextView) findViewById(R.id.tvnumerodisp);

        datainicial = Financas.dataini;
        datafinal = Financas.datafinal;
        vltotal = Financas.totaldisp;
        stoqtd = Financas.ntoquantidade;

        txdatainicial.setText(datainicial);
        txdatafinal.setText(datafinal);
        txqtddisp.setText(stoqtd);

        new GetDespesas().execute();
    }

    class GetDespesas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            List params = new ArrayList();
            params.add(new BasicNameValuePair("datainicial",datainicial));
            params.add(new BasicNameValuePair("datafinal",datafinal));

            JSONObject json = jsonParser.makeHttpRequest(urlAll,"POST",
                    params);

            //Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("dispesas");

                    itenss = new ArrayList<ItemListViewDespesas>();
                    //newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String descri = object.getString("descricao");
                        String nome = object.getString("nome");
                        String qtd = object.getString("quantidade");
                        String valorvendido = object.getString("valorvendido");
                        String valortotal = object.getString("valortotal");
                        String datavenda = object.getString("datavenda");
                        String idocorrencia = object.getString("idocorrencia");
                        String formadepagamento = object.getString("formadepagamento");
                        String valorparcela = object.getString("valorparcela");

                        if(Integer.parseInt(valorparcela) > 0 ) {
                            valortotal = valorparcela;
                        }

                        ItemListViewDespesas item = new ItemListViewDespesas( descri,  nome,  qtd,  valorvendido,  valortotal,  datavenda, idocorrencia );
                        itenss.add(item);


                    }

                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Despesa não encontrado." ,
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
                adapterListView = new AdapterListViewDespesas(Despesas.this, itenss);
                lv.setAdapter(adapterListView);
                lv.setCacheColorHint(Color.TRANSPARENT);
                valortotal.setText(vltotal);
            }

        }
    }

}