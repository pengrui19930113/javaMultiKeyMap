package com.chess.landlord._2state.simple;

import com.chess.Final;
import com.chess.Scheduler;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public class Starting implements StateHandler {
    @Final
    protected long init;
    protected long lastLogTime;
    protected Scheduler scheduler;
    protected Scheduler.Desc desc;
    protected Scheduler.Desc desc2;
    @Override
    public void onInit(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        init = cs.last();
        lastLogTime = 0;
        scheduler = Scheduler.create();
        desc = scheduler.once(()->cs.onStartingSuccess(Starting.this),3000);
        desc2 = scheduler.schedule(()->System.out.println("now: "+ctx.now()),500,2,800);
    }
    @Override
    public void onTimer(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        scheduler.tick();
    }
    @Override
    public void onDestroy(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        desc.destroy();
        desc2.destroy();
        scheduler = null;
    }
}
