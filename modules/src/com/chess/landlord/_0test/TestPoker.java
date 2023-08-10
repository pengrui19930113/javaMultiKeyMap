package com.chess.landlord._0test;

import com.chess.landlord.Poker;
import static com.chess.landlord.Poker.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestPoker {

    public static void test0() {
        for(int i=0;i<1000;i++){
            final byte rand = (byte) ThreadLocalRandom.current().nextInt(_3,__2+1);
            if(rand == __2)
                System.out.println(rand);
        }
    }

    public static void test1() {
        byte b = (byte)BYTE_MASK;
        System.out.println(b);
        System.out.println(b&TYPE_MASK);
        System.out.println(INT_DIAMOND);
    }
    public static void test3() {
        for (Poker poker : NORMAL) {
            System.out.println(poker);
        }
    }

    public static void test4() {
        short s = 0x0ff;
        System.out.println(s);
    }

    public static void test5() {
        List<Poker> pokers = pokerWithStart();
        for (Poker poker : pokers) {
            System.out.println(poker);
        }
    }
    public static void test6() {
        List<Poker> pokers = pokerWithStart();
        Collections.sort(pokers);
        for (Poker poker : pokers) {
            System.out.println(poker);
        }
    }

    public static void main(String[] args) {
//        test3();
        test6();
    }
}
