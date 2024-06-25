package com.example.mygame;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textview.MaterialTextView;

public class Activity_LifeCycle extends AppCompatActivity {
    //private MaterialTextView timer_LBL_info;
    private int count = 0;
    private final int DELAY = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("pttt", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    timer_LBL_info = findViewById(R.id.timer_LBL_info);
    }
    @Override
    protected void onStart() {
        Log.d("pttt", "onStart");
        super.onStart();
    }
    @Override
    protected void onResume() {
        Log.d("pttt", "onResume");
        super.onResume();
        start();
    }
    @Override
    protected void onPause() {
        Log.d("pttt", "onPause");
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.d("pttt", "onStop");
        super.onStop();
        stop();
    }
    @Override
    protected void onDestroy() {
        Log.d("pttt", "onDestroy");
        super.onDestroy();
    }
    private void tick() {
        count++;
        //timer_LBL_info.setText("" + count);
        playSound();
    }
    private void playSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ------------------------------------------------------------------------------------
    private final Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            tick();
            handler.postDelayed(runnable, DELAY);
        }
    };;
    private void stop() {
        handler.removeCallbacks(runnable);
    }
    private void start() {
        handler.postDelayed(runnable, DELAY);
    }
}