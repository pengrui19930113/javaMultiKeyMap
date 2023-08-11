package com.chess.landlord._2state.simple.gaming.checker;

import com.chess.NotNull;
import com.chess.landlord.Poker;

import java.util.List;

public class TrebleChecker implements Checker{
    @Override
    public boolean check(@NotNull List<Poker> pokers) {
        return pokers.size() == 3
                && Poker.isValueAllEquals(pokers.get(0),pokers.get(1),pokers.get(2))
                ;
    }
}
