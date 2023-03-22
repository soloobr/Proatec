package com.example.luiseduardo.infrafacil;

public class ItemListViewFornecedor {
    private String mId;
    private String mDescricao;
    private String mCnpj;
    private String mEndereco;


    public ItemListViewFornecedor(String id, String descricao, String cnpj, String endereco)

    {
        mId = id;
        mDescricao = descricao;
        mCnpj = cnpj;
        mEndereco = endereco;

    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmDescricao() {
        return mDescricao;
    }

    public void setmDescricao(String mDescricao) {
        this.mDescricao = mDescricao;
    }

    public String getmCnpj() {
        return mCnpj;
    }

    public void setmCnpj(String mCnpj) {
        this.mCnpj = mCnpj;
    }

    public String getmEndereco() {
        return mEndereco;
    }

    public void setmEndereco(String mEndereco) {
        this.mEndereco = mEndereco;
    }
    @Override
    public String toString() {
        return mId ;
    }
}
