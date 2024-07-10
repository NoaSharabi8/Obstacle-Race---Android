package com.example.mygame.Utilities;

import static com.example.mygame.Activities.MenuActivity.RECORDS_LIST_JSON_KEY;

import android.content.Context;
import android.util.Log;

import com.example.mygame.Models.RecordsList;
import com.google.gson.Gson;

public class DataManager {
    private static DataManager dataManager;
    private static Context context;
    private static RecordsList recordsList = new RecordsList();;
    private DataManager(Context context) {
        this.context = context;
    }
    public static void init(Context context) {
        if (dataManager == null){
            synchronized (DataManager.class){
                if (dataManager == null){
                    dataManager = new DataManager(context.getApplicationContext());
                    //loadRecordsListFromSP();
                }
            }
        }
    }
    public static DataManager getInstance() {
        return dataManager;
    }
    //private static void loadRecordsListFromSP() {}


    public static RecordsList getRecordsList() {
        return recordsList;
 }
    public void setRecordsList(RecordsList newRecordsList) {
        this.recordsList = newRecordsList;
    }
    public void updateRecordsListJson() {
        Log.d("current list:",DataManager.getInstance().getRecordsList().toString());
        String toJson = new Gson().toJson(DataManager.getInstance().getRecordsList());
        Log.d("JSON UPDATE:",toJson);
        MSP.getInstance().saveString(RECORDS_LIST_JSON_KEY,toJson);
    }
}
