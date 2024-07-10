package com.example.mygame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.Models.RecordsList;
import com.example.mygame.R;
import com.example.mygame.Utilities.DataManager;
import com.example.mygame.Utilities.MSP;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {
    public static final String RECORDS_LIST_JSON_KEY = "RECORDS_LIST_JSON";
    private MaterialButton menu_BTN_records;
    private MaterialButton menu_BTN_buttons_mode;
    private MaterialButton menu_BTN_sensors_mode;
    private MaterialButton menu_BTN_speed_slow;
    private MaterialButton menu_BTN_speed_fast;
    private MaterialButton menu_BTN_start;
    private int mode=-1; // -1 -> not pressed, 0 -> buttons, 1 -> sensors
    private int speed=-1; // -1 -> not pressed, 0 -> slow, 1 -> fast


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
        getDataFromJson();
    }

    private void initViews() {
        menu_BTN_start.setVisibility(View.INVISIBLE);
        menu_BTN_speed_fast.setOnClickListener(v -> setSpeed(1));
        menu_BTN_speed_slow.setOnClickListener(v -> setSpeed(0));
        menu_BTN_sensors_mode.setOnClickListener(v -> setMode(1));
        menu_BTN_buttons_mode.setOnClickListener(v -> setMode(0));
        menu_BTN_start.setOnClickListener(v -> startGame(mode,speed));
        menu_BTN_records.setOnClickListener(v -> moveToRecordsPage());
        setReturnMode();
    }
    private void setReturnMode() {
        getOnBackPressedDispatcher().addCallback(this,
            new OnBackPressedCallback(true) {
                @Override
                //if you press the return button in your phone all activities will close
                public void handleOnBackPressed() {
                    finishAffinity();
                }
            });
    }
    private void moveToRecordsPage() {
        menu_BTN_records.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
        Intent i = new Intent(this, RecordsActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }
    private void startGame(int mode, int speed) {
        menu_BTN_start.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
        //Log.d("start: ", "mode"+mode +"speed"+ speed);
        Intent i = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("MODE", ""+mode);
        bundle.putString("SPEED", ""+speed);
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }
    private void setSpeed(int val) {
        if(val==0) {
            speed=val;
            menu_BTN_speed_slow.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
            menu_BTN_speed_fast.setBackgroundColor(getResources().getColor(R.color.menu_buttons));
        } else if(val==1) {
            speed=val;
            menu_BTN_speed_fast.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
            menu_BTN_speed_slow.setBackgroundColor(getResources().getColor(R.color.menu_buttons));
        }

        if(mode>=0 && speed>=0)
            menu_BTN_start.setVisibility(View.VISIBLE);
    }
    private void setMode(int val) {
        if(val==0) {
            mode=val;
            menu_BTN_buttons_mode.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
            menu_BTN_sensors_mode.setBackgroundColor(getResources().getColor(R.color.menu_buttons));
        } else if(val==1) {
            mode=val;
            menu_BTN_sensors_mode.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
            menu_BTN_buttons_mode.setBackgroundColor(getResources().getColor(R.color.menu_buttons));
        }
        if(mode>=0 && speed>=0)
            menu_BTN_start.setVisibility(View.VISIBLE);
    }
    private void findViews() {
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
        menu_BTN_buttons_mode = findViewById(R.id. menu_BTN_buttons_mode);
        menu_BTN_sensors_mode= findViewById(R.id. menu_BTN_sensors_mode);
        menu_BTN_speed_fast = findViewById(R.id. menu_BTN_speed_fast);
        menu_BTN_speed_slow = findViewById(R.id. menu_BTN_speed_slow);
        menu_BTN_start= findViewById(R.id. menu_BTN_start);
    }
    private void getDataFromJson() {
        String fromSP =  MSP.getInstance().readString(RECORDS_LIST_JSON_KEY,"");
        RecordsList recordsListFromJson = new Gson().fromJson(fromSP, RecordsList.class);
        if (recordsListFromJson != null) {
            Log.d("From JSON", recordsListFromJson.toString());
            DataManager.getInstance().setRecordsList(recordsListFromJson);
        }
    }
}
