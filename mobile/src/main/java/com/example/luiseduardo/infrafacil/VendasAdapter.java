    package com.example.luiseduardo.infrafacil;

    import android.annotation.SuppressLint;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.icu.text.NumberFormat;
    import android.os.AsyncTask;
    import androidx.annotation.NonNull;
    //import android.support.v7.widget.RecyclerView;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;

    import static com.example.luiseduardo.infrafacil.PecaFragment.lsvendas;
    import static com.example.luiseduardo.infrafacil.PecaFragment.v;

    //import androidx.recyclerview.widget.RecyclerView;

    public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.MyViwerHolder> {

        Context mContext;
        List<Vendas> mData;
        public static String idocor = Status_Ordem.IDORDEM;
        public static String  idvenda, idprod, idforne, valorvenda, valorpg, qtdprodvend, descri;
        private static String urldelvenda = "http://futsexta.16mb.com/Poker/Infra_Delete_produtosvendido.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        public static String TAG = Ordem.class.getSimpleName();
        JSONParser jsonParser = new JSONParser();
        private final Locale locale = Locale.getDefault();

        public VendasAdapter(Context mContext, List<Vendas> mData) {
            this.mContext = mContext;
            this.mData = lsvendas;
        }


        public static void notifyItemChanged() {
        }

        @Override
        public MyViwerHolder onCreateViewHolder(ViewGroup parent, int viwType) {
            //mData = Status_Ordem.
            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_vendas,parent,false);
            MyViwerHolder vHolder = new MyViwerHolder(v);
            return vHolder;
        }
        public static String getCurrencySymbol() {
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol();
        }
        @Override
        public void onBindViewHolder(final MyViwerHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.tv_nome.setText(mData.get(position).getDescricao());
            holder.idvenda = (mData.get(position).getIdvenda());
            holder.idproduto = (mData.get(position).getIdprod());
            holder.idocor = (mData.get(position).getIdocor());

            //holder.checkBox.setSelected(mData.get(position).isSelected());
            holder.checkBox.setChecked(mData.get(position).isSelected());
            //if(!mData.get(position).getQtdparcel().equals("0")) {
    //            holder.imgparce.setImageResource(R.mipmap.parce100);
            //}

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Excluir item:");
                    builder.setMessage(holder.tv_nome.getText());
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            idvenda = (String) holder.idvenda;
                            //idvenda = PecaFragment.lsvendas.get(counter).getIdvenda();
                            idprod = (String) holder.idproduto;

                            String sVlr_venda = String.valueOf(holder.tv_valor.getText());
                            String sVlr_vendaold = String.format("[%s\\s]", Status_Ordem.MoneyTextWatcher.getCurrencySymbol());
                            sVlr_venda = sVlr_venda.replaceAll(sVlr_vendaold, "");
                            sVlr_venda = sVlr_venda.replaceAll("[.]", "");
                            sVlr_venda = sVlr_venda.replaceAll("[,]", "");
                            int iVlr_venda = (int)Double.parseDouble(sVlr_venda);

                            new ExcluiDadosVenda().execute();
                            removeAt(position);
                            Status_Ordem.somatValue.SubitraiVendas(iVlr_venda);
                            int number1 = (int)Double.parseDouble(Status_Ordem.Vlr_Peca);
                            int number2 = (int)Double.parseDouble(Status_Ordem.Vlr_MO);
                           // int number3 = (int)Double.parseDouble(Vlr_venda);

                           // int res = number1 - number3;
                           // Status_Ordem.editValorpca.setText(String.valueOf(res));
                           // int res1 = res + number2;

                            //Status_Ordem.editValorTotal.setText(String.valueOf(res1));

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
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mData.get(position).setSelected(isChecked);
                    String item = mData.get(position).getDescricao();
                    Toast.makeText(mContext, "FEZ"+ item, Toast.LENGTH_SHORT).show();
                }
            });

        }



        @Override
        public int getItemCount() {
            return mData.size();
        }
        public void removeAt(int position) {
            lsvendas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lsvendas.size());
        }
        @SuppressLint("NotifyDataSetChanged")
        public void selectAllItems(boolean selected, VendasAdapter adapter) {
            for (Vendas venda : mData) {
                venda.setSelected(selected);
                //CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkBoxitens);
                //checkBox.setChecked(selected);
            }
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setSelected(selected);
            }
            notifyDataSetChanged();
        }


        public class MyViwerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView tv_nome;
            private TextView tv_qtd;
            private TextView tv_valor;
            private ImageButton img;
            private ImageButton imgparce;
            private String idvenda;
            private String idproduto;
            private String idocor;

            private CheckBox checkBox;
            private CheckBox checkBoxAll;



            public MyViwerHolder(@NonNull View itemView) {
                super(itemView);

                tv_nome = (TextView) itemView.findViewById(R.id.main_line_nome);
    //            tv_qtd = (TextView)itemView.findViewById(R.id.main_line_qtd);
                tv_valor = (TextView)itemView.findViewById(R.id.main_line_valor);
                img = (ImageButton) itemView.findViewById(R.id.main_delete);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkBoxitens);
                //imgparce = (ImageButton) itemView.findViewById(R.id.main_parce);


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
                 params.add(new BasicNameValuePair("idvenda",idvenda));
                 params.add(new BasicNameValuePair("idproduto",idprod));

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
                VendasAdapter.notifyItemChanged();
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

    }
