package com.example.mygame.Activities;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mygame.Interfaces.StepCallback;
import com.example.mygame.Logic.GameManager;
import com.example.mygame.R;
import com.example.mygame.Utilities.StepDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    public static final int FAST_SPEED = 500;
    public static final int SLOW_SPEED = 800;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;
    private AppCompatImageView[][] game_IMG_matrix;
    private Timer timer;
    private int DELAY = SLOW_SPEED;
    private StepDetector stepDetector;
    MediaPlayer mp;
    public static final String SP_KEY_NAME = "NAME";
    public static final String SP_KEY_ADDRESS = "ADDRESS";
    public static final String SP_KEY_SCORE = "SCORE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameManager = new GameManager(5,5,3, 2);
        findViews();
        initViews();
        updateLivesUI();
        initMainActorImage(gameManager.getColumns());
        updateMainActorView(gameManager.getMainActorPosition());
        initMoveDetector();
        mp = MediaPlayer.create(this, R.raw.audio_miri);
        Intent prev = getIntent();
        setDELAY(parseInt(prev.getExtras().getString("SPEED")));
        setMODE(parseInt(prev.getExtras().getString("MODE")));
        startClock();
    }

    private void initViews() {
        game_BTN_left.setOnClickListener(v -> moveToLeft());
        game_BTN_right.setOnClickListener(v -> moveToRight());
        setReturnMode();
    }

    private void setReturnMode() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }
    private void setMODE(int mode) {
        if(mode==1) {
            game_BTN_left.setVisibility(View.INVISIBLE);
            game_BTN_right.setVisibility(View.INVISIBLE);
            stepDetector.start();
        }
    }
    private void startClock() {
        TimerTask task = new TimerTask() {
            public void run() {
                Log.d("pttt", Thread.currentThread().getName());
                runOnUiThread(() -> tick());
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, DELAY);
    }
    private void tick() {
        drive();
    }
    private void drive() {
        int currentMainActorPosition=gameManager.getMainActorPosition();
        int ItemAboveMainActor =gameManager.getItemValueInMatrix(1,currentMainActorPosition);
        if(ItemAboveMainActor!=-1)
        {
            if(!gameManager.getItems().get(ItemAboveMainActor).isGood()) {
                mp.start(); //make sound
                Toast.makeText(this, "תיסעעע", Toast.LENGTH_SHORT).show();
                vibrate();
                gameManager.decreaseLive();
                updateLivesUI();
            } else {
                gameManager.incrementScore();
                game_LBL_score.setText("" + gameManager.getScore());
            }
        }
        if (gameManager.getLives() == 0) {
            finishGame();
            //return;
        }
        gameManager.takeStep();
        updateView();
    }
    private void initMainActorImage(int columns) {
        int image = gameManager.getMainActor().getImage();
        for(int i=0; i<columns; i++) {
            game_IMG_matrix[0][i].setImageResource(image);
        }
    }
    private void updateMainActorView(int currentPosition) {
        for(int i=0; i<gameManager.getColumns(); i++) {
            if(i==currentPosition)
                game_IMG_matrix[0][i].setVisibility(View.VISIBLE);
            else {
                game_IMG_matrix[0][i].setVisibility(View.INVISIBLE);
            }
        }
    }
    private void updateView() {
        int rows=gameManager.getRows();
        int columns=gameManager.getColumns();

        for(int i=1; i<rows; i++) //not include row 0 (of main actor)
            for(int j=0; j<columns; j++)
                game_IMG_matrix[i][j].setVisibility(View.INVISIBLE);

        for(int i=1; i<rows; i++) { //not include row 0 (of main actor)
            for (int j=0; j<columns; j++) {
                int item = gameManager.getItemValueInMatrix(i,j);
                if (item!=-1) {
                    int currentItem=gameManager.getItems().get(item).getImage();
                    game_IMG_matrix[i][j].setImageResource(currentItem);
                    game_IMG_matrix[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void updateLivesUI() {
        int SZ = game_IMG_hearts.length;

        for (int i = 0; i < SZ; i++)
            game_IMG_hearts[i].setVisibility(View.VISIBLE);

        for (int i = 0; i < SZ - gameManager.getLives(); i++)
            game_IMG_hearts[SZ - i - 1].setVisibility(View.INVISIBLE);
    }
    private void moveToRight() {
        int newPosition = gameManager.moveMainActorToRight();
        updateMainActorView(newPosition);
    }
    private void moveToLeft() {
        int newPosition = gameManager.moveMainActorToLeft();
        updateMainActorView(newPosition);
    }
    private void findViews() {
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_LBL_score = findViewById(R.id.game_LBL_score);

        game_IMG_matrix = new AppCompatImageView[][] {
                {findViewById(R.id.game_row0col0),
                        findViewById(R.id.game_row0col1),
                        findViewById(R.id.game_row0col2),
                        findViewById(R.id.game_row0col3),
                        findViewById(R.id.game_row0col4)},
                {findViewById(R.id.game_row1col0),
                        findViewById(R.id.game_row1col1),
                        findViewById(R.id.game_row1col2),
                        findViewById(R.id.game_row1col3),
                        findViewById(R.id.game_row1col4)},
                {findViewById(R.id.game_row2col0),
                        findViewById(R.id.game_row2col1),
                        findViewById(R.id.game_row2col2),
                        findViewById(R.id.game_row2col3),
                        findViewById(R.id.game_row2col4)},
                {findViewById(R.id.game_row3col0),
                        findViewById(R.id.game_row3col1),
                        findViewById(R.id.game_row3col2),
                        findViewById(R.id.game_row3col3),
                        findViewById(R.id.game_row3col4)},
                {findViewById(R.id.game_row4col0),
                        findViewById(R.id.game_row4col1),
                        findViewById(R.id.game_row4col2),
                        findViewById(R.id.game_row4col3),
                        findViewById(R.id.game_row4col4)}
        };

        game_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
                findViewById(R.id.game_IMG_heart4),
        };
    }
    private void finishGame() {
        Intent i = new Intent(this, GameOverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SCORE", ""+gameManager.getScore());
        i.putExtras(bundle);
        timer.cancel();
        startActivity(i);
        onPause();
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void setDELAY(int delay){
        if(delay==0)
            DELAY=SLOW_SPEED;
        if(delay==1)
            DELAY=FAST_SPEED;

    }
    private void initMoveDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepLeft() {
                moveToLeft();
            }
            @Override
            public void stepRight() {
                moveToRight();
            }
        });
    }
}
