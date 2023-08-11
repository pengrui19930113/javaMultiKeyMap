package com.chess.landlord._1game.simple;

import com.chess.landlord.Poker;

import java.util.List;

class Data {
    public Data init(Object... params) {

        return this;
    }
    /*洗牌后的结果*/
    public List<Poker> shuffledPokers;
    /*发牌后的结果*/
    public List<Poker> one;
    public List<Poker> two;
    public List<Poker> three;
    public List<Poker> goal;
    public int landIndex;

}
