package com.example.luiseduardo.infrafacil;

//import static com.example.luiseduardo.infrafacil.Poker.itens;
import static com.example.luiseduardo.infrafacil.Poker.lsplayer;
import static com.example.luiseduardo.infrafacil.Poker.myrecyclerview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import static com.example.luiseduardo.infrafacil.Poker;

public class AdapterListViewPlayers extends RecyclerView.Adapter<AdapterListViewPlayers.MyViwerHolder> implements ListAdapter {

    Context mContext;
    List<PlayersListView> mData;
    public static String idocor = Status_Ordem.IDORDEM;
    public static String  idplayer,rebuy, addon, valor;
    private static String urldelvenda = "http://futsexta.16mb.com/Poker/Infra_Delete_produtosvendido.php";
    private static String URLUP = "http://futsexta.16mb.com/Poker/Poker_Edit_Players.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static String TAG = Ordem.class.getSimpleName();
    JSONParser jsonParser = new JSONParser();
    private final Locale locale = Locale.getDefault();
    private ProgressDialog pDialog;

    public AdapterListViewPlayers(Context mContext, List<PlayersListView> mData) {
        this.mContext = mContext;
        this.mData = lsplayer;
    }


    public static void notifyItemChanged() {
    }

    @Override
    public MyViwerHolder onCreateViewHolder(ViewGroup parent, int viwType) {
        //mData = Status_Ordem.
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_players,parent,false);
        MyViwerHolder vHolder = new MyViwerHolder(v);
        return vHolder;
    }
    public static String getCurrencySymbol() {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
    }
    @Override
    public void onBindViewHolder(final MyViwerHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_idplayer.setText(mData.get(position).getId());
        holder.tv_nome.setText(mData.get(position).getNome());
        holder.tv_qtdrebuy.setText(mData.get(position).getRebuy());
        holder.tv_qtdaddon.setText(mData.get(position).getAddon());

        valor = mData.get(position).getValor();

        //BigDecimal parsed = parseToBigDecimal(valor);
        //String formatted;
        //formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
        holder.tv_valortotal.setText(valor);

        //holder.tv_valor.setText(mData.get(position).getValoruni());

        //holder.idvenda = (mData.get(position).getIdvenda());
       // holder.idproduto = (mData.get(position).getIdprod());
        //holder.idocor = (mData.get(position).getIdocor());
      //  if(!mData.get(position).getQtdparcel().equals("0")) {
    //            holder.imgparce.setImageResource(R.mipmap.parce100);
        //}

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Adicionar  Rebuy:");
                builder.setMessage(holder.tv_nome.getText());
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //idvenda = (String) holder.idvenda;
                        rebuy = String.valueOf(holder.tv_qtdrebuy.getText());

                        valor = String.valueOf(holder.tv_valortotal.getText());
                        //addon = (String) holder.tv_qtdaddon.getText();
                        //String Vlr_venda = String.valueOf(holder.tv_valor.getText());
                        //String Vlr_venda = String.valueOf(holder.tv_valor.getText());
                        //new ExcluiDadosVenda().execute();
                        //removeAt(position);
                        int number1 = (int)Double.parseDouble(rebuy);
                        //int number2 = (int)Double.parseDouble(addon);
                        int number3 = (int)Double.parseDouble(valor);

                        int res = number1 + 1;
                        int restota = res * 20;
                        int restotal = restota + number3;
                        // Status_Ordem.editValorpca.setText(String.valueOf(res));
                        // int res1 = res + number2;

                        //holder.tv_qtdrebuy.setText(String.valueOf(res));
                        //holder.tv_valortotal.setText(String.valueOf(restotal));


                        idplayer = String.valueOf(holder.tv_idplayer.getText());
                        rebuy = String.valueOf(res);
                        addon = String.valueOf(holder.tv_qtdaddon.getText());
                        valor = String.valueOf(restotal);

                        new UpdatePlayer().execute();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                //cria o AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
        holder.imgaddon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Adicionar  Addon:");
                builder.setMessage(holder.tv_nome.getText());
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //idvenda = (String) holder.idvenda;
                        rebuy = String.valueOf(holder.tv_qtdrebuy.getText());
                        addon = String.valueOf(holder.tv_qtdaddon.getText());



                        //addon = (String) holder.tv_qtdaddon.getText();
                        //String Vlr_venda = String.valueOf(holder.tv_valor.getText());
                        //String Vlr_venda = String.valueOf(holder.tv_valor.getText());
                        //new ExcluiDadosVenda().execute();
                        //removeAt(position);
                        int number2 = (int)Double.parseDouble(rebuy);
                        int number1 = (int)Double.parseDouble(addon);
                        int number3 = (int)Double.parseDouble(valor);

                        int res1 = number1 +1;
                        int res = number2 * 20;
                        int restotal = res + number3;
                        // Status_Ordem.editValorpca.setText(String.valueOf(res));
                        // int res1 = res + number2;

                        holder.tv_qtdaddon.setText(String.valueOf(res1));
                        holder.tv_valortotal.setText(String.valueOf(restotal));

                        idplayer = String.valueOf(holder.tv_idplayer.getText());
                        //rebuy = String.valueOf(res);
                        addon = String.valueOf(res);
                        valor = String.valueOf(holder.tv_valortotal.getText());


                        new UpdatePlayer().execute();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                //cria o AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, holder.tv_nome.getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateList(List<PlayersListView> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void removeAt(int position) {
        lsplayer.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lsplayer.size());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public class MyViwerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_idplayer;
        private TextView tv_nome;
        private TextView tv_qtdrebuy;
        private TextView tv_qtdaddon;
        private TextView tv_valortotal;


        private ImageButton img;
        private ImageButton imgaddon;
        private ImageButton imgparce;
        private String idvenda;
        private String idproduto;
        private String idocor;



        public MyViwerHolder(@NonNull View itemView) {
            super(itemView);

            tv_idplayer = (TextView) itemView.findViewById(R.id.idplayer);
            tv_nome = (TextView) itemView.findViewById(R.id.main_line_nome);
            tv_qtdrebuy = (TextView)itemView.findViewById(R.id.main_line_valorrebuy);
            tv_qtdaddon = (TextView)itemView.findViewById(R.id.main_line_valoraddon);
            tv_valortotal = (TextView)itemView.findViewById(R.id.main_line_valortotal);

            //img = (ImageButton) itemView.findViewById(R.id.btnrebuy);
            //imgaddon = (ImageButton) itemView.findViewById(R.id.main_delete);
            //imgparce = (ImageButton) itemView.findViewById(R.id.addrebuy);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Toast.makeText(mContext, tv_nome.getText(), Toast.LENGTH_SHORT).show();
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                //User user = users.get(position);
                // We can access the data within the views
                Toast.makeText(mContext, tv_nome.getText(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    class ExcluiDadosVenda extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            int success;
            try {

                List params = new ArrayList();
                params.add(new BasicNameValuePair("idocorrencia",idocor));
                //params.add(new BasicNameValuePair("idvenda",idvenda));
                //params.add(new BasicNameValuePair("idproduto",idprod));

                JSONObject newjson = jsonParser.makeHttpRequest(urldelvenda,"POST",
                        params);
                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Insert Item successo!", newjson.toString());

                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Item não Atualizado", newjson.getString(TAG_MESSAGE));

                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String file_url) {

            if (file_url != null) {
                //Toast.makeText(Context.this,  file_url, Toast.LENGTH_LONG).show();
            }
            AdapterListViewPlayers.notifyItemChanged();
        }
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

    class UpdatePlayer extends AsyncTask<String, String, String>  {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {

            int success;
            try {

                List params = new ArrayList();

                params.add(new BasicNameValuePair("id", idplayer));
                params.add(new BasicNameValuePair("rebuy", rebuy));
                params.add(new BasicNameValuePair("addon", addon));
                params.add(new BasicNameValuePair("valor", valor));


                Log.d("Debug!", "starting");

                JSONObject newjson = jsonParser.makeHttpRequest(URLUP, "POST",
                        params);

                success = newjson.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Editar successo!", newjson.toString());
                    //finish();
                    return newjson.getString(TAG_MESSAGE);

                } else {
                    Log.d("Não Atualizada", newjson.getString(TAG_MESSAGE));
                    //finish();
                    return newjson.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            //if (pDialog.isShowing()) {
              //  pDialog.dismiss();
            //}
            //if (file_url != null) {
                //Toast.makeText(Poker.this,  file_url, Toast.LENGTH_LONG).show();
            //}
            //Poker.Atu;



        }

    }

    class Soma{

    }

}
