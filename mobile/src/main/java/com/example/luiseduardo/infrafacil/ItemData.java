package com.example.luiseduardo.infrafacil;

public class ItemData {

    private String s;
    private int circle_shape;

    public ItemData(String s, int circle_shape) {

        this.s = s;
        this.circle_shape = circle_shape;

    }

    public String getTitle() {
        return s;
    }

    public void getTitle(String s) {
        this.s = s;
    }

    public void getImageUrl(int circle_shape)
    {

        this.circle_shape = circle_shape;
    }
}
