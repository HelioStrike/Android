package com.zachl.restock.entities.wrappers;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.entities.managers.ColorManager;
import com.zachl.restock.entities.managers.HazardManager;
import com.zachl.restock.entities.managers.Manager;
import com.zachl.restock.entities.managers.ViewManager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ManagedActivity extends AppCompatActivity {
    protected ViewManager vm ;
    protected ColorManager cm;
    protected String type = "hs";
    private Manager[] managers;

    public enum Package{
        Default(new Class[]{ViewManager.class, ColorManager.class}),
        All(new Class[]{ViewManager.class, ColorManager.class, HazardManager.class});

        Class[] contents;
        Package(Class[] contents){
            this.contents = contents;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void build(ArrayList<ViewGroup> v, String type, Package pack){
        for(int i = 0; i < pack.contents.length; i++){
            try {
                Constructor<?> ctor = pack.contents[i].getConstructor(AppCompatActivity.class, ArrayList.class, String.class);
                Object object = ctor.newInstance(this, v, type);
                ((Manager)object).apply();
            }
            catch(Exception e){
                //e.printStackTrace();
            }
        }
    }
    protected void buildManager(ArrayList<ViewGroup> v, String type, Class manager, double result){
        try {
            Constructor<?> ctor = manager.getConstructor(AppCompatActivity.class, ArrayList.class, String.class);
            Object object = ctor.newInstance(this, v, type);
            ((HazardManager) object).setPositive(result);
            ((Manager)object).apply();
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }
}
