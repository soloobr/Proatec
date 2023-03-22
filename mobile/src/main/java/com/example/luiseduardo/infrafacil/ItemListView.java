package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class ItemListView {

    private String name;
    private String datasaida;
    private String  dataentrada;
    private String  id;
    private String  status;
    private int iconeRid;


    public ItemListView(String status, int trabalho100)
    {
    }

    public ItemListView(String name, String datasaida, String dataentrada, String id, String status, int iconeRid)

    {
        this.name = name;
        this.datasaida = datasaida;
        this.dataentrada = dataentrada;
        this.id = id;
        this.status = status;
        this.iconeRid = iconeRid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatasaida() {
        return datasaida;
    }

    public void setDatasaida(String datasaida) {
        this.datasaida = datasaida;
    }

    public String getDataentrada() {
        return dataentrada;
    }

    public void setDataentrada(String dataentrada) {
        this.dataentrada = dataentrada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIconeRid()
    {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid)
    {
        this.iconeRid = iconeRid;
    }


}

