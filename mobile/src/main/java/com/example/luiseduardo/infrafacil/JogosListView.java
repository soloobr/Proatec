package com.example.luiseduardo.infrafacil;

public class JogosListView {

        private final String image_path;
        private String id;
        private String Descricao;
        private String Data;
        private String vlentrada;
        private String qtdfichaentrada;
        private String vlrebuy;
        private String qtdficharebuy;
        private String vladdon;
        private String qtdfichaaddon;
        private String qtdplayers;

        public String getImage_path() {
            return image_path;
        }

        public String getQtdplayers() {
            return qtdplayers;
        }

        public void setQtdplayers(String qtdplayers) {
            this.qtdplayers = qtdplayers;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescricao() {
            return Descricao;
        }

        public void setDescricao(String descricao) {
            Descricao = descricao;
        }

        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }

        public String getVlentrada() {
            return vlentrada;
        }

        public void setVlentrada(String vlentrada) {
            this.vlentrada = vlentrada;
        }

        public String getQtdfichaentrada() {
            return qtdfichaentrada;
        }

        public void setQtdfichaentrada(String qtdfichaentrada) {
            this.qtdfichaentrada = qtdfichaentrada;
        }

        public String getVlrebuy() {
            return vlrebuy;
        }

        public void setVlrebuy(String vlrebuy) {
            this.vlrebuy = vlrebuy;
        }

        public String getQtdficharebuy() {
            return qtdficharebuy;
        }

        public void setQtdficharebuy(String qtdficharebuy) {
            this.qtdficharebuy = qtdficharebuy;
        }

        public String getVladdon() {
            return vladdon;
        }

        public void setVladdon(String vladdon) {
            this.vladdon = vladdon;
        }

        public String getQtdfichaaddon() {
            return qtdfichaaddon;
        }

        public void setQtdfichaaddon(String qtdfichaaddon) {
            this.qtdfichaaddon = qtdfichaaddon;
        }

        public JogosListView(String id, String Descricao, String Data, String vlentrada, String qtdfichaentrada, String vlrebuy, String qtdficharebuy, String vladdon, String qtdfichaaddon, String qtdplayers, String image_path) {
            this.id = id;
            this.Descricao = Descricao;
            this.Data = Data;
            this.vlentrada = vlentrada;
            this.qtdfichaentrada = qtdfichaentrada;
            this.vlrebuy = vlrebuy;
            this.qtdficharebuy = qtdficharebuy;
            this.vladdon = vladdon;
            this.qtdfichaaddon = qtdfichaaddon;
            this.qtdplayers = qtdplayers;
            this.image_path = image_path;


        }



}