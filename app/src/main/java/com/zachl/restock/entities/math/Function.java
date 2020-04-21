package com.zachl.restock.entities.math;

public class Function {
    /**
     * An equation container class for managing the different equations required for the various result values without overcrowding the ResultsActivity class.
     * I'm in the process of modifying these, so as of now they're largely placeholders.
     */
    private int[] variables;

    public enum Multiplier{
        HS(new double[]{0.1d, 0.201d, 5.94d}),
        TP(new double[]{0.39d, 0.19d, 57}),
        WB(new double[]{2.56d, 43.26d, 1279.35d});
        double[] mults;
        Multiplier(double[] mult){
            this.mults = mult;
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
        needs[0] = (double)Math.round((variables[1] / eq.mults[variables[4]]) / variables[2] / ((float)variables[3]/100));
        //DIFFERENCE IN SURVIVED AND QUARANTINE
        needs[1] = needs[0] - variables[0];
        //RATION PERCENT
        needs[2] = (int)((needs[0] / needs[1]) * 100);
        return needs;
    }
}
