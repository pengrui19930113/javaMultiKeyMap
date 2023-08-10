package com.chess.landlord._1game.simple;

import com.chess.landlord._1game.Config;

public class SimpleConfig implements Config {
    protected int tickMaxPostRunnable = 10;
    protected static final int INVALID_TICK_MAX_POST_RUNNABLE_FIXED_NUM = 10;
    protected boolean debug = false;
    public int getTickMaxPostRunnable() {
        return tickMaxPostRunnable>0
                ?tickMaxPostRunnable
                :INVALID_TICK_MAX_POST_RUNNABLE_FIXED_NUM;
    }
    public boolean isDebug() {
        return debug;
    }
}
