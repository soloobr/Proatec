package com.example.luiseduardo.infrafacil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterSpinnerFornecedor extends ArrayAdapter<ItemListViewFornecedor> {

    /*

    public AdapterSpinnerFornecedor(Context context, ArrayList<ItemListViewFornecedor> mFornecedorList) {
        super(context,0,mFornecedorList);
    }

    public AdapterSpinnerFornecedor(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource();
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_fornecedor_layout,parent, false);
        }

        TextView id = convertView.findViewById(R.id.spinnerid);
        TextView descricao = convertView.findViewById(R.id.spinnerdescricao);

        ItemListViewFornecedor CurrentItem = getItem(position);

        id.setText(CurrentItem.getmId());
        descricao.setText(CurrentItem.getmDescricao());

        return convertView;


    }
*/

    LayoutInflater flater;

    public AdapterSpinnerFornecedor(Activity context, int resouceId, int textviewId, List<ItemListViewFornecedor> list){

        super(context,resouceId,textviewId, list);
//        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);

    }

    private View rowview(View convertView , int position){

        ItemListViewFornecedor rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.spinner_fornecedor_layout, null, false);

            holder.txtID = (TextView) rowview.findViewById(R.id.spinnerid);
            holder.txtDescricao = (TextView) rowview.findViewById(R.id.spinnerdescricao);
            //holder.txtcnpj = (TextView) rowview.findViewById(R.id.spinnerdescricao);
            //holder.txtendereco = (TextView) rowview.findViewById(R.id.spinnerdescricao);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtID.setText(rowItem.getmId());
        holder.txtDescricao.setText(rowItem.getmDescricao());
       // holder.txtcnpj.setText(rowItem.getmCnpj());
       // holder.txtendereco.setText(rowItem.getmEndereco());

        return rowview;
    }

    private class viewHolder{
        TextView txtID;
        TextView txtDescricao;
        TextView txtcnpj;
        TextView txtendereco;
    }
}

