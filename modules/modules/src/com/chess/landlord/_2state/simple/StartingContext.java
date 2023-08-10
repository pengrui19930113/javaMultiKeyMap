package com.chess.landlord._2state.simple;

import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

public interface StartingContext extends StateSuperContext {

    void onStartingSuccess(StateHandler state);
}
