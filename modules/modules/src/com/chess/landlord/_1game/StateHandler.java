package com.chess.landlord._1game;


import com.chess.ParentCallChild;

import java.util.Arrays;

@ParentCallChild
public interface StateHandler {
    void onTimer(StateSuperContext ctx);
    default void onTimeout(StateSuperContext ctx){}
    default void onAction(StateSuperContext ctx,Object... params){
        System.out.println("****** default start ******");
        System.out.println("ctx:"+ctx);
        System.out.println(Arrays.asList(params));
        System.out.println("****** default start ******");
    }
    default void onInit(StateSuperContext ctx){}
    default void onDestroy(StateSuperContext ctx){}
}
