package com.zachl.apocalypsecalculator.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zachl.apocalypsecalculator.MainActivity;
import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.UpdateRunnable;
import com.zachl.apocalypsecalculator.Updating;
import com.zachl.apocalypsecalculator.entities.Function;

import org.w3c.dom.Text;

public class ResultsActivity extends AppCompatActivity implements Updating {
    private int[] answers = new int[3];
    private String type;
    private Function.Eq eq = Function.Eq.Tp_Days;
    private float[] results;
    private UpdateRunnable updateR;
    private View[] calcs = new View[3];
    private int resultI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        for(int i = 0; i < answers.length; i++){
            answers[i] = intent.getIntExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SUFF + i, i);
        }

        switch(type){
            case "hs":
                eq = Function.Eq.Hs_Days;
                break;
            case "wb":
                eq = Function.Eq.Wb_Days;
                break;
        }
        Function function = new Function(answers);
        results = function.apply(eq);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        calcs[0] = findViewById(R.id.results1);
        calcs[1] = findViewById(R.id.results2);
        calcs[2] = findViewById(R.id.results3);
        start();
    }
    public void start(){
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
            if(resultI < 2) {
                resultI++;
                start();
            }
        }
    }
}
