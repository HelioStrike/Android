package com.zachl.restock.entities.managers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.R;

import java.util.ArrayList;

public class ViewManager extends Manager{
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

    public ViewManager(AppCompatActivity activity, ArrayList<ViewGroup> v, String type){
        super(activity, v, type, R.string.type_mod);
        sources.add(activity.getResources().getStringArray(R.array.resources));
        sources.add(activity.getResources().getStringArray(R.array.current_array));
        sources.add(activity.getResources().getStringArray(R.array.sizes1));
        sources.add(activity.getResources().getStringArray(R.array.sizes2));
        sources.add(activity.getResources().getStringArray(R.array.size_array));
        sources.add(activity.getResources().getStringArray(R.array.resource_dirs));
    }

    @Override
    public void modify(ArrayList<View> views){
        for(View view : views){
            if(view instanceof TextView){
                for(int i = 0; i < sources.size(); i++){
                    for(String s : sources.get(i)){
                        if(((TextView)view).getText().toString().equalsIgnoreCase(s)){
                            ((TextView) view).setText(sources.get(i)[getTypeIndex(type)]);
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
