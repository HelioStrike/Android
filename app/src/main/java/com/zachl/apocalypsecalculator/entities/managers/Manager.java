package com.zachl.apocalypsecalculator.entities.managers;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.apocalypsecalculator.R;

import java.util.ArrayList;

public abstract class Manager {
    public static String[] resources;
    public static final String[] types = {"hs", "tp", "wb"};

    protected AppCompatActivity activity;
    protected View v;
    protected String type;
    protected final String key;

    public Manager(AppCompatActivity activity, View v, String type, int keyID) {
        this.activity = activity;
        this.v = v;
        this.type = type;
        key = activity.getString(keyID);
        resources = new String[]{activity.getString(R.string.resource_2), activity.getString(R.string.resource_1), activity.getString(R.string.resource_3)};
    }

    public void apply(){
        modify(collect((ViewGroup)v));
    }

    public int getTypeIndex(String type){
        for(int i = 0; i < types.length; i++){
            if(type.equalsIgnoreCase(types[i]))
                return i;
        }
        return 0;
    }

    private ArrayList<View> collect(ViewGroup v) {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < v.getChildCount(); i++) {
            if (v.getChildAt(i).getContentDescription() != null && v.getChildAt(i).getContentDescription().toString().contains(key)) {
                views.add(v.getChildAt(i));
            }
        }
        return views;
    }

    public abstract void modify(ArrayList<View> views);
}
