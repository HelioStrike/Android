package com.zachl.apocalypsecalculator.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.TextView;

import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.entities.managers.HazardManager;
import com.zachl.apocalypsecalculator.entities.math.Function;
import com.zachl.apocalypsecalculator.entities.runnables.interfaces.Buffer;
import com.zachl.apocalypsecalculator.entities.runnables.BufferRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.interfaces.Updating;
import com.zachl.apocalypsecalculator.entities.wrappers.ManagedActivity;

import java.util.ArrayList;

public class ResultsActivity extends ManagedActivity implements Updating {
    private int[] answers = new int[4];
    private String type;
    private Function.Multiplier eq;
    private double[] results;
    private UpdateRunnable updateR;
    private View[] calcs = new View[3];
    private ConstraintLayout ui;
    private int resultI = 0;
    private int headerBuffer = 5;
    private int textBuffer = 8;
    private View icon;
    BufferRunnable buffer2;

    private ArrayList<TextView> hazardText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_key1);

        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        icon = findViewById(R.id.icon2);
        build((View)icon.getParent(), type, Package.Default);
        for(int i = 0; i < answers.length - 1; i++){
            answers[i] = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SUFF + i));
        }
        answers[3] = (Float.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_PERCENT))).intValue();

        switch(type){
            case "hs":
                eq = Function.Multiplier.HS;
                break;
            case "tp":
                eq = Function.Multiplier.TP;
                break;
            case "wb":
                eq = Function.Multiplier.WB;
                break;
        }
        Function function = new Function(answers);
        results = function.apply(eq);
        buildManager((View) icon.getParent(), type, HazardManager.class, results[1]);
        results[1] = Math.abs(results[1]);

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

       buffer2 = new BufferRunnable(new Buffer() {
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
        buffer2.end();
        updateR = new UpdateRunnable(this, 50);
        updateR.start(calcs[resultI]);
    }
    @Override
    public void update(View view) {
        if(Integer.valueOf(((TextView)view).getText().toString()) < results[resultI]){
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
            ((TextView)view).setText("" + (int)results[resultI]);
            if(resultI < 1) {
                resultI++;
                textChange();
            }
        }
    }
}
