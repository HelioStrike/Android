package com.zachl.apocalypsecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zachl.apocalypsecalculator.activities.CalculatorActivity;
import com.zachl.apocalypsecalculator.runnables.Buffer;
import com.zachl.apocalypsecalculator.runnables.BufferRunnable;
import com.zachl.apocalypsecalculator.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.runnables.Updating;

public class MainActivity extends AppCompatActivity implements Updating {
    /**
     * Every activity is a screen/page on the app
     */
    public static final String EXTRA = "COM.ZACHL.CALCULATOR.TYPE";
    private UpdateRunnable updateR;
    private ImageButton tp, hs, wb, desc;
    private TextView descT;
    private ConstraintLayout ui;

    private float scaleIncr;
    private float target;
    private int descI;
    private String[] descSrces;
    private int headerBuffer = 10;
    private float tempScale = 1;

    private boolean initd = false;
    private boolean triggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_key1);
        tp = findViewById(R.id.tp_button);
        hs = findViewById(R.id.hs_button);
        wb = findViewById(R.id.wb_button);
        desc = findViewById(R.id.header);
        descT = findViewById(R.id.desc_2);

        descSrces = new String[]{getString(R.string.description_2_1), getString(R.string.description_2_2), getString(R.string.description_2_3),
                getString(R.string.description_2_4), getString(R.string.description_2_5)};
        View.OnTouchListener buttonL = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !triggered) {
                    expand(v, 1.3f, 0.015f);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || triggered){
                    expand(v, 1f, -0.01f);
                    return true;
                }
                return false;
            }
        };
        tp.setOnTouchListener(buttonL);
        hs.setOnTouchListener(buttonL);
        wb.setOnTouchListener(buttonL);
        desc.setOnTouchListener(buttonL);

        ui = findViewById(R.id.ui);

        BufferRunnable buffer = new BufferRunnable(new Buffer() {
            @Override
            public void wake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });
            }
        }, headerBuffer);
        buffer.start();
    }
    public void init(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(this, R.layout.activity_main);
        TransitionManager.beginDelayedTransition(ui);
        constraintSet.applyTo(ui);
        initd = true;
    }

    public void expand(View view, float target, float scaleIncr){
        if(updateR != null && updateR.running()){
            updateR.end();
        }
        updateR = new UpdateRunnable(this);
        updateR.start(view);
        this.scaleIncr = scaleIncr;
        this.target = target;
        this.tempScale = view.getScaleY();
    }
    public void trigger(View view){
        switch(view.getId()){
            case R.id.header:
                if(descI == descSrces.length - 1)
                    descI = -1;
                descI++;
                descT.setText(descSrces[descI]);
                break;
            default:
                Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                intent.putExtra(EXTRA, view.getContentDescription());
                startActivity(intent);
        }
    }
    @Override
    public void update(View view) {
        if (tempScale > target + 0.02f || tempScale < target -0.02f) {
            tempScale += scaleIncr;
            view.setScaleY(tempScale);
            view.setScaleX(tempScale);
        } else {
            updateR.end();
            if (target > 1) {
                triggered = true;
                final View fview = view;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trigger(fview);
                    }
                });
            }
            else{
                triggered = false;
            }
        }
    }
}
