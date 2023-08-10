package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

import java.util.Objects;

public class Dealing implements StateHandler {
    protected Scheduler scheduler;
    protected Scheduler.Desc desc;
    @Override
    public void onInit(StateSuperContext ctx) {
        System.out.println("Dealing.onInit permanent update");
        StateHandler.super.onInit(ctx);
        scheduler = Scheduler.create();
        desc = scheduler.scheduleUpdate(()-> System.out.println("Dealing.onInit:"+ctx.now()),3000);
    }

    @Override
    public void onTimer(StateSuperContext ctx) {
        DealingContext dc = (DealingContext) ctx;
        scheduler.tick();
    }
    void ok(StateSuperContext ctx){
        DealingContext dc = (DealingContext) ctx;
        dc.onDealingSuccess(this);
    }

    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        if(Objects.equals("next",params[0])){
            ok(ctx);
            System.out.println("change state");
        }
    }

    @Override
    public void onDestroy(StateSuperContext ctx) {
        StateHandler.super.onDestroy(ctx);
        desc.destroy();
        desc = null;
        scheduler.destroy();
        scheduler = null;
    }
}
