package com.example.luiseduardo.infrafacil;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.luiseduardo.infrafacil.VendasAdapter.getCurrencySymbol;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class AdapterListViewDespesas extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListViewDespesas> itens;
    private final Locale locale = Locale.getDefault();

    public AdapterListViewDespesas(Context context, ArrayList<ItemListViewDespesas> itens)
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
    public ItemListViewDespesas getItem(int position)
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
        ItemListViewDespesas item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.list_item_despesas, null);

        ((TextView) view.findViewById(R.id.tvnomedisp)).setText(item.getDescri());
        ((TextView) view.findViewById(R.id.tvnomeclidisp)).setText(item.getNome());
        ((TextView) view.findViewById(R.id.tvdatadisp)).setText(item.getDatavenda());
        ((TextView) view.findViewById(R.id.tvqdtpis)).setText(item.getQtd());

        String valor = item.getValorvendido();
        BigDecimal parsed = parseToBigDecimal(valor);
        String formatted;
        formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

        ((TextView) view.findViewById(R.id.tvvalorvendadisp)).setText(formatted);

        String valortotal = item.getValortotal();
        BigDecimal parsedd = parseToBigDecimal(valortotal);
        String formattedd;
        formattedd = NumberFormat.getCurrencyInstance(locale).format(parsedd);

        ((TextView) view.findViewById(R.id.tvvalortotaldisp)).setText(formattedd);


        return view;
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
