package com.example.luiseduardo.infrafacil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.luiseduardo.infrafacil.JSONParser.json;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Cadastro_Produtos extends AppCompatActivity implements View.OnClickListener{

    public static String idforne, valor, qtdprod, descri;
    private EditText edtquantidade, edtidforne, editvalorvendido, edtdescricaoproduto;
    private  TextView textViewNumcadProd, tvtopdescri;
    private Button btncancelprpd, btnconfirma;
    JSONParser jsonParser = new JSONParser();
    private static String IsertItem = "http://futsexta.16mb.com/Poker/IsertItem_ProdutoMobile.php";
    private static String url = "http://futsexta.16mb.com/Poker/Infra_Get_produto.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private View v;
    private String i, id,descricao,quantidade, valorvenda, fornecedor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitt_cadastro_produtos);

        edtquantidade = (EditText) findViewById(R.id.editquantidadenew);
        edtidforne = (EditText) findViewById(R.id.editidfornecedor);
        editvalorvendido = (EditText) findViewById(R.id.edtvalorproduto);
        edtdescricaoproduto = (EditText) findViewById(R.id.editnomeprod);
        textViewNumcadProd = (TextView) findViewById(R.id.textViewNumcadProd);
        tvtopdescri = (TextView) findViewById(R.id.tvtopdescri);
        btncancelprpd = (Button) findViewById(R.id.btnCancelarprod);
        btnconfirma = (Button) findViewById(R.id.btnSalvarprod);

        editvalorvendido.addTextChangedListener(new Status_Ordem.MoneyTextWatcher(editvalorvendido));

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
             i =(String) b.get("STRING_IDPROD");
            String d =(String) b.get("STRING_DESCRICAO");
            String q =(String) b.get("STRING_QUANTIDADE");
            String f =(String) b.get("STRING_FORNECEDOR");
            String v =(String) b.get("STRING_VALOR");
            //String a =(String) b.get("STRING_ATIVO");
            edtdescricaoproduto.setText(d);
            edtquantidade.setText(q);
            edtidforne.setText(f);
            editvalorvendido.setText(v);

            tvtopdescri.setText("EDITANDO PRODUTO");

            textViewNumcadProd.setText(i);

            new GetProduto().execute();

        }


    }

/*    public void onClickEdit (View v){
        switch (v.getId()) {

            case R.id.btncanceladdprod:
                finish();
                //break;
            case R.id.btnconfirmaaddprod:
                descri = edtdescricaoproduto.getText().toString();
                valor = editvalorvendido.getText().toString();
                qtdprod = edtquantidade.getText().toString();
                idforne = edtidforne.getText().toString();
                new InserirPeca().execute();
                finish();
                //break;
            default:
                break;
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCancelarprod:
                finish();
                break;
            case R.id.btnSalvarprod:
                descri = edtdescricaoproduto.getText().toString();
                valor = editvalorvendido.getText().toString();
                qtdprod = edtquantidade.getText().toString();
                idforne = edtidforne.getText().toString();
                new InserirPeca().execute();
                finish();
                //break;
            default:
                break;
        }
    }


    class InserirPeca extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            String Vlr_Pexaold = String.format("[%s\\s]", MoneyTextWatcher.getCurrencySymbol());
            valor = valor.replaceAll(Vlr_Pexaold, "");
            valor = valor.replaceAll(",", "");

            int success;
            try {
                //String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                List params = new ArrayList();
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("descri", descri));
                params.add(new BasicNameValuePair("valor", valor));
                params.add(new BasicNameValuePair("quantidadeproduto", qtdprod));
                params.add(new BasicNameValuePair("idfornecedor", idforne));



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
                    Log.d("Produto Atualizado", newjson.getString(TAG_MESSAGE));
                    // finish();
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            if (file_url != null) {
                Toast.makeText(Cadastro_Produtos.this,  file_url, Toast.LENGTH_LONG).show();
            }
        }

    }

    class GetProduto extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Cadastro_Produtos.this);
            pDialog.setMessage("Buscando Produto...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {



            List params = new ArrayList();
            params.add(new BasicNameValuePair("idproduto",i));

            JSONObject json = jsonParser.makeHttpRequest(url,"GET",
                    params);

            //Log.e(TAG, "Response from : " + json);

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("produto");

                    for (int i = 0; i < eventDetails.length(); i++) {
                        JSONObject c = eventDetails.getJSONObject(i);


                        id = c.getString("Id");
                        descricao = c.getString("descricao");
                        quantidade = c.getString("quantidade");
                        valorvenda = c.getString("valor");
                        fornecedor = c.getString("idfornecedor");



                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {

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


            }else{
                edtdescricaoproduto.setText(descricao);
                edtquantidade.setText(quantidade);
                edtidforne.setText(fornecedor);
                editvalorvendido.setText(valorvenda);
            }
        }

    }

}
