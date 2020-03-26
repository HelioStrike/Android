package com.zachl.apocalypsecalculator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.entities.runnables.Buffer;
import com.zachl.apocalypsecalculator.entities.runnables.BufferRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.Updating;
import com.zachl.apocalypsecalculator.entities.wrappers.ManagedActivity;

import java.util.ArrayList;

public class ResultsActivity extends ManagedActivity implements Updating {
    private int[] answers = new int[3];
    private String type;
    //private Function.Eq eq = Function.Eq.Tp;
    private float[] results;
    private UpdateRunnable updateR;
    private View[] calcs = new View[3];
    private ConstraintLayout ui;
    private int resultI;
    private int headerBuffer = 5;
    private int textBuffer = 8;
    private View icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_key1);

        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        icon = findViewById(R.id.icon2);
        build((View)icon.getParent(), type);
        for(int i = 0; i < answers.length; i++){
            answers[i] = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SUFF + i));
        }

        /*switch(type){
            case "tp":
                eq = Function.Eq.Hs_Days;
                break;
            case "wb":
                eq = Function.Eq.Wb_Days;
                break;
        }
        Function function = new Function(answers);
        results = function.apply(eq);*/

        calcs[0] = findViewById(R.id.results1);
        calcs[1] = findViewById(R.id.results2);
        calcs[2] = findViewById(R.id.results3);
        ui = findViewById(R.id.menu);
        BufferRunnable buffer = new BufferRunnable(new Buffer() {
            @Override
            public void wake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        start();
                    }
                });
            }
        }, headerBuffer);
        buffer.start();

        BufferRunnable buffer2 = new BufferRunnable(new Buffer() {
            @Override
            public void wake() {
                textChange();
            }
        }, textBuffer);
        buffer2.start();
    }

    public void goHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void start(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(this, R.layout.activity_results);
        TransitionManager.beginDelayedTransition(ui);
        constraintSet.applyTo(ui);
    }
    public void textChange(){
        updateR = new UpdateRunnable(this, 50);
        updateR.start(calcs[resultI]);
    }
    @Override
    public void update(View view) {
        if(Integer.valueOf(((TextView)view).getText().toString()) < answers[resultI]){
            final View fview = view;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)fview).setText(Integer.toString(Integer.valueOf(((TextView)fview).getText().toString()) + 1));
                }
            });
        }
        else{
            updateR.end();
            if(resultI < 1) {
                resultI++;
                textChange();
            }
        }
    }
}
