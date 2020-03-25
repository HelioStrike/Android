package com.zachl.apocalypsecalculator.entities.wrappers;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.apocalypsecalculator.entities.managers.ColorManager;
import com.zachl.apocalypsecalculator.entities.managers.ViewManager;

public class ManagedActivity extends AppCompatActivity {
    protected ViewManager vm ;
    protected ColorManager cm;
    protected String type = "hs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void build(View v, String type){
        vm = new ViewManager(this, v, type);
        vm.apply();
        cm = new ColorManager(this, v, type);
        cm.apply();
    }
}
