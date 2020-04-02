package com.zachl.apocalypsecalculator.entities.managers;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.zachl.apocalypsecalculator.R;

import java.util.ArrayList;

public class HazardManager extends Manager {
    private int positive = 0;
    private ArrayList<String[]> sources = new ArrayList<>();
    public HazardManager(AppCompatActivity activity, View v, String type){
        super(activity, v, type, R.string.hazard_mod);
        sources.add(activity.getResources().getStringArray(R.array.results_3));
        sources.add(activity.getResources().getStringArray(R.array.results_4));
        sources.add(activity.getResources().getStringArray(R.array.results_5));
        sources.add(activity.getResources().getStringArray(R.array.result_conclusions));
    }
    public void setPositive(double diff){
        if(diff < 0) {
            this.positive = 1;
        }
        else{
            this.positive = 0;
        }
        Log.i("DIFF", "" + positive);
    }
    @Override
    public void modify(ArrayList<View> views) {
        for(View view : views){
            if(view instanceof TextView){
                    for(int i = 0; i < sources.size(); i++){
                        for(String s : sources.get(i)){
                            if(((TextView)view).getText().toString().equalsIgnoreCase(s)){
                                ((TextView)view).setText(sources.get(i)[positive]);
                            }
                        }
                    }
            }
            else if(view instanceof ImageView){
                if(positive == 1)
                    DrawableCompat.setTint(((ImageView)view).getDrawable(), ContextCompat.getColor(activity.getApplicationContext(), ColorManager.Color.HAZARD.val));
                else{
                    DrawableCompat.setTint(((ImageView)view).getDrawable(), ContextCompat.getColor(activity.getApplicationContext(), ColorManager.Color.values()[getTypeIndex(type)].val));
                }
            }
        }
    }
}
