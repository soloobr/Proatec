package com.example.luiseduardo.infrafacil;

import static com.example.luiseduardo.infrafacil.MoneyTextWatcher.getCurrencySymbol;
import static com.example.luiseduardo.infrafacil.Poker_main.sImg;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class AdapterListViewPoker extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListViewPoker> itens;
    private final Locale locale = Locale.getDefault();
    private String datejogo;

    public AdapterListViewPoker(Context context, ArrayList<ItemListViewPoker> itens)
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
    public ItemListViewPoker getItem(int position)
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
        ItemListViewPoker item = itens.get(position);


        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.list_item_jogospoker, null);


        CircleImageView img = view.findViewById(R.id._imagem_prod);

        String imgpath = item.getImage_path();

        if(imgpath.equals("0")){
            Picasso.with(view.getContext()).load("http://futsexta.16mb.com/Poker/imgjogo/default.png").into(img);
        }else{
            if(Poker.reload){
                Picasso.with(view.getContext()).load(imgpath).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);

            }else{
                Picasso.with(view.getContext()).load(imgpath).into(img);
            }
        }

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.tvdescrijogo)).setText(item.getDescricao());
        //((TextView) view.findViewById(R.id.tvdatajogo)).setText(item.getData());
        ((TextView) view.findViewById(R.id.tvqtdplayers)).setText(item.getQtdplayers());

        String ds1  = item.getData();

        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
             datejogo = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((TextView) view.findViewById(R.id.tvdatajogo)).setText(datejogo);

        String valor = item.getVlentrada();
        BigDecimal parsed = parseToBigDecimal(valor);
        String formatted;
        formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

        ((TextView) view.findViewById(R.id.tvvalorvenda)).setText(formatted);
        //((TextView) view.findViewById(R.id.tvvalorvenda)).setText(format.format(item.getValor()));
        //((TextView) view.findViewById(R.id.lchamado)).setText("Nº Produto:");

        //((ImageView) view.findViewById(R.id._imagem_prod)).setImageResource(item.getIconeRid());

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
