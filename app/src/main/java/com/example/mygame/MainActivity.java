package com.example.mygame;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.button.MaterialButton;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private AppCompatImageView[] game_IMG_hearts;
    private AppCompatImageView[][] game_IMG_matrix;
    private Timer timer;
    private final int DELAY = 1000;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameManager = new GameManager(4,3,3, 2);
        findViews();
        updateLivesUI();
        initMainActorImage(gameManager.getColumns());
        updateMainActorView(gameManager.getMainActorPosition());

        mp = MediaPlayer.create(this, R.raw.audio_miri);
        game_BTN_left.setOnClickListener(v -> moveToLeft());
        game_BTN_right.setOnClickListener(v -> moveToRight());
        startClock();
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
                Toast.makeText(this, "\uD83D\uDDE3\uFE0F", Toast.LENGTH_SHORT).show();
                vibrate();
                gameManager.decreaseLive();
                updateLivesUI();
            }
        }
        if (gameManager.getLives() == 0) {
            lose();
            //return;
        } /*else if (?) {
            win();
            return;}*/

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

        game_IMG_matrix = new AppCompatImageView[][] {
                {findViewById(R.id.game_row0col0),
                findViewById(R.id.game_row0col1),
                findViewById(R.id.game_row0col2)},
                {findViewById(R.id.game_row1col0),
                findViewById(R.id.game_row1col1),
                findViewById(R.id.game_row1col2)},
                {findViewById(R.id.game_row2col0),
                findViewById(R.id.game_row2col1),
                findViewById(R.id.game_row2col2)},
                {findViewById(R.id.game_row3col0),
                findViewById(R.id.game_row3col1),
                findViewById(R.id.game_row3col2)}
        };

        game_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
                findViewById(R.id.game_IMG_heart4),
        };
    }
    private void lose() {
        gameManager.setLives(3);
        updateLivesUI();
        //Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
        //gameDone();
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

 /*   private void gameDone() {
        Log.d("pttt", "Game Done");
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
        finish();
    } */
    /* private void win() {
        Toast.makeText(this, "You win " + gameManager.getScore(), Toast.LENGTH_SHORT).show();
        gameDone();} */
}
