package com.chess.landlord._2state.simple;

import com.chess.Final;
import com.chess.Scheduler;
import com.chess.landlord._1game.StateSuperContext;
import com.chess.landlord._1game.StateHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Landing implements StateHandler {
    @Final
    protected long initTime;
    @Final
    protected int sizeOfChooser;
    @Final
    protected final int defaultCancelTimes = 2;
    protected Scheduler scheduler;
    protected Boolean[] chooses;
    protected int cancelTimes;
    protected int result;
    @Override
    public void onInit(StateSuperContext ctx) {
        final LandingContext lc = (LandingContext)ctx;
        ctx.log().println("enter landing state");
        sizeOfChooser = 3;
        result = -2;
        cancelTimes = defaultCancelTimes;
        initTime = lc.last();
        scheduler = ctx.scheduler();
        chooses = new Boolean[sizeOfChooser];
    }
    @Override
    public void onTimer(StateSuperContext ctx) {
        final LandingContext lc = (LandingContext)ctx;
        scheduler.tick();;
    }
    private int cmd(LandingContext ctx,Object action){
        int cmd;
        if(Objects.equals("yes",action)){
            cmd = 0;
        }else if(Objects.equals("no",action)){
            cmd = 1;
        }else{
            ctx.log().println("unknown action:"+action+",must be [yes|no]");
            return -1;
        }
        return cmd;
    }
    private int who(LandingContext ctx,Object who){
        int idx;
        if(Objects.equals("one",who)){
            idx = 0;
        }else if(Objects.equals("two",who)) {
            idx = 1;
        }else if(Objects.equals("three",who)) {
            idx = 2;
        }else{
            ctx.log().println("unknown player:"+who+",must be [one|two|three]");
            return -1;
        }
        return idx;
    }
    private boolean allFilled(LandingContext ctx,int idx) {
        for (Boolean choose : chooses) {//someone no chosen
            if(Objects.isNull(choose)){
                ctx.log().printf("%s chosen %s,ready for other players action\n",idx,chooses[idx]);
                return false;
            }
        }
        return true;
    }
    private int allFalse(LandingContext ctx){
        for (Boolean choose : chooses) {//someone no chosen
            if(Objects.isNull(choose)){
                return -1;
            }
            if(choose) return 1;
        }
        return 0;
    }
    /**
     * 抢
     * 不抢
     * @param ctx
     * @param params
     */
    @Override
    public void onAction(StateSuperContext ctx, Object... params) {
        final LandingContext lc = (LandingContext)ctx;
        Object scene = params[0];
        if(Objects.equals("landing",scene)){
            if(params.length<3){
                ctx.log().println("landing cmd length must more then equals 3");
                return;
            }
            final Object who = params[1];
            final int idx = who(lc,who);
            if(-1==idx)return;
            if(Objects.nonNull(chooses[idx])){
                ctx.log().printf("player %s already chosen %s%n",who,chooses[idx]?"yes":"no");
                return;
            }
            final Object action = params[2];
            final int cmd = cmd(lc,action);
            if(-1==cmd)return;
            chooses[idx] = (cmd==0);
            ctx.log().printf("player %s chosen %s%n",idx,chooses[idx]);
            if(!allFilled(lc,idx)) return;//wait for other player's action
            final int res = allFalse(lc);
            if(-1==res)return;
            final boolean allFalse = (0==res);
            if(allFalse){
                if(0 == cancelTimes){
                    ctx.log().printf("default cancel times:%s all consumed,will reshuffling\n",defaultCancelTimes);
                    lc.onLandingFailure(this);
                }else{
//                chooses = null;
                    chooses = new Boolean[sizeOfChooser];
                    cancelTimes--;
                    ctx.log().println("cancel times --");
                }
            }else{
                final List<Integer> yesIndices = new ArrayList<>();
                for(int i=0;i<chooses.length;i++)
                    if(chooses[i]) yesIndices.add(i);
                final int index = ThreadLocalRandom.current()
                        .nextInt(yesIndices.size());
                result = yesIndices.get(index);
                ctx.log().println("result:"+result);
                lc.onLandingSuccess(this);
            }
        }else{
            ctx.log().println("unknown cmd:"+Arrays.asList(params));
        }
    }

    @Override
    public void onDestroy(StateSuperContext ctx) {
        scheduler.destroy();
        scheduler = null;
        chooses = null;
    }

    public static void main(String[] args) {
        int i = ThreadLocalRandom.current().nextInt(0);//bound must be positive
        System.out.println(i);
    }
}
