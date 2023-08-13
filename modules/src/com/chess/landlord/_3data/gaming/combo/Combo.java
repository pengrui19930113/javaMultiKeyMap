package com.chess.landlord._3data.gaming.combo;

import java.util.Objects;

public interface Combo {

    ComboType type();
    default boolean match(Combo pre){
        return Objects.requireNonNull(pre)
            .type().matcher.contains(this.type());}


    static void test(){
        Combo pre = () -> ComboType.JOKERS;
        //client put <combo,pokers>
        //checker user really have the pokers then pokers
        //checker pokers really match the combo
    }
}
