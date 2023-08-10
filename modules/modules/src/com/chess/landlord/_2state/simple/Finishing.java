package com.chess.landlord._2state.simple;

import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public class Finishing implements StateHandler {
    @Override
    public void onTimer(StateSuperContext ctx) {
        final FinishingContext fc = (FinishingContext) ctx;
    }
}
