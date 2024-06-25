package com.example.mygame;
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
    public Item setGood(boolean healthy) {
        isGood = healthy;
        return this;
    }
}

/*
img_watermelon_juice
img_tacos
img_soup
img_soup_mushrooms
img_sausages
img_salad
img_pasta
img_pancake
img_nachos
img_ice_cream
img_hot_dog
img_eggs
img_chicken
img_burger
 */