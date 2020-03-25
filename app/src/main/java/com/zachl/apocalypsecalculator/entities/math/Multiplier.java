package com.zachl.apocalypsecalculator.entities.math;

public class Multiplier {
    /**
     * A container for the final values which are used in calculations
     * This is the gross one; there's definitely a better way to use enums here,
     * I'm just not sure what that is.
     */
    public static final float TP_AVG_DAILY = 0;
    public static final float HS_AVG_DAILY = 0;

    public enum TpSizes{
        SMALL(1),
        AVERAGE(1),
        LARGE(1);
        float size;
        TpSizes(float size){this.size = size;}
    }

    public enum HsSizes{
        SMALL(1),
        AVERAGE(1),
        LARGE(1);
        float size;
        HsSizes(float size){this.size = size;}
    }

    public enum WbSizes{
        SMALL(1),
        AVERAGE(1),
        LARGE(1);
        float size;
        WbSizes(float size){this.size = size;}
    }
}
