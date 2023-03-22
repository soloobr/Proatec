package com.example.luiseduardo.infrafacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class AdapterListViewHistoricoCli extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;

    public AdapterListViewHistoricoCli(Context context, ArrayList<ItemListView> itens)
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
        //Pega o item de acordo com a posção.
        ItemListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.list_itemhistorico, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.

        ((TextView) view.findViewById(R.id.tvdescri)).setText(item.getName());
        ((TextView) view.findViewById(R.id.numero)).setText(item.getId());
        //((TextView) view.findViewById(R.id.lchamado)).setText("Nº Produto:");

        ((ImageView) view.findViewById(R.id._imagem_ocor)).setImageResource(item.getIconeRid());

        return view;
    }
}
