package com.zachl.restock.entities.managers;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.entities.runnables.UpdateRunnable;

import java.util.ArrayList;

public class ButtonManager extends Manager{
    private enum Type{
        Expand("e_", new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                        final UpdateRunnable updateR = new UpdateRunnable(new UpdateRunnable.Updater() {
                            @Override
                            public void update(View view) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    expand(v, v.getScaleY(), 1.3f, 0.015f);
                                }
                                else if(event.getAction() == MotionEvent.ACTION_UP){
                                    expand(v, v.getScaleY(), 1f, -0.01f);
                                }
                            }
                        });
                        if(!updateR.running()){
                            updateR.start(v);
                        }
                    }
                return false;
            }
        });
        String key;
        public View.OnTouchListener listener;
        Type(String key, View.OnTouchListener listener){
            this.key = key;
            this.listener = listener;
        }
    }
    private static View.OnTouchListener listener;
    public ButtonManager(AppCompatActivity activity, ArrayList<ViewGroup> v, String type, int keyID) {
        super(activity, v, type, keyID);
    }

    @Override
    public void modify(ArrayList<View> views) {
        for(View view : views){
            String tkey = view.getContentDescription().toString().replace(key, "");
            view.setOnTouchListener(Type.valueOf(tkey).listener);
        }
    }

    private static void expand(View view, float tempScale, float target, float scaleIncr){
        if (tempScale > target + 0.02f || tempScale < target -0.02f) {
            tempScale += scaleIncr;
            view.setScaleY(tempScale);
            view.setScaleX(tempScale);
        }
    }
}
