package com.chess.landlord._2state.simple;

import com.chess.ChildCallParent;
import com.chess.Write;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public interface ShufflingContext extends StateSuperContext {
    @Write
    @ChildCallParent
    void onShufflingSuccess(StateHandler state);
}
