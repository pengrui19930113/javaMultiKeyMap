package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord.Poker;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Dealing implements StateHandler {
    protected Scheduler scheduler;
    protected Scheduler.Desc desc;
    /*
        17 *3 +3
     */
    protected List<Poker> pokers;
    protected List<Poker>[] places;
    @Override
    public void onInit(StateSuperContext ctx) {
        final DealingContext dc = (DealingContext) ctx;
        ctx.log().println("enter dealing state");
        pokers = dc.shuffledPokers();
        final List<Poker>[] places = new List[4];
        for(int i=0;i<places.length;i++){
            places[i] = new LinkedList<>();
        }
        this.places = places;
        scheduler = ctx.scheduler();
        desc = scheduler.scheduleUpdate(()-> {
            for(int i=0;i<3;i++){
                final Poker poker = pokers.remove(0);
                places[i].add(poker);
                ctx.log().print((i+1)+" "+poker+" ");
            }
            ctx.log().println();
            if(3==pokers.size()){
                places[3].addAll(pokers);
                pokers.clear();
                ctx.log().println("goal:"+places[3]);
                ctx.log().println("exist dealing state");
                dc.onDealingSuccess(this);
            }
        },100);
    }
    public List<Poker>[] result(){
        final List<Poker>[] result = new List[places.length];
        System.arraycopy(places,0,result,0,result.length);
        return result;
    }
    @Override
    public void onTimer(StateSuperContext ctx) {
        final DealingContext dc = (DealingContext) ctx;
        scheduler.tick();
    }
    private void ok(StateSuperContext ctx){
        final DealingContext dc = (DealingContext) ctx;
    }

    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        Object param = params[0];
        if(Objects.equals("stop",param)){
            if(!desc.stop()){
                ctx.log().println("already stopped");
            }
        }else if(Objects.equals("resume",param)){
            if(!desc.resume()){
                ctx.log().println("was running");
            }
        }else {
            ctx.log().println("unknown cmd:"+param);
        }
    }

    @Override
    public void onDestroy(StateSuperContext ctx) {
        StateHandler.super.onDestroy(ctx);
        desc.destroy();
        desc = null;
        scheduler.destroy();
        scheduler = null;
        for(int i = 0; i< places.length; i++){
            places[i].clear();
            places[i] = null;
        }
        places = null;
    }
}
