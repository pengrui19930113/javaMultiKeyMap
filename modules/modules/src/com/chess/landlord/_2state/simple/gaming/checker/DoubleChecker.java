package com.chess.landlord._2state.simple.gaming.checker;

import com.chess.NotNull;
import com.chess.landlord.Poker;

import java.util.List;

public class DoubleChecker implements Checker{
    @Override
    public boolean check(@NotNull List<Poker> pokers) {
        return pokers.size() == 2
                && pokers.get(0).isValueEquals(pokers.get(1));
    }
}
