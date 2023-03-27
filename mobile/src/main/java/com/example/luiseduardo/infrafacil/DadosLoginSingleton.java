package com.example.luiseduardo.infrafacil;

public class DadosLoginSingleton {
    private static DadosLoginSingleton instance;
    private DadosLogin dadosLogin;

    private DadosLoginSingleton() {
        dadosLogin = new DadosLogin();
    }

    public static DadosLoginSingleton getInstance() {
        if (instance == null) {
            instance = new DadosLoginSingleton();
        }
        return instance;
    }

    public DadosLogin getDadosLogin() {
        return dadosLogin;
    }
}
