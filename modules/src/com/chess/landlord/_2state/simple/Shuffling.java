package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

import java.util.Objects;

public class Shuffling implements StateHandler {
    protected Scheduler scheduler;
    protected Scheduler.Desc desc;
    @Override
    public void onInit(StateSuperContext ctx) {
//        StateHandler.super.onInit(ctx);
        final ShufflingContext sc = (ShufflingContext) ctx;
        scheduler = Scheduler.create();
        desc = scheduler.schedule(()->System.out.println("Shuffling.onInit.update: "+ctx.now()),500,2,800);
    }

    @Override
    public void onTimer(StateSuperContext ctx) {
        final ShufflingContext sc = (ShufflingContext) ctx;
    }

    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        final ShufflingContext sc = (ShufflingContext) ctx;
        if(params.length>0){
            final Object param = params[0];
            if(param.getClass().isAssignableFrom(String.class)){
                if(Objects.equals("next",param)){
                    sc.onShufflingSuccess(this);
                }else{
                    System.out.println("unknown cmd:"+param);
                }
            }else{
                System.out.println(param.getClass()+" value:"+param);
            }
        }else{
            System.out.println("empty params");
        }
    }

    public static void main(String[] args) {
        final Object param = "";
        System.out.println(param.getClass().isAssignableFrom(String.class));
    }
}
