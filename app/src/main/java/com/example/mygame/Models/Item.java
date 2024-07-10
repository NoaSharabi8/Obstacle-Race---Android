package com.example.mygame.Models;
public class Item {
    private int image;
    private boolean isGood;
    public Item() {}
    public int getImage() {
        return image;
    }
    public Item setImage(int image) {
        this.image = image;
        return this;
    }
    public boolean isGood() {
        return isGood;
    }
    public Item setGood(boolean isGood) {
        this.isGood = isGood;
        return this;
    }
}
