package com.example.luiseduardo.infrafacil;

public class Vendas {

    private String idvenda;
    private String idprod;
    private String idocor;
    private String status;
    private String descricao;
    private int iconeRid;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getIdvenda() {
        return idvenda;
    }

    public void setIdvenda(String idvenda) {
        this.idvenda = idvenda;
    }

    public String getIdprod() {
        return idprod;
    }

    public void setIdprod(String idprod) {
        this.idprod = idprod;
    }

    public String getIdocor() {
        return idocor;
    }

    public void setIdocor(String idocor) {
        this.idocor = idocor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }

    public Vendas() {

    }



    public Vendas(String idprod,String descricao, String idocor,String status ) {
        //this.idvenda = idvenda;
        this.idprod = idprod;
        this.descricao =  descricao;
        this.idocor =  idocor;
        this.status = status;
        this.iconeRid = iconeRid;

    }



}
