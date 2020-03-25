package com.zachl.apocalypsecalculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MotionEventCompat;

import com.zachl.apocalypsecalculator.MainActivity;
import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.runnables.Updating;

import java.util.ArrayList;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CalculatorActivity extends AppCompatActivity implements Updating {
    public static final String EXTRA_SUFF = ".ANSWER.";
    public static final String EXTRA_PERCENT = ".ANSWER.PERCENT";
    private int[] answerViews;

    private ArrayList<ArrayList<Integer>> sources = new ArrayList<>();
    private ArrayList<Integer> views = new ArrayList<>();
    private ArrayList<Integer> colorViews = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private View[] options;
    private View f1, f2, f3, icon, slider;
    private int[] iconSrces;
    private TextView percent;
    private float percentValue = 100;
    private ConstraintLayout ui;
    private int[] sliderBounds = {70, 830};

    private int typeI = 0;
    private int[][] optionSrces;
    private int[] optionsI = {0,0,0};
    private String type;

    private float mLastTouchX, mLastTouchY, mPosX;
    private int mActivePointerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sources.add(new ArrayList<Integer>());
        sources.add(new ArrayList<Integer>());
        sources.get(1).add(R.string.resource_1);
        sources.get(1).add(R.string.dir_3_tp);
        sources.get(1).add(R.string.dir_5_tp);
        sources.add(new ArrayList<Integer>());
        sources.get(2).add(R.string.resource_3);
        sources.get(2).add(R.string.dir_3_wb);
        sources.get(2).add(R.string.dir_5_wb);

        views.add(R.id.subtitle);
        views.add(R.id.prompt3);
        views.add(R.id.prompt5);

        colorViews.add(R.id.header);
        colorViews.add(R.id.calc);
        colorViews.add(R.id.slider);

        colors.add(R.color.hs);
        colors.add(R.color.tp);
        colors.add(R.color.wb);
        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);

        icon = findViewById(R.id.icon);
        iconSrces = new int[]{R.drawable.hs_icon, R.drawable.tp_icon, R.drawable.wb_icon};
        if(!type.equalsIgnoreCase("hs")) {
            typeI++;
            if (type.equalsIgnoreCase("wb"))
                typeI++;
        }
            for(int i = 0; i < sources.get(typeI).size(); i++){
                ((TextView)findViewById(views.get(i))).setText(getString(sources.get(typeI).get(i)));
            }
            for(int i = 0; i < colorViews.size(); i++){
                DrawableCompat.setTint(((ImageView)findViewById(colorViews.get(i))).getDrawable(), ContextCompat.getColor(getApplicationContext(), colors.get(typeI)));
                //((ImageView)findViewById(colorViews.get(i))).setColorFilter(colors.get(typeI));
            }
            ((ImageView)icon).setImageResource(iconSrces[typeI]);


        percent = findViewById(R.id.percent);
        slider = findViewById(R.id.slider);
        slider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final float x = MotionEventCompat.getX(event, pointerIndex);

                        // Remember where we started (for dragging)
                        mLastTouchX = x;
                        // Save the ID of this pointer (for dragging)
                        mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        // Find the index of the active pointer and fetch its position
                        final int pointerIndex =
                                MotionEventCompat.findPointerIndex(event, mActivePointerId);

                        final float x = MotionEventCompat.getX(event, pointerIndex);

                        // Calculate the distance moved
                        final float dx = x - mLastTouchX;

                        mPosX += dx;

                        if(v.getX() + mPosX > sliderBounds[0] && v.getX() + mPosX < sliderBounds[1]) {
                            v.setX(v.getX() + mPosX);
                            percent.setX(percent.getX() + mPosX);
                            percentValue += mPosX / 15;
                            percent.setText("" + (int)percentValue + "%");
                        }

                        // Remember this touch position for the next move event
                        mLastTouchX = x;

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_POINTER_UP: {

                        final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                        if (pointerId == mActivePointerId) {
                            // This was our active pointer going up. Choose a new
                            // active pointer and adjust accordingly.
                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                            mLastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                            mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        ui = findViewById(R.id.ui);
        /*f1 = findViewById(R.id.info1);
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
                {R.drawable.tp_button2u, R.drawable.tp_button2d}, {R.drawable.tp_button3u, R.drawable.tp_button3d}};*/
        answerViews = new int[]{R.id.text1, R.id.text2, R.id.text3};
    }

    /*public void expand(View view, float target, float scaleIncr){
        if(updateR != null && updateR.running()){
            updateR.end();
        }
        updateR = new UpdateRunnable(this);
        updateR.start(view);
        this.scaleIncr = scaleIncr;
        this.target = target;
    }*/
    @Override
    public void update(View view) {
        /*if(view.getScaleY() > target + 0.05f || view.getScaleY() < target - 0.05f){
            view.setScaleY(view.getScaleY() + scaleIncr);
            view.setScaleX(view.getScaleX() + scaleIncr);
        }
        else{
            updateR.end();
            if(target > 1)
                trigger(view);
        }*/
    }

    public void trigger(View view){
        //POP-UP
        view.setScaleY(1);
        view.setScaleX(1);
    }

    public void changeImage(View view){
        switch(view.getId()) {
            case R.id.calc:
                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                intent.putExtra(MainActivity.EXTRA, type);
                int[] answers = new int[3];
                for(int i = 0; i < answers.length; i++){
                        answers[i] = Integer.valueOf(((TextView) findViewById(answerViews[i])).getText().toString());
                    if(answers[i] > 0) {
                        intent.putExtra(MainActivity.EXTRA + EXTRA_SUFF + i, "" + answers[i]);
                        Log.i("Answer", "" + answers[i]);
                    }
                    intent.putExtra(MainActivity.EXTRA + EXTRA_PERCENT, "" + percentValue);
                }
                startActivity(intent);
                break;
            default:
                for (int i = 0; i < options.length; i++) {
                    ((ImageView) options[i]).setImageResource(optionSrces[i][optionsI[1]]);
                }
                int pos = Integer.valueOf(view.getTag().toString());
                if (optionsI[pos] == 1)
                    optionsI[pos] = -1;
                optionsI[pos]++;
                ((ImageView) view).setImageResource(optionSrces[pos][optionsI[pos]]);
                break;
        }
    }
}
