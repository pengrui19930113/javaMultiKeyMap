package com.chess.landlord._1game.simple;

import com.chess.landlord._1game.Config;

public class DemoSubLandlord extends SimpleLandlord {
    public DemoSubLandlord(Config c) {
        super(c);
        Object obj = super.data;
        //如果吧改类移出 com.chess.landlord._1game.simple; 则上一行代码报错
    }

    void demo(char n){
        switch (n){
            case 'a':
            case 'b':
                System.out.println("a b");
                break;
            default:
                System.out.println("unknown");
        }
        //same as upon
        switch (n) {
            case 'a', 'b' -> System.out.println("a b");
            default -> System.out.println("unknown");
        }
    }
}
