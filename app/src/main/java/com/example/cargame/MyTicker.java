package com.example.cargame;

import java.util.Timer;
import java.util.TimerTask;

public class MyTicker {

    private Timer timer = new Timer();
    private Activity_CarGame.CallBack_Timer callBack_timer;

    public MyTicker(Activity_CarGame.CallBack_Timer callBack_timer) {
        this.callBack_timer = callBack_timer;
    }


    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (callBack_timer != null) {
                    callBack_timer.tick();
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        timer.cancel();
    }
}



