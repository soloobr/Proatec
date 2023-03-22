package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class ItemListViewPeca {
    private String texto;
    private String descri;
    private String numero;
    private String valor;
    private int iconeRid;


    public ItemListViewPeca(String status, int trabalho100)
    {
    }

    public ItemListViewPeca(String texto, String descri, String numero, String valor, int iconeRid)

    {
        this.texto = texto;
        this.descri = descri;
        this.numero = numero;
        this.valor = valor;
        this.iconeRid = iconeRid;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIconeRid()
    {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid)
    {
        this.iconeRid = iconeRid;
    }

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }
}

