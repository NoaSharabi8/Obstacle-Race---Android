package com.example.mygame.Logic;

import com.example.mygame.Models.Item;
import com.example.mygame.R;

import java.util.ArrayList;

public class GameManager {
    private int rows = 4;
    private int columns = 3;
    private int lives = 3;
    private int score = 0;
    private Item mainActor;
    private int mainActorPosition=2;
    private ArrayList<Item> items = new ArrayList<>();
    private boolean addNewItem=true;
    private int[][] gameMatrix;
    public GameManager(int initialRows,int initialColumns, int initialLives, int initialMainActorPosition) {
        if (initialRows > 0 && initialRows <= 5) {
            rows = initialRows;
        }
        if (initialColumns > 0 && initialColumns <= 5) {
            columns = initialColumns;
        }
        if (initialMainActorPosition >= 0 && initialMainActorPosition <= columns) {
            mainActorPosition = initialMainActorPosition;
        }
        if (initialLives > 0 && initialLives <= 4) {
            lives = initialLives;
        }

        initGameMatrix(rows, columns);
        initMainActor(initialMainActorPosition);
        initItems();
    }
    private void initGameMatrix(int rows, int columns) {
        gameMatrix=new int[rows][columns];
        for(int i=0; i<rows; i++)
            for(int j=0; j<columns; j++) {
                gameMatrix[i][j]=-1;
            }
    }
    private void initItems() {
        items.add(new Item().setImage(R.drawable.img_driver3).setGood(false));
        items.add(new Item().setImage(R.drawable.img_bibi).setGood(true));
        items.add(new Item().setImage(R.drawable.img_sara).setGood(true));
    }
    private void initMainActor(int initialMainActorPosition) {
        mainActor=new Item().setImage(R.drawable.img_miri).setGood(true);
        if(initialMainActorPosition >= 0 && initialMainActorPosition <= columns-1) {
            gameMatrix[0][initialMainActorPosition]=1; //active main actor position
        } else {
            gameMatrix[0][0]=1; //active main actor position - default
        }
    }
    public int getRows() { return rows; }
    public int getColumns() { return columns; }
    public int getLives() {return lives;}
    public Item getMainActor() {return mainActor;}
    public int getMainActorPosition() {return mainActorPosition;}
    public ArrayList<Item> getItems() {return items;}
    public int getItemValueInMatrix(int i, int j) {return gameMatrix[i][j];}
    public void setAddNewItem(boolean val) {addNewItem=val;}
    public void setLives(int newLives) {lives=newLives;}
    public void decreaseLive() { lives--; }
    public int getScore() { return score; }
    public void incrementScore() { score++; }
    public int moveMainActorToRight() {
        if(getMainActorPosition()<(getColumns()-1)) {
            gameMatrix[0][mainActorPosition]=0;
            mainActorPosition++;
            gameMatrix[0][mainActorPosition]=1;
        }
        return mainActorPosition;
    }
    public int moveMainActorToLeft() {
        if(getMainActorPosition()>0) {
            gameMatrix[0][mainActorPosition]=0;
            mainActorPosition--;
            gameMatrix[0][mainActorPosition]=1;
        }
        return mainActorPosition;
    }
    public void takeStep() {
        for(int i=1; i<rows-1; i++)  //get values from upper row
            for(int j=0; j<columns; j++)
                gameMatrix[i][j]=gameMatrix[i+1][j];

        for(int j=0; j<columns; j++)  //update highest row
            gameMatrix[rows-1][j]=-1;

        if(addNewItem) { //check if we need to add item to new highest row
            int position=grillPosition();
            int newItem=grillItem();
            gameMatrix[rows-1][position]=newItem;
            setAddNewItem(false); //next time doesn't need to add new item
        } else {
            setAddNewItem(true); //next time need to add new item
        }
    }
    private int grillItem() {
        return (int) (Math.random()*(items.size()));
    }
    private int grillPosition() {
        return (int) (Math.random()*(getColumns()));
    }
}
