package com.chess.landlord._2state.simple;

import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public class Gaming implements StateHandler {
    protected boolean gameOver;
    @Override
    public void onTimer(StateSuperContext ctx) {
        final GamingContext cc = (GamingContext)ctx;
        if(gameOver)
            cc.onGamingOver(this);
    }
}
