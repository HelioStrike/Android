package com.zachl.apocalypsecalculator.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zachl.apocalypsecalculator.MainActivity;
import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.UpdateRunnable;
import com.zachl.apocalypsecalculator.Updating;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity implements Updating {
    public static final String EXTRA_SUFF = ".ANSWER.";
    private int[] answerViews;

    private ArrayList<ArrayList<Integer>> sources = new ArrayList<>();
    private ArrayList<Integer> views = new ArrayList<>();
    private View[] options;
    private View f1, f2, f3;
    private int typeI = 0;
    private int[] calcSrces;
    private int calcI = 0;
    private int[][] optionSrces;
    private int[] optionsI = {0,0,0};
    private UpdateRunnable updateR;
    private float scaleIncr = 0.015f;
    private float target;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sources.add(new ArrayList<Integer>());
        sources.get(0).add(R.drawable.tp_field1);
        sources.get(0).add(R.drawable.tp_field2);
        sources.get(0).add(R.drawable.tp_field3);
        sources.get(0).add(R.drawable.tp_prompt);
        sources.get(0).add(R.drawable.tp_button1u);
        sources.get(0).add(R.drawable.tp_button2u);
        sources.get(0).add(R.drawable.tp_button3u);

        views.add(R.id.field1);
        views.add(R.id.field2);
        views.add(R.id.field3);
        views.add(R.id.prompt);
        views.add(R.id.option1);
        views.add(R.id.option2);
        views.add(R.id.option3);

        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        if(!type.equalsIgnoreCase("tp")){
            if(type.equalsIgnoreCase("wb"))
                typeI = 1;
            for(int i = 0; i < sources.size(); i++){
                ((ImageView)findViewById(views.get(i))).setImageResource(sources.get(typeI).get(i));
            }
        }

        View.OnTouchListener buttonL = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    expand(v, 1.5f, 0.015f);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    expand(v, 1f, -0.025f);
                    return true;
                }
                return false;
            }
        };

        f1 = findViewById(R.id.info1);
        f2 = findViewById(R.id.info2);
        f3 = findViewById(R.id.info3);

        f1.setOnTouchListener(buttonL);
        f2.setOnTouchListener(buttonL);
        f3.setOnTouchListener(buttonL);

        View b1 = findViewById(R.id.option1);
        View b2 = findViewById(R.id.option2);
        View b3 = findViewById(R.id.option3);
        options = new View[]{b1, b2, b3};

        calcSrces = new int[]{R.drawable.calculate, R.drawable.calculate_d};
        optionSrces = new int[][]{{R.drawable.tp_button1u, R.drawable.tp_button1d},
                {R.drawable.tp_button2u, R.drawable.tp_button2d}, {R.drawable.tp_button3u, R.drawable.tp_button3d}};
        answerViews = new int[]{R.id.text1, R.id.text2, R.id.text3};
    }

    public void expand(View view, float target, float scaleIncr){
        if(updateR != null && updateR.running()){
            updateR.end();
        }
        updateR = new UpdateRunnable(this);
        updateR.start(view);
        this.scaleIncr = scaleIncr;
        this.target = target;
    }
    @Override
    public void update(View view) {
        if(view.getScaleY() > target + 0.05f || view.getScaleY() < target - 0.05f){
            view.setScaleY(view.getScaleY() + scaleIncr);
            view.setScaleX(view.getScaleX() + scaleIncr);
        }
        else{
            updateR.end();
            if(target > 1)
                trigger(view);
        }
    }

    public void trigger(View view){
        //POP-UP
        view.setScaleY(1);
        view.setScaleX(1);
    }

    public void changeImage(View view){
        switch(view.getId()){
            case R.id.calculate:
                if(calcI == 1)
                    calcI = -1;
                calcI++;
                ((ImageView)view).setImageResource(calcSrces[calcI]);

                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                intent.putExtra(MainActivity.EXTRA, type);
                int[] answers = new int[3];
                for(int i = 0; i < answers.length; i++){
                    answers[i] = Integer.valueOf(((TextView)findViewById(answerViews[i])).getText().toString());
                    intent.putExtra(MainActivity.EXTRA + EXTRA_SUFF + i, answers[i]);
                }
                startActivity(intent);
                break;
            default:
                for(int i = 0; i < options.length; i++){
                    ((ImageView)options[i]).setImageResource(optionSrces[i][optionsI[1]]);
                }
                int pos = Integer.valueOf(view.getTag().toString());
                if(optionsI[pos] == 1)
                    optionsI[pos] = -1;
                optionsI[pos]++;
                ((ImageView)view).setImageResource(optionSrces[pos][optionsI[pos]]);
                break;
        }
    }
}
