package com.chess.landlord._1game;


import com.chess.ParentCallChild;

import java.util.Arrays;

@ParentCallChild
public interface StateHandler {
    void onTimer(StateSuperContext ctx);
    default void onTimeout(StateSuperContext ctx){}
    default void onAction(StateSuperContext ctx,Object... params){
        ctx.log().println("****** default start ******");
        ctx.log().println("ctx:"+ctx);
        ctx.log().println(Arrays.asList(params));
        ctx.log().println("****** default start ******");
    }
    default void onInit(StateSuperContext ctx){}
    default void onDestroy(StateSuperContext ctx){}
}
