package com.example.alphamusic;

public class Selections {
    public String Name, PictureURL;
    public int NumOfSel;

    public Selections(){

    }

    public Selections(String Name, String PictureURL, int NumOfSel){
        this.Name = Name;
        this.NumOfSel = NumOfSel;
        this.PictureURL = PictureURL;
    }
}
