package com.zachl.restock.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zachl.restock.R;
import com.zachl.restock.entities.runnables.BufferRunnable;
import com.zachl.restock.entities.runnables.UpdateRunnable;

public class LoginActivity extends AppCompatActivity {
    View logo;
    TextView email;
    ConstraintLayout ui;
    UpdateRunnable updateR;
    float target, inc, tempScale;
    String id;
    boolean initd = false;
    Intent intent = null;
    private BufferRunnable buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_key1);
        logo = findViewById(R.id.app_logo);
        buffer = new BufferRunnable(new BufferRunnable.Buffer() {
            @Override
            public void wake() {
                startActivity();
            }
        }, 8);
        buffer.start();
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        if(buffer == null){
            buffer = new BufferRunnable(new BufferRunnable.Buffer() {
                @Override
                public void wake() {
                    startActivity();
                }
            }, 8);
            buffer.start();
        }
    }

    public void startActivity(){
        if(intent == null) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            buffer = null;
            intent = null;
        }
    }
}
