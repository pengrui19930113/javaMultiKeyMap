package com.chess.landlord._2state.simple.gaming.checker;

import com.chess.NotNull;
import com.chess.landlord.Poker;

import java.util.ArrayList;
import java.util.List;

public class DoubleSequenceChecker implements Checker{
    @Override
    public boolean check(@NotNull List<Poker> sorted) {
        final int size = sorted.size();
        return  size>=6 && size<= 20 && interNearDifferenceIsOne(sorted);
    }
    protected boolean interNearDifferenceIsOne(List<Poker> sorted) {
        final int size = sorted.size();
        final List<Poker> one = new ArrayList<>(size/2);
        final List<Poker> two = new ArrayList<>(size/2);
        for(int i=0;i<size;i++){
            final List<Poker> ref = i%2==0?one:two;
            ref.add(sorted.get(i));
        }
        return nearDifferenceIsOne(one)&&nearDifferenceIsOne(two);
    }

    public static void main(String[] args) {
        final List<Poker> pokers = List.of(
                new Poker(Poker.SPADE, Poker._3), new Poker(Poker.DIAMOND, Poker._3)
                , new Poker(Poker.SPADE, Poker._4), new Poker(Poker.DIAMOND, Poker._4)
                , new Poker(Poker.SPADE, Poker._5), new Poker(Poker.DIAMOND, Poker._5)
                , new Poker(Poker.SPADE, Poker._6), new Poker(Poker.DIAMOND, Poker._6)
        );
        final boolean check = new DoubleSequenceChecker().check(pokers);
        System.out.println(check);
    }
}
