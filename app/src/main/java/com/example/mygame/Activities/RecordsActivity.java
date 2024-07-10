package com.example.mygame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.Fragments.ListFragment;
import com.example.mygame.Fragments.MapFragment;
import com.example.mygame.Interfaces.CallBack_SendClick;
import com.example.mygame.Models.Record;
import com.example.mygame.R;
import com.google.android.material.button.MaterialButton;


public class RecordsActivity extends AppCompatActivity {
    private ListFragment listFragment;
    private MapFragment mapFragment;
    private TextView records_txt;
    private static Record[] recordsListSoretedByScore ;
    private final int NUM_OF_RECORDS_SHOWN = 10;
    private MaterialButton records_BTN_back;
    CallBack_SendClick callBack_SendClick = new CallBack_SendClick() {
        @Override
        public void userRecordClicked(Record record) {
            showUserLocation(record);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        findViews();
        setRecordsList();
        initFragments();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
        records_BTN_back.setOnClickListener(v-> goToMenu());
        recordsListSoretedByScore = new Record[NUM_OF_RECORDS_SHOWN];
        getSupportFragmentManager().beginTransaction()
                .add(R.id.records_FRAME_list, listFragment)
                .add(R.id.records_FRAME_map, mapFragment)
                .commit();

    }

    private void goToMenu() {
        Intent i = new Intent(this, MenuActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        //onPause();
        finish();
    }
    private void setRecordsList() {

    }
    private void initFragments() {
        listFragment = new ListFragment();
        listFragment.setCallBack(callBack_SendClick);
        mapFragment = new MapFragment();
    }

    private void findViews() {
        records_BTN_back=findViewById(R.id.records_BTN_back);
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_map, mapFragment).commit();
    }
    private void showUserLocation(Record record) {
        mapFragment.zoomOnUser(record.getXPosition(),record.getYPosition());
    }
}
