package com.chess.landlord._2state.simple;

import com.chess.Scheduler;
import com.chess.landlord.Poker;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;
import com.chess.landlord._2state.simple.gaming.GamingInitData;
import com.chess.landlord._2state.simple.gaming.GamingLogicData;
import com.chess.landlord._2state.simple.gaming.LandlordPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gaming implements StateHandler {
    protected boolean gameOver;
    protected Scheduler scheduler;
    protected GamingLogicData logicData;
    @Override
    public void onInit(StateSuperContext ctx) {
        ctx.log().println("enter gaming state");
        final GamingContext cc = (GamingContext)ctx;
        final GamingLogicData ld = new GamingLogicData();
        logicData = ld;
        final GamingInitData gamingData = cc.gamingData();
        LandlordPlayer player;
        player= new LandlordPlayer();
        player.inHands = new ArrayList<>(gamingData.one());
        ld.players.add(player);
        player= new LandlordPlayer();
        player.inHands = new ArrayList<>(gamingData.two());
        ld.players.add(player);
        player= new LandlordPlayer();
        player.inHands = new ArrayList<>(gamingData.three());
        ld.players.add(player);
        final List<Poker> goal = gamingData.goal();
        final int index = gamingData.landIndex();
        final List<LandlordPlayer> players = ld.players;
        for(int i=0;i<players.size();i++){
            final LandlordPlayer curr = players.get(i);
            curr.isLand = i==index;
            curr.active = curr.isLand;
            if(curr.isLand){
                curr.inHands.addAll(goal);
                logicData.curActive = curr;
            }
        }
        for (LandlordPlayer lp : players) {
            Collections.sort(lp.inHands);
        }
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

    }
    void planOne(){
        //gaming info
        //gaming action one 0
        //gaming action two 2|33
        //gaming action three 0
        //gaming action one 0
        //gaming action two 3|333
        //gaming action three 3|777
        //gaming action one 3|999
        //gaming action two 0
        //gaming action three 3|999
        //gaming action one 4|1|4443
        //gaming action two 4|2|8888
        //gaming action three 0
        //gaming action one 0
        //gaming action two 5|1|3_4_5_6_7
        //gaming action three 5|1|10_J_Q_K_A
        //gaming action one 0
        //gaming action two 0
        //gaming action three 5|2|10_J_Q_K_A
    }
    void planTwo(){

    }
    @Override
    public void onDestroy(StateSuperContext ctx) {
        scheduler.destroy();
        scheduler = null;
        logicData = null;
    }
}
