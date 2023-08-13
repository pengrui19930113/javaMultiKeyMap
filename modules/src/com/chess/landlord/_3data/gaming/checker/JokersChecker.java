package com.chess.landlord._3data.gaming.checker;

import com.chess.NotNull;
import com.chess.landlord.Poker;

import java.util.List;

public class JokersChecker implements Checker{
    @Override
    public boolean check(@NotNull List<Poker> pokers) {
        return pokers.size() == 2
                &&Poker.isJokers(pokers.get(0),pokers.get(1));
    }
}
