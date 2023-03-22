package com.example.luiseduardo.infrafacil;

public class ItemListViewDespesas {

    private String descri;
    private String nome;
    private String qtd;
    private String valorvendido;
    private String valortotal;
    private String datavenda;
    private String idocorencia;




    public ItemListViewDespesas(String descri, String nome, String qtd, String valorvendido, String valortotal, String datavenda, String idocorencia)

    {
        this.descri = descri;
        this.nome = nome;
        this.qtd = qtd;
        this.valorvendido = valorvendido;
        this.valortotal = valortotal;
        this.datavenda = datavenda;
        this.idocorencia = idocorencia;

    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public void setValorvendido(String valorvendido) {
        this.valorvendido = valorvendido;
    }

    public void setValortotal(String valortotal) {
        this.valortotal = valortotal;
    }

    public void setDatavenda(String datavenda) {
        this.datavenda = datavenda;
    }

    public void setIdocorencia(String idocorencia) { this.idocorencia = idocorencia; }

    public String getIdocorencia() { return idocorencia; }

    public String getDescri() {
        return descri;
    }

    public String getNome() {
        return nome;
    }

    public String getQtd() {
        return qtd;
    }

    public String getValorvendido() {
        return valorvendido;
    }

    public String getValortotal() {
        return valortotal;
    }

    public String getDatavenda() {
        return datavenda;
    }






}
