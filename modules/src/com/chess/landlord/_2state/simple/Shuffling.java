package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord.Poker;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

import java.util.*;

public class Shuffling implements StateHandler {
    protected Scheduler scheduler;
    protected Scheduler.Desc desc;
    protected List<Poker> pokers;
    @Override
    public void onInit(StateSuperContext ctx) {
//        StateHandler.super.onInit(ctx);
        final ShufflingContext sc = (ShufflingContext) ctx;
        scheduler = ctx.scheduler();
        ctx.log().println("enter shuffling state");
        desc = scheduler.once(()->ctx.log().println("print help to got some information to help yourself"),300);
        pokers = new ArrayList<>(Poker.pokerWithStar());
        Collections.shuffle(pokers);
    }

    @Override
    public void onTimer(StateSuperContext ctx) {
        final ShufflingContext sc = (ShufflingContext) ctx;
        scheduler.tick();
    }
    private void show(StateSuperContext ctx){
        final int LINE_COUNT = 9;
        int i=LINE_COUNT;
        for (Poker poker : pokers) {
            ctx.log().print(poker+" ");
            i--;
            if(0==i){
                i = LINE_COUNT;
                ctx.log().println();
            }
        }
    }
    public List<Poker> result(){
        return new ArrayList<>(pokers);
    }
    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        final ShufflingContext sc = (ShufflingContext) ctx;
        if(params.length>0){
            final Object param = params[0];
            if(param.getClass().isAssignableFrom(String.class)){
                if(Objects.equals("next",param)){
                    ctx.log().println("leaving shuffling");
                    sc.onShufflingSuccess(this);
                }else if(Objects.equals("help",param)){
                    ctx.log().println("help: for got some info to use command line");
                    ctx.log().println("next: go to next state");
                }else if(Objects.equals("reshuffle",param)){
                    Collections.shuffle(pokers);
                    show(ctx);
                }else if(Objects.equals("show",param)){
                    show(ctx);
                }else{
                    ctx.log().println("unknown cmd:"+param+" , please use help");
                }
            }else{
                ctx.log().println(param.getClass()+" value:"+param);
            }
        }else{
            ctx.log().println("empty params");
        }
    }

    @Override
    public void onDestroy(StateSuperContext ctx) {
        desc.destroy();
        desc = null;
        scheduler.destroy();;
        scheduler = null;
        pokers.clear();
        pokers = null;
    }

    public static void main(String[] args) {
        //CharSequence param = "";
        System.out.println(CharSequence.class.isAssignableFrom(String.class));//true
        System.out.println(String.class.isAssignableFrom(CharSequence.class));//false
    }
}
