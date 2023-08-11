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
    @Override
    public void onInit(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        init = cs.last();
        ctx.log().println("enter starting state");
        lastLogTime = 3;
        scheduler = ctx.scheduler();
        desc = scheduler.schedule(()->{
            ctx.log().println(lastLogTime--);
            if(0==lastLogTime){
                scheduler.once(()->{
                    ctx.log().println("leaving starting");
                    cs.onStartingSuccess(Starting.this);
                },1000);
                desc.stop();
            }
        },0,0,1000);
    }
    @Override
    public void onTimer(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        scheduler.tick();
    }
    @Override
    public void onDestroy(StateSuperContext ctx) {
        final StartingContext cs = (StartingContext) ctx;
        if(!desc.destroy()){
            ctx.log().println("warning for desc.destroy on starting.destroy");
        }
        desc = null;
        scheduler.destroy();
        scheduler = null;
    }
}
