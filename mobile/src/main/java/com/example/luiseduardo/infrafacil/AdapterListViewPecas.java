package com.example.luiseduardo.infrafacil;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class AdapterListViewPecas extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListViewPecas> itens;
    private final Locale locale = Locale.getDefault();

    public AdapterListViewPecas(Context context, ArrayList<ItemListViewPecas> itens)
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
    public ItemListViewPecas getItem(int position)
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
        ItemListViewPecas item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.list_item_pecas_pecas, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.tvnome)).setText(item.getDescricao());
        ((TextView) view.findViewById(R.id.tvqtdpeca)).setText(item.getSerie());
        ((TextView) view.findViewById(R.id.tvnumero)).setText(item.getId());

       // String valor = item.getValor();
        //BigDecimal parsed = parseToBigDecimal(valor);
        String formatted;
        //formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

        //((TextView) view.findViewById(R.id.tvvalorvenda)).setText(formatted);
        //((TextView) view.findViewById(R.id.tvvalorvenda)).setText(format.format(item.getValor()));
        //((TextView) view.findViewById(R.id.lchamado)).setText("Nº Produto:");

        ((ImageView) view.findViewById(R.id._imagem_prod)).setImageResource(item.getIconeRid());

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
