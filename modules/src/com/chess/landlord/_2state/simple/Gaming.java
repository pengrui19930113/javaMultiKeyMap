package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public class Gaming implements StateHandler {
    protected boolean gameOver;
    protected Scheduler scheduler;
    @Override
    public void onInit(StateSuperContext ctx) {
        final GamingContext cc = (GamingContext)ctx;
        scheduler = ctx.scheduler();
    }

    @Override
    public void onTimer(StateSuperContext ctx) {
        scheduler.tick();
        final GamingContext cc = (GamingContext)ctx;


        if(gameOver)
            cc.onGamingOver(this);
    }

    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        final GamingContext cc = (GamingContext)ctx;
        ctx.log().println("gaming action , change state");
        cc.onGamingOver(this);
    }

    @Override
    public void onDestroy(StateSuperContext ctx) {
        scheduler.destroy();
        scheduler = null;
    }
}
