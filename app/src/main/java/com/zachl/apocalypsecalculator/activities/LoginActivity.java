package com.zachl.apocalypsecalculator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zachl.apocalypsecalculator.R;
import com.zachl.apocalypsecalculator.entities.runnables.interfaces.Buffer;
import com.zachl.apocalypsecalculator.entities.runnables.BufferRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.UpdateRunnable;
import com.zachl.apocalypsecalculator.entities.runnables.interfaces.Updating;

public class LoginActivity extends AppCompatActivity implements Updating {
    View logo;
    TextView email;
    ConstraintLayout ui;
    UpdateRunnable updateR;
    float target, inc, tempScale;
    String id;
    boolean initd = false;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_key1);
        logo = findViewById(R.id.app_logo);
        BufferRunnable bufferR = new BufferRunnable(new Buffer() {
            @Override
            public void wake() {
                expand(logo, 1.3f, 0.01f);
            }
        }, 4);
        bufferR.start();
        ui = findViewById(R.id.ui);
        email = findViewById(R.id.email);
        //login = findViewById(R.id.login_button);
        //signup = findViewById(R.id.login_button);
    }

    public void expand(View view, float target, float inc){
        this.target = target;
        this.inc = inc;
        updateR = new UpdateRunnable(this, 20);
        updateR.start(view);
        tempScale = view.getScaleY();
    }

    @Override
    public void update(View view) {
        if(tempScale < target - 0.02 || tempScale > target + 0.02){
            tempScale += inc;
            view.setScaleY(tempScale);
            view.setScaleX(tempScale);
        }
        else{
            updateR.end();
            if(target > 1 && !initd){
                initd = true;
                expand(view, 0.8f, -0.01f);
            }
            else{
                /*
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.load(this, R.layout.activity_login);
                TransitionManager.beginDelayedTransition(ui);
                constraintSet.applyTo(ui);*/
                if(intent == null) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}
