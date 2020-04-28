package com.zachl.restock.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.R;
import com.zachl.restock.entities.managers.Manager;

public class PopUp extends AppCompatActivity {
    public static final String key = "COM.ZACHL.RESTOCK.POPUP";
    private TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.4));
        hint = findViewById(R.id.hint);
        String type = getIntent().getStringExtra(key);
        hint.setText(getResources().getStringArray(R.array.resource_hints)[Manager.getTypeIndex(type)]);
    }
}
