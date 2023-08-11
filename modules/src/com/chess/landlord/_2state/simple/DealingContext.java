package com.chess.landlord._2state.simple;

import com.chess.ChildCallParent;
import com.chess.ParentCallChild;
import com.chess.Write;
import com.chess.landlord.Poker;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;

import java.util.List;

public interface DealingContext extends StateSuperContext {

    @Write
    @ChildCallParent
    void onDealingSuccess(StateHandler state);

    @Write
    @ChildCallParent
    List<Poker> shuffledPokers();
}
