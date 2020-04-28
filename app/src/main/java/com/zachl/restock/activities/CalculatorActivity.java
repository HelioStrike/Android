package com.zachl.restock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.zachl.restock.R;
import com.zachl.restock.entities.managers.ColorManager;
import com.zachl.restock.entities.managers.Manager;
import com.zachl.restock.entities.wrappers.ManagedActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.MotionEvent.ACTION_DOWN;

public class CalculatorActivity extends ManagedActivity{
    public static final String EXTRA_SUFF = ".ANSWER.";
    public static final String EXTRA_PERCENT = ".ANSWER.PERCENT";
    public static final String EXTRA_SIZE = ".ANSWER.SIZE";

    private int[] answerViews;

    private View icon;
    private ImageButton help;
    private ConstraintLayout ui;

    private int typeI = 0;
    private ArrayList<ImageView> options;
    private ArrayList<TextView> textOpts;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        help = (ImageButton) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help(v);
            }
        });
        icon = findViewById(R.id.icon);
        ArrayList<ViewGroup> av = new ArrayList<>();
        av.add((ViewGroup)icon.getParent());
        av.add((ViewGroup)findViewById(R.id.scroll_constraints));
        build(av, type, Package.Default);

        View.OnTouchListener optionL = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == ACTION_DOWN){
                    optionSelect(((ImageView)v));
                }
                return false;
            }
        };
        ui = findViewById(R.id.ui);
        answerViews = new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5};
        options = new ArrayList<>(Arrays.asList((ImageView)findViewById(R.id.size_b2), (ImageView) findViewById(R.id.size_b3)));
        textOpts = new ArrayList<>(Arrays.asList((TextView)findViewById(R.id.size2), (TextView) findViewById(R.id.size3)));

        for(View view : options){
            view.setOnTouchListener(optionL);
        }
        remove();
        optionSelect(options.get(0));
    }

    public void remove(){
        if(textOpts.get(1).getText().toString().equalsIgnoreCase(getString(R.string.null_key))){
            textOpts.get(1).setVisibility(View.INVISIBLE);
            options.get(1).setVisibility(View.INVISIBLE);
            textOpts.remove(1);
            options.remove(1);
            typeI = 0;
        }
    }
    public void changeImage(View view){
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra(MainActivity.EXTRA, type);
        int[] answers = new int[5];
        for(int i = 0; i < answers.length; i++){
            try {
                answers[i] = Integer.valueOf(((TextView) findViewById(answerViews[i])).getText().toString());
            }
            catch(NumberFormatException e){
                cancelStart();
                return;
            }
            if(answers[i] > 0) {
                intent.putExtra(MainActivity.EXTRA + EXTRA_SUFF + i, "" + answers[i]);
            }
            else{
                cancelStart();
                return;
            }
        }
        intent.putExtra(MainActivity.EXTRA + EXTRA_SIZE, "" + typeI);
        startActivity(intent);
    }

    public void optionSelect(ImageView view){
        int i = 0;
        for(ImageView iview : options){
            iview.setImageResource(R.drawable.size_field);
            if(iview== view) {
                typeI = i;
                break;
            }
            i++;
        }
        for(TextView tview : textOpts){
            tview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBack2));
        }
        view.setImageResource(R.drawable.size_filled);
        DrawableCompat.setTint(view.getDrawable(), ContextCompat.getColor(getApplicationContext(), ColorManager.Color.values()[Manager.getTypeIndex(type)].val));
        for(int j = 0; j < options.size(); j++){
            if(j != i)
                options.get(j).setImageResource(R.drawable.size_field);
        }
        textOpts.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBack1));
    }

    public void help(View view){
        Intent intent = new Intent(getApplicationContext(), PopUp.class);
        intent.putExtra(PopUp.key, type);
        startActivity(intent);
    }
    public void cancelStart(){
        Toast.makeText(getApplicationContext(), "Please fill out every required field", Toast.LENGTH_SHORT).show();
    }
}
