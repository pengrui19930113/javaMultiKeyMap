package com.chess.landlord._1game;


import com.chess.ParentCallChild;

@ParentCallChild
public interface StateHandler {
    void onTimer(StateSuperContext ctx);
    default void onTimeout(StateSuperContext ctx){}
    default void onAction(StateSuperContext ctx,Object... params){}
    default void onInit(StateSuperContext ctx){}
    default void onDestroy(StateSuperContext ctx){}
}
