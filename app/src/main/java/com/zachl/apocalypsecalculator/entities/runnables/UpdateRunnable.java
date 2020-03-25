package com.zachl.apocalypsecalculator.entities.runnables;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateRunnable{
    /**
     * Consistent updating/modifying isn't available on the main thread, which is occupied by Android monitoring processes.
     * To circumvent this I use a basic timed runnable that requires a view, which is an object on the screen, as updating processes
     * only involve expanding, moving, or otherwise manipulating a view in a smooth, fluid manner.
     */
    AppCompatActivity activity;
    Timer timer;
    private int refresh;
    private boolean running;
    public UpdateRunnable(final AppCompatActivity activity){
        this.activity = activity;
        timer = new Timer();
        this.refresh = 5;
    }

    public UpdateRunnable(final AppCompatActivity activity, int refresh){
        this.activity = activity;
        timer = new Timer();
        this.refresh = refresh;
    }
    public void start(View view) {
        final View fView = view;
        running = true;
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        final AppCompatActivity temp = activity;
                        if (temp instanceof Updating) {
                            ((Updating) temp).update(fView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, refresh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void end(){
        timer.cancel();
        running = false;
    }

    public boolean running(){
        return running;
    }
}

