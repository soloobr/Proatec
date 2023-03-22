package com.example.luiseduardo.infrafacil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Clientes extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static RecyclerView myrecyclerview;
    private ProgressDialog pDialog;
    private static String urlAll = "http://futsexta.16mb.com/Proatec/Infra_Get_clientes.php";
    private static String url = "http://futsexta.16mb.com/Poker/ordem_servicomobile.php";
    ArrayList<HashMap<String, String>> OcorList;
    private ClientesAdapter adapterListView;
    //private ArrayList<ItemListView> itens;
    public static ArrayList itens = null;
    //public static ArrayList<ItemListViewVendas> itens;

    //public static ArrayList<ItemListViewVendas> itens;
    ArrayList<HashMap<String, String>> newItemlist = new ArrayList<HashMap<String, String>>();
    private ListView lv;
    private String TAG = Produtos.class.getSimpleName();
    private String idvenda,idprod, idforne, valorvenda, qtdprodvend, descri;
    public String Nomecli = "%";
    SearchView searchView;
    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    private static String IsertItem = "http://futsexta.16mb.com/Poker/IsertItem_OrdemMobile.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ImageView imgaddnewcli;
    public static String idcliente, strchbinativo, status;
    private static CheckBox chbinativo;


    private View v;
    public ClientesAdapter clientesAdapterAdapter;
    public static List<ItemListViewClientes> lsvendas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        searchView = (SearchView) findViewById (R.id.searchcli);
        imgaddnewcli = (ImageView) findViewById(R.id.imgaddnewcli);


        imgaddnewcli.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.v(TAG, " click");
                Intent it5 = new Intent(Clientes.this, Cadastro_Clientes.class);
                startActivity(it5);
            }
        });

        chbinativo = (CheckBox) findViewById(R.id.chbinativo);



        OcorList = new ArrayList<>();

        myrecyclerview = (RecyclerView) findViewById(R.id.listviwercli);

        new Clientes.GetDadosClientes().execute();

  /*      myrecyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {


                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_alertprod, null);
                final EditText qtd = alertLayout.findViewById(R.id.edtquantidade);
                final EditText idfornecedor = alertLayout.findViewById(R.id.edtidforne);
                final EditText valordevenda = alertLayout.findViewById(R.id.editvalorvendido);
                valordevenda.addTextChangedListener(new Status_Ordem.MoneyTextWatcher(valordevenda));

                final TextView tvdescriproduto = alertLayout.findViewById(R.id.tvdescriproduto);
                final TextView txnumeroid = alertLayout.findViewById(R.id.txnumeroid);

                tvdescriproduto.setText(descri);
                txnumeroid.setText(idprod);
                qtd.setText("1");
                idfornecedor.setText("1");


                AlertDialog.Builder alert = new AlertDialog.Builder(Clientes.this);
                alert.setTitle("Adicionar item");
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Adição cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idforne = idfornecedor.getText().toString();
                        valorvenda = valordevenda.getText().toString();
                        String Vlr_Pexaold = String.format("[%s\\s]", Status_Ordem.MoneyTextWatcher.getCurrencySymbol());
                        valorvenda = valorvenda.replaceAll(Vlr_Pexaold, "");
                        valorvenda = valorvenda.replaceAll(",", "");

                        qtdprodvend = qtd.getText().toString();
                        PecaFragment.lsvendas.add(new Vendas( idvenda,descri, qtdprodvend , valorvenda,idforne,idprod));
                        VendasAdapter.notifyItemChanged();
                        finish();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });*/
        //lv.setOnItemClickListener(this);


        newItemlist = new ArrayList<HashMap<String, String>>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                OcorList.clear();
                //adapterListView.notifyDataSetChanged();
                Nomecli = searchView.getQuery().toString();

                new Clientes.GetDadosClientes().execute();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
    class GetDadosClientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (chbinativo.isChecked()){
                strchbinativo = "True";
            }else{
                strchbinativo = null;
            }


            if (Nomecli != null) {
                Nomecli=Nomecli;
                Log.i("Profile : ", "Not Null  "+Nomecli);
            } else{
                Log.i("Profile : ", "Iss Null  "+Nomecli);
                Nomecli = "%";
            }

            List params = new ArrayList();
            params.add(new BasicNameValuePair("Nomecli",Nomecli));
            params.add(new BasicNameValuePair("flagstatus",strchbinativo));


            JSONObject json = jsonParser.makeHttpRequest(urlAll,"POST",
                    params);

            //Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("clientes");

                    itens = new ArrayList<ItemListViewClientes>();
                    newItemlist.clear();

                    for (int i = 0; i < eventDetails.length(); i++)
                    {
                        object = eventDetails.getJSONObject(i);
                        String id = object.getString("Id");
                        String nome = object.getString("Nome");
                        String email = object.getString("Email");
                        String funcionario = object.getString("Funcionario");
                        String telefone = object.getString("Telefone");
                        String statuss = object.getString("Status");

                        idcliente = id;
                        status = statuss;

                        ItemListViewClientes item1 = new ItemListViewClientes( id,  nome,  email,  telefone, funcionario, statuss );
                        itens.add(item1);


                    }

                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Cliente não encontrado." ,
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


            ClientesAdapter.notifyItemChanged();

            myrecyclerview = (RecyclerView) findViewById(R.id.listviwercli);
            ClientesAdapter clientesAdapter  = new ClientesAdapter(Clientes.this,itens);
            myrecyclerview.setLayoutManager(new LinearLayoutManager(Clientes.this));
            myrecyclerview.setAdapter(clientesAdapter);


        }
    }




    @Override
    protected void onStart() {
        super.onStart();

        new Clientes.GetDadosClientes().execute();

    }
}
