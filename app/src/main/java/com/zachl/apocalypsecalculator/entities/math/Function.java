package com.zachl.apocalypsecalculator.entities.math;

public class Function {
    /**
     * An equation container class for managing the different equations required for the various result values without overcrowding the ResultsActivity class.
     * I'm in the process of modifying these, so as of now they're largely placeholders.
     */
    private int[] variables;
    private interface Applyable{
        float apply(int[] variables);
    }

    public enum Eq implements Applyable{
        TP {
            public float apply(int[] variables) {
                float result;
                result = ((variables[1] * Multiplier.TP_AVG_DAILY) / variables[2]) / variables[4];
                return result;
            }
        },

        HS{
            public float apply(int[] variables) {
                float result;
                result = ((variables[1] * Multiplier.TP_AVG_DAILY) / variables[2]) / variables[4];
                return result;
            }
        },

        WB{
            public float apply(int[] variables) {
                float result;
                result = ((variables[1] * Multiplier.TP_AVG_DAILY) / variables[2]) / variables[4];
                return result;
            }
        };
    }
    public Function(int v1, int v2, int v3, int o){
        variables = new int[]{v1, v2, v3, o};
    }
    public Function(int[] variables){
        this.variables = variables;
    }

    public float apply(Eq eq){
        float result = eq.apply(variables);
        return result;
    }
}
