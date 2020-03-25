package com.zachl.apocalypsecalculator.entities.managers;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.zachl.apocalypsecalculator.R;

import java.util.ArrayList;

public class ColorManager {
    private static final String[] types = {"hs", "tp", "wb"};
    private AppCompatActivity activity;
    private View v;
    private String type;
    private final String key;

    private enum Color{
        HS(R.color.hs),
        TP(R.color.tp),
        WB(R.color.wb),
        MAIN(R.color.main),
        HAZARD(R.color.hazard);

        int val;
        Color(int val){
            this.val = val;
        }
    }
    public ColorManager(AppCompatActivity activity, View v, String type){
        this.activity = activity;
        this.v = v;
        this.type = type;
        key = activity.getString(R.string.color_mod);
    }

    public void apply(){
        modify(collect((ViewGroup) v));
    }

    private int getTypeIndex(String type){
        for(int i = 0; i < types.length; i++){
            if(type.equalsIgnoreCase(types[i]))
                return i;
        }
        return 0;
    }
    private ArrayList<View> collect(ViewGroup v){
        ArrayList<View> views = new ArrayList<>();
        for(int i = 0; i < v.getChildCount(); i++){
            if(v.getChildAt(i).getContentDescription() != null && v.getChildAt(i).getContentDescription().toString().equalsIgnoreCase(key)) {
                views.add(v.getChildAt(i));
            }
        }
        return views;
    }

    private void modify(ArrayList<View> views){
        for(View view : views){
            DrawableCompat.setTint(((ImageView)view).getDrawable(), ContextCompat.getColor(activity.getApplicationContext(), Color.values()[getTypeIndex(type)].val));
        }
    }
}
