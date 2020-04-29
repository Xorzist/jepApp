package com.example.jepapp.Models;

public class MenuTypes {
    private String name;
    private int thumbnail;

    public MenuTypes() {}

    public MenuTypes(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }



    public int getThumbnail() { return thumbnail; }

    public void setThumbnail(int thumbnail) { this.thumbnail = thumbnail; }
}
