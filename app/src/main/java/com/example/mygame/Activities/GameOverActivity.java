package com.example.mygame.Activities;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mygame.Models.Record;
import com.example.mygame.Models.RecordsList;
import com.example.mygame.R;
import com.example.mygame.Utilities.DataManager;
import com.example.mygame.Utilities.GPS;
import com.google.android.material.button.MaterialButton;

public class GameOverActivity extends AppCompatActivity {
    private static final int  RECORD_LIST_SIZE = 10 ;
    private AppCompatImageView game_over_IMG_background;
    private TextView gameOver_TV_points;
    private EditText gameOver_ETXT_name;
    private MaterialButton gameOver_BTN_save;
    private MaterialButton gameOver_BTN_menu;
    private MaterialButton gameOver_BTN_scoreBoard;
    int score;
    private GPS gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        findViews();
        initViews();
        Intent prev = getIntent();
        score=parseInt(prev.getExtras().getString("SCORE"));
        gameOver_TV_points.append(Integer.toString(score));
        gps = new GPS(this);
    }
    private void initViews() {
        gameOver_BTN_menu.setVisibility(View.INVISIBLE);
        gameOver_BTN_scoreBoard.setVisibility(View.INVISIBLE);
        gameOver_BTN_save.setOnClickListener(v -> saveRecord());
        gameOver_BTN_menu.setOnClickListener(v -> goToMenu());
        gameOver_BTN_scoreBoard.setOnClickListener(v -> goToScoreBoard());
        setReturnMode();
    }
    private void setReturnMode() {
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
        }
    });
    }
    private void goToScoreBoard() {
        gameOver_BTN_scoreBoard.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
        Intent i = new Intent(this, RecordsActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void goToMenu() {
        gameOver_BTN_menu.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
        Intent i = new Intent(this, MenuActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
    private void saveRecord() {
        String name = gameOver_ETXT_name.getText().toString();
        if(!name.isEmpty()) { //make sure there is a player name
            RecordsList tempList = DataManager.getRecordsList();
            if (tempList.getList().size() == RECORD_LIST_SIZE &&
                    tempList.getList().get(RECORD_LIST_SIZE-1).getScore() > score) { // dont add record if its not top 10
                return;
            }
            int i=0;
            gps.updateLocation(this);
            Record record =new Record(name,score,gps.getX(),gps.getY());
            for (i=0;i<tempList.getList().size();i++) {
                if (tempList.getList().get(i).getScore() <= score)
                    break;
            }
            if (tempList.getList().size() == RECORD_LIST_SIZE) { // delete last record if list full
                tempList.getList().remove(RECORD_LIST_SIZE-1);
            }
            tempList.getList().add(i,record);
            DataManager.getInstance().setRecordsList(tempList);
            DataManager.getInstance().updateRecordsListJson();

            gameOver_BTN_save.setBackgroundColor(getResources().getColor(R.color.menu_button_clicked));
            gameOver_BTN_menu.setVisibility(View.VISIBLE);
            gameOver_BTN_scoreBoard.setVisibility(View.VISIBLE);
            hideKeyboard();
            }
        }
    private void hideKeyboard() {
        View view = getCurrentFocus(); // returns the view that has focus or null
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
        private void findViews() {
        game_over_IMG_background = findViewById(R.id.game_over_IMG_background);
        gameOver_TV_points = findViewById(R.id.gameOver_TV_points);
        gameOver_ETXT_name = findViewById(R.id.gameOver_ETXT_name);
        gameOver_BTN_save = findViewById(R.id.gameOver_BTN_save);
        gameOver_BTN_menu = findViewById(R.id.gameOver_BTN_menu);
        gameOver_BTN_scoreBoard = findViewById(R.id.gameOver_BTN_scoreBoard);
    }
}
