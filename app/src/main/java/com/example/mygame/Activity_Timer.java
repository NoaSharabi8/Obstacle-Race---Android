package com.example.mygame;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_Timer extends AppCompatActivity {
    private MaterialTextView timer_LBL_info;
    private MaterialButton timer_BTN_start;
    private MaterialButton timer_BTN_stop;
    private int count = 0;
    private final int DELAY = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

   /*     timer_LBL_info = findViewById(R.id.timer_LBL_info);
        timer_BTN_start = findViewById(R.id.timer_BTN_start);
        timer_BTN_stop = findViewById(R.id.timer_BTN_stop);*/
        timer_BTN_start.setOnClickListener(v -> start());
        timer_BTN_stop.setOnClickListener(v -> stop());
    }
    private void tick() {
        count++;
        timer_LBL_info.setText("" + count);
    }
    // ------------------------------------------------------------------------------------
    private Timer timer;
    private void stop() {
        timer.cancel();
    }
    private void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("pttt", Thread.currentThread().getName());
                runOnUiThread(() -> tick());
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, DELAY);
    }
}