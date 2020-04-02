package com.zachl.apocalypsecalculator.entities.math;

import android.util.Log;

public class Function {
    /**
     * An equation container class for managing the different equations required for the various result values without overcrowding the ResultsActivity class.
     * I'm in the process of modifying these, so as of now they're largely placeholders.
     */
    private int[] variables;

    public enum Multiplier{
        TP(0.064d),
        HS(0.067d),
        WB(2.56d);

        double mult;
        Multiplier(double mult){
            this.mult = mult;
        }
    }
    public Function(int days, int count, int persons, int slider){
        variables = new int[]{days, count, persons, slider};
    }
    public Function(int[] variables){
        this.variables = variables;
    }

    public double[] apply(Multiplier eq){
        double[] needs = new double[3];
        //DAYS SURVIVED
        needs[0] = (double)Math.round((variables[1] / eq.mult) / variables[2] / ((float)variables[3]/100));
        Log.i("NEEDS 0", "" + needs[0]);
        //DIFFERENCE IN SURVIVED AND QUARANTINE
        needs[1] = needs[0] - variables[0];
        Log.i("NEEDS 1", "" + needs[1]);
        //RATION PERCENT
        needs[2] = (int)((needs[0] / needs[1]) * 100);
        Log.i("NEEDS 2", "" + needs[2]);
        return needs;
    }
}
