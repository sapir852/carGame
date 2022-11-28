package com.example.cargame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_CarGame extends AppCompatActivity {
    private final int NUM_OF_COLUMNS = 3;
    final int DELAY = 1000;
    private final int MAX_LIVES = 3;

    private AppCompatImageView game_IMG_background;
    private ExtendedFloatingActionButton game_BTN_Right;
    private ExtendedFloatingActionButton game_BTN_Left;


    private ShapeableImageView[] game_IMG_hearts;
    private ImageView[] game_IMG_car;
    private int move = 1;
    int l=1;
    int r=-1;

    private ImageView[][] rock;
    private int[][] vals;
    private int live=MAX_LIVES;

    private int colum_Choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initViews();
        hideSystemUI(this);

        Glide
                .with(Activity_CarGame.this)
                .load("https://i.pinimg.com/originals/0c/1b/c0/0c1bc08b26b95f83240cc4edba40f0e3.jpg")
                .into(game_IMG_background);

        startTimer();
        initVals();
        //  refreshUI();



    }
    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();

    }
    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }
    private void initVals(){
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {
                vals[i][j] = 0;
            }
        }
    }


    private void moveCar(int direction) {

        if (direction==-1 && move < 2) {
            game_IMG_car[move].setVisibility(View.INVISIBLE);
            move++;
            game_IMG_car[move].setVisibility(View.VISIBLE);
        } else if (direction==1 && move >= 1) {
            game_IMG_car[move].setVisibility(View.INVISIBLE);
            move--;
            game_IMG_car[move].setVisibility(View.VISIBLE);
        }
    }


//    private void refreshUI() {
//        logic();
//        chekGame();
//        //updateTimeUI();
//
//    }


    private void initViews() {
        game_BTN_Left.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveCar(l);
            }
        }));
        game_BTN_Right.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCar(r);
            }
        }));
    }

    private void logic() {

        colum_Choose = (int) Math.floor(Math.random() * NUM_OF_COLUMNS);

        for (int i = vals.length - 1; i > 0; i--) {
            for (int j = 0; j < vals[0].length; j++) {
                vals[i][j] = vals[i - 1][j]; // save the rock for next colum
            }
        }

        for (int i = 0; i < vals[0].length; i++) {
            vals[0][i] = 0;
        }

        vals[0][colum_Choose] = 1;

        updateUILogic();
    }
    private void updateUILogic() {

        for (int i = 0; i < rock.length; i++) {
            for (int j = 0; j < rock[i].length; j++) {
                ImageView im = rock[i][j];
                if (vals[i][j] == 0) {
                    im.setVisibility(View.INVISIBLE);
                } else if (vals[i][j] == 1 ) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.ic_rock);

                }}}}


    private void chekGame() {
        if (vals[NUM_OF_COLUMNS-1][move] == 1  ) {

            MySignal.getInstance().vibrate();
            live--;

            game_IMG_hearts[live].setVisibility(View.INVISIBLE);

            if (live <= 0) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        stopTimer();
        MySignal.getInstance().vibrate();
        MySignal.getInstance().toast("GAME OVER!!!! ");

    }

    private void findView() {
        // game_TXT_time = findViewById(R.id.game_TXT_time);
        game_IMG_background = findViewById(R.id.game_IMG_background);

        game_BTN_Right = findViewById(R.id.game_BTN_Right);

        game_BTN_Left = findViewById(R.id.game_BTN_Left);



        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart3),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart1),};

        game_IMG_car = new ImageView[]{
                findViewById(R.id.game_IMG_left),
                findViewById(R.id.game_IMG_midl),
                findViewById(R.id.game_IMG_right),};

        rock= new ImageView[][]{
                {findViewById(R.id.game_IMG_rock_00), findViewById(R.id.game_IMG_rock_01), findViewById(R.id.game_IMG_rock_02)},
                {findViewById(R.id.game_IMG_rock_10), findViewById(R.id.game_IMG_rock_11), findViewById(R.id.game_IMG_rock_12)},
                {findViewById(R.id.game_IMG_rock_20), findViewById(R.id.game_IMG_rock_21), findViewById(R.id.game_IMG_rock_22)}
        };

        vals = new int[rock.length][rock[0].length];


    }


    //////full screen
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI(this);
        }
    }
    public static void hideSystemUI(Activity activity) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Dim the Status and Navigation Bars
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // Without - cut out display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    //timer
    private Timer timer = new Timer();

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {
                        logic();
                        chekGame();


                        //updateTimeUI();
                    }
                });
            }
        }, DELAY, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }

}
