package com.example.luiseduardo.infrafacil;

public class itemLayoutView {
    private String texto;
    private String descri;
    private String numero;
    private int iconeRid;


    public itemLayoutView(String status, int trabalho100)
    {
    }

    public itemLayoutView(String texto, String descri, String numero, int iconeRid)

    {
        this.texto = texto;
        this.descri = descri;
        this.numero = numero;
        this.iconeRid = iconeRid;
    }

    public String getTitle() {
        return descri;
    }

    public void getTitle(String descri) {
        this.descri = descri;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getImageUrl()
    {
        return iconeRid;
    }

    public void getImageUrl(int iconeRid)
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
