package com.example.luiseduardo.infrafacil;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicoFragment extends Fragment {

    static View v;

    private String title;
    private int page;

    public static EditText editServRealizado = Status_Ordem.editServRealizado2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         Activity a;
       // Log.i ("Activit", a);
        Log.i ("Activit", context.getClass().getName());
        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    public ServicoFragment() {
        // Required empty public constructor
    }
    public static ServicoFragment newInstance(int page, String title) {
        ServicoFragment fragmentFirst = new ServicoFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        // Inflate the layout for this fragment

       // editServRealizado2 = (EditText) findViewById(R.id.editServRealizado2);

        v = inflater.inflate(R.layout.fragment_servico,container, false);
        editServRealizado = (EditText) v.findViewById(R.id.editServRealizado);
        //editServRealizado.setText("oi");
        //return inflater.inflate(R.layout.fragment_servico, container, false);
        return (v);
    }

}
