package com.chess.landlord._3data.gaming.checker;

import com.chess.landlord.Poker;

import java.util.List;

public interface Checker {
    boolean check(List<Poker> pokers);
    default boolean nearDifferenceIsOne(List<Poker> sorted) {
        for(int i=0;i<sorted.size()-1;i++){
            final Poker smaller = sorted.get(i);
            final Poker bigger = sorted.get(i+1);
            if(!Poker.valuesDiff1(smaller,bigger))
                return false;
        }
        return true;
    }
}
