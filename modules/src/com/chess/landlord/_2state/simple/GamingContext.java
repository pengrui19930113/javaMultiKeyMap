package com.chess.landlord._2state.simple;

import com.chess.ChildCallParent;
import com.chess.Write;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;
import com.chess.landlord._2state.simple.gaming.GamingInitData;

public interface GamingContext extends StateSuperContext {
    @Write
    @ChildCallParent
    void onGamingOver(StateHandler state);
    GamingInitData gamingData();
}
