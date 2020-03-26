package com.zachl.apocalypsecalculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.entities.runnables.Updating;
import com.zachl.apocalypsecalculator.entities.wrappers.ManagedActivity;

import java.util.ArrayList;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CalculatorActivity extends ManagedActivity{
    public static final String EXTRA_SUFF = ".ANSWER.";
    public static final String EXTRA_PERCENT = ".ANSWER.PERCENT";
    private int[] answerViews;

    private View icon, slider;
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
        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);

        icon = findViewById(R.id.icon);
        build((View)icon.getParent(), type);

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
        answerViews = new int[]{R.id.text1, R.id.text2, R.id.text3};
    }

    public void changeImage(View view){
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra(MainActivity.EXTRA, type);
        int[] answers = new int[3];
        for(int i = 0; i < answers.length; i++){
            answers[i] = Integer.valueOf(((TextView) findViewById(answerViews[i])).getText().toString());
            if(answers[i] > 0) {
                intent.putExtra(MainActivity.EXTRA + EXTRA_SUFF + i, "" + answers[i]);
            }
            intent.putExtra(MainActivity.EXTRA + EXTRA_PERCENT, "" + percentValue);
        }
        startActivity(intent);
    }
}
