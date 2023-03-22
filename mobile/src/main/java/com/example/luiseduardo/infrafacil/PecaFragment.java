package com.example.luiseduardo.infrafacil;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PecaFragment   extends Fragment {

    static View v;
    public static RecyclerView myrecyclerview;
    public static List<Vendas> lsvendas;
    public static ImageButton btnadd;

    public static ArrayList itens = null;
    private static String IDORDEM = Status_Ordem.IDORDEM;

    JSONParser jsonParser = new JSONParser();
    private static String urlvenda = "http://futsexta.16mb.com/Proatec/Get_produtosemprestado.php";

    public static String idvenda, idprod,  qtd,  idcliente,  idocor, datavenda,  idforne,  valoruni, valorpago, valortotal,  formadepagamento,  status,  qtdparcel, parcela,  valorparcela, datavencparcela,  name;
    public static int Somavebdas, somatotal;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        // Log.i ("Activit", a);
        Log.i ("Activit", context.getClass().getName());
        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    public PecaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (lsvendas != null && lsvendas.isEmpty()) {
       // }else {
            lsvendas = new ArrayList<>();
        //}
        new GetDadosVenda().execute();
    }

    private void createExampleList() {    }

    public  void buildReclierView() {

        myrecyclerview = (RecyclerView) v.findViewById(R.id.recycleitemendas);
        VendasAdapter vendasAdapter  = new VendasAdapter(getContext(),lsvendas);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(vendasAdapter);
    }

    public void insertItem(int position){
        lsvendas.add(new Vendas(idprod,  name,  idocor,  status));
        VendasAdapter.notifyItemChanged();
    }
    public void removetItem(int position){
        lsvendas.remove(position);
        VendasAdapter.notifyItemChanged();
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    v =  inflater.inflate(R.layout.fragment_peca,container,false);
    myrecyclerview = (RecyclerView) v.findViewById(R.id.recycleitemendas);
    VendasAdapter vendasAdapter  = new VendasAdapter(getContext(),lsvendas);
    myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    myrecyclerview.setAdapter(vendasAdapter);

    btnadd = (ImageButton) v.findViewById(R.id.itemadd);
    configureImageButton();


    return v;

    }

    private class GetDadosVenda extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            IDORDEM = Status_Ordem.IDORDEM;
            String NUM_Ocor = IDORDEM;
//            Log.e("Profile IDOCOR: ", IDORDEM);

            List params = new ArrayList();
            params.add(new BasicNameValuePair("IDOcor",NUM_Ocor));

            JSONObject jsonStr = jsonParser.makeHttpRequest(urlvenda,"POST",
                    params);

            Log.i("Profile JSON: ", jsonStr.toString());

            if (jsonStr != null) {
                try {
                    Log.e(TAG, "Não nulo 1");
                    JSONObject jsonObj = new JSONObject(String.valueOf(jsonStr));
                    Log.e(TAG, "JSON : " + jsonStr);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("produtos");
                    Log.e(TAG, "Count : " + contacts.length());

                    if (itens == null){
                        itens = new ArrayList<ItemListViewVendas>();
                    }

                    // Somavebdas = 0;
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        //String idvend = c.getString("idvenda");
                        String idpro = c.getString("Id");
                        String idocor = c.getString("idocorrencia");
                        String sdescricao = c.getString("descricao");
                        String sstatus = c.getString("status");

                        idprod = idpro;
                        status =  sstatus;
                        name =  sdescricao;


                        lsvendas.add(new Vendas(idpro,  sdescricao,  idocor,  sstatus));

                        //Somavebdas = Somavebdas + (int)Double.parseDouble(valortotal);

                        Log.e(TAG, String.valueOf(lsvendas));

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error : " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

           // VendasAdapter.notifyItemChanged();

            myrecyclerview = (RecyclerView) v.findViewById(R.id.recycleitemendas);
            VendasAdapter vendasAdapter  = new VendasAdapter(getContext(),lsvendas);
            myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            myrecyclerview.setAdapter(vendasAdapter);


           //Status_Ordem.editValorpca.setText(String.valueOf(Somavebdas));

            //Status_Ordem.Vlr_Peca = String.valueOf(Somavebdas);
            //Status_Ordem.Vlr_MO = ((EditText)findViewById(R.id.editValormo)).getText().toString();


            //int number1 = (int)Double.parseDouble(Status_Ordem.Vlr_MO);
           // int number2 = (int)Double.parseDouble(String.valueOf(Somavebdas));
            //int res = number1 + number2;

            //Status_Ordem.editValorTotal.setText(String.valueOf(res));

            //Somavebdas=0;

        }
    }


    private void configureImageButton() {
        // TODO Auto-generated method stub
        ImageButton btn = (ImageButton) v.findViewById(R.id.itemadd);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Produtos.class);
                startActivity(intent);
                //Toast.makeText(getActivity(), "You Clicked the button!", Toast.LENGTH_LONG).show();

            }
        });


    }

    public void onStart() {
        super.onStart();
        buildReclierView();
//
        Log.e(TAG, "Iniciou");
    }

    public void onStop() {
        super.onStop();
        Log.e(TAG, "Fechou");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static PecaFragment newInstance(int page, String title) {
        PecaFragment fragmentFirst = new PecaFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
}
