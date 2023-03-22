package com.example.luiseduardo.infrafacil;

public class ItemListViewPecas{
    private String Descricao;
    private String Modelo;
    private String id;
    private String Serie;
    private String Status;
    private int iconeRid;


    public ItemListViewPecas(String descricao, String modelo, String id,String serie, String status, int iconeRid)

    {
        this.Descricao = descricao;
        this.Modelo = modelo;
        this.id = id;
        this.Serie = serie;
        this.Status = status;
        this.iconeRid = iconeRid;


    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String serie) {
        Serie = serie;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }
}
