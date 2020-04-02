package com.zachl.apocalypsecalculator.entities.runnables;

import com.zachl.apocalypsecalculator.entities.runnables.interfaces.Buffer;

import java.util.Timer;
import java.util.TimerTask;

public class BufferRunnable {
    private Buffer buffer;
    private Timer timer;
    private int time;
    private int refresh;
    public BufferRunnable(Buffer buffer, int time){
        timer = new Timer();
        this.refresh = 100;
        this.time = time;
        this.buffer = buffer;
    }
    public void start(){
        final Buffer fbuffer = buffer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(time > 0)
                    time--;
                else{
                    buffer.wake();
                }
            }
        }, 0, refresh);
    }
    public void end(){
        timer.cancel();
    }
}
