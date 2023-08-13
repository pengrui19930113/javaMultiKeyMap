package com.chess.landlord._2state.simple.gaming;

import com.chess.landlord.Poker;

import java.util.List;

public interface GamingInitData{
    List<Poker> one();

    List<Poker> two();

    List<Poker> three();
    List<Poker> goal();

    int landIndex();
}
