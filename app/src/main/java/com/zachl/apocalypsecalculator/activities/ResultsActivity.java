package com.zachl.apocalypsecalculator.activities;

import androidx.appcompat.app.ActionBar;
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

import com.zachl.apocalypsecalculator.MainActivity;
import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.runnables.Buffer;
import com.zachl.apocalypsecalculator.runnables.BufferRunnable;
import com.zachl.apocalypsecalculator.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.runnables.Updating;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements Updating {
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

    private ArrayList<Integer> views = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> sources = new ArrayList<>();
    private ArrayList<Integer> colorViews = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();

    private View icon;
    private int[] iconSrces;
    private TextView dualPrompt1, dualPrompt2;
    private int typeI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_key1);

        sources.add(new ArrayList<Integer>());
        sources.add(new ArrayList<Integer>());
        sources.get(1).add(R.string.resource_1);
        sources.get(1).add(R.string.resource_1);
        sources.add(new ArrayList<Integer>());
        sources.get(2).add(R.string.resource_3);
        sources.get(2).add(R.string.resource_3);

        views.add(R.id.subtitle2);
        views.add(R.id.text8);

        colorViews.add(R.id.header);
        colorViews.add(R.id.home);

        colors.add(R.color.hs);
        colors.add(R.color.tp);
        colors.add(R.color.wb);

        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        for(int i = 0; i < answers.length; i++){
            answers[i] = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SUFF + i));
        }

        icon = findViewById(R.id.icon2);
        iconSrces = new int[]{R.drawable.hs_icon, R.drawable.tp_icon, R.drawable.wb_icon};
        if(!type.equalsIgnoreCase("hs")) {
            typeI++;
            if (type.equalsIgnoreCase("wb"))
                typeI ++;
        }
            for(int i = 0; i < sources.get(typeI).size(); i++){
                ((TextView)findViewById(views.get(i))).setText(getString(sources.get(typeI).get(i)));
            }
            for(int i = 0; i < colorViews.size(); i++){
                DrawableCompat.setTint(((ImageView)findViewById(colorViews.get(i))).getDrawable(), ContextCompat.getColor(getApplicationContext(), colors.get(typeI)));
            }
            ((ImageView)icon).setImageResource(iconSrces[typeI]);


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
