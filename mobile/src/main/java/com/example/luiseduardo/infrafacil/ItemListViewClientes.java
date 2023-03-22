package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class ItemListViewClientes {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private int iconeRid;
    private String funcionario;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ItemListViewClientes(String id, String nome, String email, String telefone, String funcionario, String status)

    {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.iconeRid = iconeRid;
        this.funcionario = funcionario;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
}

