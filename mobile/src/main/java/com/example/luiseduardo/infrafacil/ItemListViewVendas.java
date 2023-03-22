package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class ItemListViewVendas {
    private String qtd;
    private String nome;
    private String txvalor;
    private int iconeRid;


    public ItemListViewVendas(String status, int trabalho100)
    {
    }

    public ItemListViewVendas(String qtd, String nome, String txvalor, int iconeRid)

    {
        this.qtd = qtd;
        this.nome = nome;
        this.txvalor = txvalor;
        this.iconeRid = iconeRid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public int getIconeRid()
    {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid)
    {
        this.iconeRid = iconeRid;
    }

    public String getValor()
    {
        return txvalor;
    }

    public void setValor(String txvalor)
    {
        this.txvalor = txvalor;
    }
}

