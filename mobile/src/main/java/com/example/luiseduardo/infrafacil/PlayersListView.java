package com.example.luiseduardo.infrafacil;

public class PlayersListView {

        private String id;
        private String idjogo;
        private String nome;
        private String rebuy;
        private String addon;
        private String valor;
        private String vlentrada;
        private String vlrebuy;
        private String vladdon;
        private String imgpatch;
        private int iconeRid;




    public PlayersListView(String status, int trabalho100)
        {
        }


    public String getImgpatch() {
        return imgpatch;
    }

    public void setImgpatch(String imgpatch) {
        this.imgpatch = imgpatch;
    }

    public PlayersListView(String id, String idjogo, String nome, String rebuy, String addon, String valor, String vlentrada, String vlrebuy, String vladdon, int iconeRid, String imgpatch)

        {
            this.id = id;
            this.idjogo = idjogo;
            this.nome = nome;
            this.rebuy = rebuy;
            this.addon = addon;
            this.valor = valor;
            this.vlentrada = vlentrada;
            this.vlrebuy = vlrebuy;
            this.vladdon = vladdon;
            this.iconeRid = iconeRid;
            this.imgpatch = imgpatch;
        }
        public String getVlentrada() {
            return vlentrada;
        }

        public void setVlentrada(String vlentrada) {
            this.vlentrada = vlentrada;
        }

        public String getVlrebuy() {
            return vlrebuy;
        }

        public void setVlrebuy(String vlrebuy) {
            this.vlrebuy = vlrebuy;
        }

        public String getVladdon() {
            return vladdon;
        }

        public void setVladdon(String vladdon) {
            this.vladdon = vladdon;
        }
        public String getValor() { return valor; }

        public void setValor(String valor) { this.valor = valor; }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdjogo() {
            return idjogo;
        }

        public void setIdjogo(String idjogo) {
            this.idjogo = idjogo;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getRebuy() {
            return rebuy;
        }

        public void setRebuy(String rebuy) {
            this.rebuy = rebuy;
        }

        public String getAddon() {
            return addon;
        }

        public void setAddon(String addon) {
            this.addon = addon;
        }

        public int getIconeRid() {
            return iconeRid;
        }

        public void setIconeRid(int iconeRid) {
            this.iconeRid = iconeRid;
        }
}
