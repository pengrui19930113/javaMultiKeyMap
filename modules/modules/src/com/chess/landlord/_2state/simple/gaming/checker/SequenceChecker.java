package com.chess.landlord._2state.simple.gaming.checker;

import com.chess.NotNull;
import com.chess.landlord.Poker;
import com.chess.landlord._2state.simple.gaming.combo.Sequence;

import java.util.List;

public class SequenceChecker implements Checker{
    @Override
    public boolean check(@NotNull List<Poker> sorted) {
        final int size = sorted.size();
        return  size>=Sequence.minSize() && size<= Sequence.maxSize()
                && nearDifferenceIsOne(sorted);
    }
}
