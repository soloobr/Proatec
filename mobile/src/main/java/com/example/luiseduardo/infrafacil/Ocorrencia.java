package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 04/12/2017.
 */

public class Ocorrencia {

private String NUM_Ocorrencia;
private String Nome;
private String Descri_Servi;
private String Status;

    public String getNUM_Ocorrencia() {
        return NUM_Ocorrencia;
    }

    public void setNUM_Ocorrencia(String NUM_Ocorrencia) {
        this.NUM_Ocorrencia = NUM_Ocorrencia;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescri_Servi() {
        return Descri_Servi;
    }

    public void setDescri_Servi(String descri_Servi) {
        Descri_Servi = descri_Servi;
    }

    public int getImagem(int position) {
        switch (position) {
            case 0:
                return (R.mipmap.trabalho100);
            case 1:
                return (R.mipmap.cracha);
            default:
                return (R.mipmap.if_tools);

        }
    }
}
