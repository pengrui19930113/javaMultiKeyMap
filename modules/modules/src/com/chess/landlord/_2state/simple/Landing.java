package com.chess.landlord._2state.simple;

import com.chess.Final;
import com.chess.landlord._1game.StateSuperContext;
import com.chess.landlord._1game.StateHandler;

public class Landing implements StateHandler {
    @Final
    protected long initTime;
    @Override
    public void onInit(StateSuperContext ctx) {
        final LandingContext lc = (LandingContext)ctx;
        initTime = lc.last();
    }
    @Override
    public void onTimer(StateSuperContext ctx) {
        final LandingContext lc = (LandingContext)ctx;
        if(lc.now()-initTime>1000000){
            lc.onLandingFailure(this);
        }
    }
    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        final LandingContext lc = (LandingContext)ctx;
        lc.onLandingSuccess(this);
    }
}
