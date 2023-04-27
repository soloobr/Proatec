package com.example.luiseduardo.infrafacil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.luiseduardo.infrafacil.Clientes.itens;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.MyViwerHolder> {

    Context mContext;
    List<ItemListViewClientes> mData;
    private static String urdesativa = "http://futsexta.16mb.com/Proatec/Infra_Desativa_cliente.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static String idselectec = "";
    public ClientesAdapter(Context mContext, List<ItemListViewClientes> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    public static void notifyItemChanged() {
    }

    @Override
    public MyViwerHolder onCreateViewHolder(ViewGroup parent, int viwType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_clientes,parent,false);
        MyViwerHolder vHolder = new MyViwerHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViwerHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_nome.setText(mData.get(position).getNome());
        holder.ativo = (mData.get(position).getStatus());
        String statuss = (mData.get(position).getStatus());
        if(statuss.equals("Inativo")) {
            holder.tv_nome.setTextColor(Color.RED);
        }

        holder.tv_id.setText(mData.get(position).getId());
        holder.fone = (mData.get(position).getTelefone());
        holder.mail = (mData.get(position).getEmail());



        //holder.img.setImageResource(mData.get(position).ge());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DesativaCliente().execute();
                Toast.makeText(mContext, "O Cliente: "+holder.tv_nome.getText()+" Foi Desativado", Toast.LENGTH_SHORT).show();
                idselectec = String.valueOf(holder.tv_id.getText());
                removeAt(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Cadastro_Clientes.class);
                String strName = null;

                i.putExtra("STRING_ID", holder.tv_id.getText());
                i.putExtra("STRING_NOME", holder.tv_nome.getText());
                i.putExtra("STRING_FONE", holder.fone);
                i.putExtra("STRING_MAIL", holder.mail);
                i.putExtra("STRING_STATUS","EDITANDO DADOS PROFESSOR");
                i.putExtra("STRING_ATIVO",holder.ativo);
                mContext.startActivity(i);
                //Toast.makeText(mContext, holder.tv_id.getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    private void removeAt(int position) {
        itens.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itens.size());
    }

    public class MyViwerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_nome;
        private TextView tv_id;
        private ImageButton img;
        private String fone;
        private String mail;
        private String ativo;





        public MyViwerHolder(@NonNull View itemViewClientes) {
            super(itemViewClientes);

            tv_nome = (TextView) itemViewClientes.findViewById(R.id.main_line_nomecli);
            tv_id = (TextView) itemViewClientes.findViewById(R.id.main_line_idcli);
            img = (ImageButton) itemViewClientes.findViewById(R.id.main_deletecli);

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

     class DesativaCliente extends AsyncTask<String, String, String> {

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected String doInBackground(String... strings) {
             int success;
             try {
            //String idcliente = Clientes.idcliente;

             List params = new ArrayList();
             params.add(new BasicNameValuePair("Idcliente",idselectec));


             JSONObject newjson = jsonParser.makeHttpRequest(urdesativa,"POST",
                     params);
                 success = newjson.getInt(TAG_SUCCESS);
                 if (success == 1) {
                     Log.d("Professor desativado!", newjson.toString());

                     return newjson.getString(TAG_MESSAGE);

                 } else {
                     Log.d("Professor não encontrado", newjson.getString(TAG_MESSAGE));

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
            ClientesAdapter.notifyItemChanged();
        }
    }

}
