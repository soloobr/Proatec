package com.example.luiseduardo.infrafacil;

/**
 * Created by Windows 10 on 19/11/2017.
 */

public class SuggestGetSet {
    String Id,Nome;
    public SuggestGetSet(String id, String name){
        this.setId(id);
        this.setName(name);

    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() { return Nome; }

    public void setName(String name) { this.Nome = name; }


}
