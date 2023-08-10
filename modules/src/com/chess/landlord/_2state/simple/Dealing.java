package com.chess.landlord._2state.simple;

import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public class Dealing implements StateHandler {
    @Override
    public void onTimer(StateSuperContext ctx) {
        DealingContext dc = (DealingContext) ctx;
    }

    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        DealingContext dc = (DealingContext) ctx;
        dc.onDealingSuccess(this);
    }
}
