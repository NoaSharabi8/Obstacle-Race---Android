package com.example.mygame;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Handler extends AppCompatActivity {
    //private MaterialTextView timer_LBL_info;
    //private MaterialButton timer_BTN_start;
    //private MaterialButton timer_BTN_stop;
    private int count = 0;
    private final int DELAY = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

  /*      timer_LBL_info = findViewById(R.id.timer_LBL_info);
        timer_BTN_start = findViewById(R.id.timer_BTN_start);
        timer_BTN_stop = findViewById(R.id.timer_BTN_stop);
        timer_BTN_start.setOnClickListener(v -> start());
        timer_BTN_stop.setOnClickListener(v -> stop());*/
    }
    private void tick() {
        count++;
        //imer_LBL_info.setText("" + count);
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