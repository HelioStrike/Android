package com.zachl.apocalypsecalculator.runnables;

import android.view.View;

public interface Updating {
    /**
     * Every activity that has moving, expanding, etc. components implements this.
     * Also, this should probably have something like a start function, but I'm already off the deep end in my use of this interface so
     * I'll come back to this once we have a solid foundation and scalability is the focus.
     */
    void update(View view);
}
