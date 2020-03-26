package com.zachl.apocalypsecalculator.entities.managers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.apocalypsecalculator.R;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ViewManager {
    private static final String[] types = {"hs", "tp", "wb"};
    private AppCompatActivity activity;
    private View v;
    private String type;
    private final String key;
    private ArrayList<String[]> sources = new ArrayList<>();

    private enum Icon{
        HS(R.drawable.hs_icon),
        TP(R.drawable.tp_icon),
        WB(R.drawable.wb_icon);

        int val;
        Icon(int val){
            this.val = val;
        }
    }

    public ViewManager(AppCompatActivity activity, View v, String type){
        this.activity = activity;
        this.v = v;
        this.type = type;
        key = activity.getString(R.string.type_mod);
        sources.add(activity.getResources().getStringArray(R.array.resources));
        sources.add(activity.getResources().getStringArray(R.array.current_array));
        sources.add(activity.getResources().getStringArray(R.array.describe_array));
    }

    public void apply(){
        modify(collect((ViewGroup)v));
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
            if(v.getChildAt(i).getContentDescription() != null && v.getChildAt(i).getContentDescription().toString().equalsIgnoreCase(key)){
                views.add(v.getChildAt(i));
            }
        }
        return views;
    }

    private void modify(ArrayList<View> views){
        for(View view : views){
            if(view instanceof TextView){
                for(int i = 0; i < sources.size(); i++){
                    for(String s : sources.get(i)){
                        if(((TextView)view).getText().toString().equalsIgnoreCase(s)){
                            ((TextView)view).setText(sources.get(i)[getTypeIndex(type)]);
                        }
                    }
                }
            }
            else if(view instanceof ImageView){
                ((ImageView)view).setImageResource(Icon.values()[getTypeIndex(type)].val);
            }
        }
    }
}
