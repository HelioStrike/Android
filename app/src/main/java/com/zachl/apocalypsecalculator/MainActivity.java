package com.zachl.apocalypsecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.zachl.apocalypsecalculator.activities.CalculatorActivity;

public class MainActivity extends AppCompatActivity implements Updating{
    /**
     * Every activity is a screen/page on the app
     */
    public static final String EXTRA = "COM.ZACHL.CALCULATOR.TYPE";
    private UpdateRunnable updateR;
    private ImageButton tp, hs, wb, desc;
    private ImageView descT;

    private float scaleIncr = 0.015f;
    private float target;
    private int descI;
    private int[] descSrces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tp = findViewById(R.id.tp_logo);
        hs = findViewById(R.id.hs_logo);
        wb = findViewById(R.id.wb_logo);
        desc = findViewById(R.id.desc_button);
        descT = findViewById(R.id.desc);

        descSrces = new int[]{R.drawable.desc_text1, R.drawable.desc_text2, R.drawable.desc_text3, R.drawable.desc_text4, R.drawable.desc_text5};
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
        tp.setOnTouchListener(buttonL);
        hs.setOnTouchListener(buttonL);
        wb.setOnTouchListener(buttonL);
        desc.setOnTouchListener(buttonL);
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

    public void trigger(View view){
        switch(view.getId()){
            case R.id.desc_button:
                if(descI == descSrces.length - 1)
                    descI = -1;
                descI++;
                descT.setImageResource(descSrces[descI]);
                desc.setScaleX(1);
                desc.setScaleY(1);
                break;
            default:
                Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                intent.putExtra(EXTRA, view.getContentDescription());
                startActivity(intent);
        }
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
}
