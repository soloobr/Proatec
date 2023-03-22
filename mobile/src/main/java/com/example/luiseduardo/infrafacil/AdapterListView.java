package com.example.luiseduardo.infrafacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;

    public AdapterListView(Context context, ArrayList<ItemListView> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public ItemListView getItem(int position)
    {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        ItemListView item = itens.get(position);
        view = mInflater.inflate(R.layout.list_item_ocorrenciasall, null);

        ((TextView) view.findViewById(R.id.nome)).setText(item.getName());
        ((TextView) view.findViewById(R.id.tvdataretirada)).setText(item.getDatasaida());
        ((TextView) view.findViewById(R.id.tvdatasaida)).setText(item.getDataentrada());

        if (item.getDatasaida().toString() == "null" || item.getDatasaida().toString() == "0000-00-00"){
        //meuLinearLayout.setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tvdatasaida)).setText("__/__/____");
        }else {
            ((TextView) view.findViewById(R.id.tvdatasaida)).setText(item.getDatasaida());
        }
        //LinearLayout meuLinearLayout = view.findViewById(R.id.lndev);
        if (item.getDataentrada().toString() == "null" || item.getDataentrada().toString() == "0000-00-00"){
            //meuLinearLayout.setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tvdataretirada)).setText("__/__/____");
        }else {
            ((TextView) view.findViewById(R.id.tvdataretirada)).setText(item.getDataentrada());
        }

        ((TextView) view.findViewById(R.id.numero)).setText(item.getId());
        ((TextView) view.findViewById(R.id.status)).setText(item.getStatus());


        ((ImageView) view.findViewById(R.id._imagem_ocor)).setImageResource(item.getIconeRid());

        return view;
    }
}
