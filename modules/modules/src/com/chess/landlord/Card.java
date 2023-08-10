package com.chess.landlord;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Card implements Comparable<Card> {
    public static final int TYPE_SHIFT = 6;
    public static final int TYPE_MASK = 0b00_1100_0000;
    public static final int VALUE_MASK = 0b0001_1111;
    public static final int NO_JOKER_VALUE_MASK = 0b0000_1111;
    public static final int STAR_MASK = 0b0010_0000;
    public static final int JOKER_MASK = 0b0001_0000;
    public static final int BYTE_MASK = 0x0ff;
    /**
     * ♠️♥️♣️♦️
     */
    public static final byte SPADE = (byte)(0<<TYPE_SHIFT);
    public static final byte HEART = (byte)(1<<TYPE_SHIFT);
    public static final byte CLUB = (byte)(2<<TYPE_SHIFT);
    public static final byte DIAMOND = (byte)(3<<TYPE_SHIFT);
    public static final int INT_CLUB = BYTE_MASK&CLUB;
    public static final int INT_DIAMOND = BYTE_MASK&DIAMOND;
    public static final byte JOKER_TYPE = 0b0001_0000;
    /*
      8   7   6   5   4   3   2   1
      1. 桃杏梅芳 类型7,8两位
      2. 小王 0b0001_0000 大王 0b0001_1111, 第5位如果是1则是王
   */
    public static final byte
            _3 = 3
            ,_4 = 4
            ,_5 = 5
            ,_6 = 6
            ,_7 = 7
            ,_8 = 8
            ,_9 = 9
            ,_10 = 10
            ,J = 11
            ,Q = 12
            ,K = 13
            ,A = 14
            ,__2 = 15
            ,BJ = 0b0001_0000
            ,RJ = 0b0001_1111
            ;
    public static final boolean DEFAULT_USE_CHAR = false;
    public final byte value;
    protected boolean useChar;
    public Card(byte type,byte value){
        this(type,value,false);
    }
    public Card(byte type,byte value,boolean start){
        useChar = DEFAULT_USE_CHAR;
        final int intType = BYTE_MASK&type;
        this.value = (byte)(start?(intType|value| STAR_MASK):(intType|value));
    }
    public boolean isSpade(){
        return (value&TYPE_MASK) == SPADE;
    }
    @Override
    public int compareTo(Card o) {
        return (this.value&VALUE_MASK) -(o.value&VALUE_MASK) ;
    }
    public boolean isStar(){
        return (STAR_MASK &value) == STAR_MASK;
    }
    public boolean isValueEquals(Card o){
        if(Objects.isNull(o))
            return false;
        return (this.value & VALUE_MASK) == (o.value&VALUE_MASK);
    }
    public boolean isTypeEquals(Card o){
        if(Objects.isNull(o))
            return false;
        return (this.value & TYPE_MASK) == (o.value&TYPE_MASK);
    }
    public boolean isJoker(){
        return (this.value & JOKER_MASK) == JOKER_MASK;
    }
    /**
     * ♠️♥️♣️♦️
     */
    @Override
    public String toString() {
        return toMyString(useChar);
    }

    public String toMyString(boolean useChar) {
        String star = isStar()?"*":"";
        String type;
        String v;
        if(isJoker()){
            final boolean isRj = (value&NO_JOKER_VALUE_MASK) >0;
            type = useChar?"J":"🃏";
            v = isRj?"R":"B";
        }else{
            switch (value&TYPE_MASK){
                case SPADE->type=useChar?"S":"♠️";
                case HEART->type=useChar?"H":"♥️";
                case INT_CLUB->type=useChar?"C":"♣️";
                case INT_DIAMOND->type=useChar?"D":"♦️";
                default -> type="";
            }
            int tmp = value& NO_JOKER_VALUE_MASK;
            if(tmp>=_3&&tmp<=_10){
                v = String.valueOf(tmp);
            }else{
                switch (tmp){
                    case J -> v = "J";
                    case Q -> v = "Q";
                    case K -> v = "K";
                    case A -> v = "A";
                    case __2 -> v = "2";
                    default -> v = "";
                }
            }
        }
        return String.format("%s%s%s",star,type,v);
    }
    public static final Card[] NORMAL = {
            new Card(SPADE,_3),new Card(HEART,_3),new Card(CLUB,_3),new Card(DIAMOND,_3)
            ,new Card(SPADE,_4),new Card(HEART,_4),new Card(CLUB,_4),new Card(DIAMOND,_4)
            ,new Card(SPADE,_5),new Card(HEART,_5),new Card(CLUB,_5),new Card(DIAMOND,_5)
            ,new Card(SPADE,_6),new Card(HEART,_6),new Card(CLUB,_6),new Card(DIAMOND,_6)
            ,new Card(SPADE,_7),new Card(HEART,_7),new Card(CLUB,_7),new Card(DIAMOND,_7)
            ,new Card(SPADE,_8),new Card(HEART,_8),new Card(CLUB,_8),new Card(DIAMOND,_8)
            ,new Card(SPADE,_9),new Card(HEART,_9),new Card(CLUB,_9),new Card(DIAMOND,_9)
            ,new Card(SPADE,_10),new Card(HEART,_10),new Card(CLUB,_10),new Card(DIAMOND,_10)
            ,new Card(SPADE,J),new Card(HEART,J),new Card(CLUB,J),new Card(DIAMOND,J)
            ,new Card(SPADE,Q),new Card(HEART,Q),new Card(CLUB,Q),new Card(DIAMOND,Q)
            ,new Card(SPADE,K),new Card(HEART,K),new Card(CLUB,K),new Card(DIAMOND,K)
            ,new Card(SPADE,A),new Card(HEART,A),new Card(CLUB,A),new Card(DIAMOND,A)
            ,new Card(SPADE,__2),new Card(HEART,__2),new Card(CLUB,__2),new Card(DIAMOND,__2)
            ,new Card(JOKER_TYPE,BJ)
            ,new Card(JOKER_TYPE,RJ)
    };
    public static List<Card> pokerWithStart(){
        final byte rand = (byte)ThreadLocalRandom.current().nextInt(_3,__2+1);
        return pokerWithStart(rand);
    }
    public static List<Card> pokerWithStart(byte value){
        if(value<_3||value>__2)
            throw new IllegalArgumentException();
        final List<Card> cards = new LinkedList<>(){{
            add(new Card(JOKER_TYPE,BJ));
            add(new Card(JOKER_TYPE,RJ));
        }};
        for(int i=_3;i<=__2;i++){
            for(int j=0;j<4;j++){
                if(value != i){
                    cards.add(new Card((byte)(j<<TYPE_SHIFT),(byte)i));
                }else{
                    cards.add(new Card((byte)(j<<TYPE_SHIFT),(byte)i,true));
                }
            }
        }
        return cards;
    }
    public static void test0() {
        for(int i=0;i<1000;i++){
            final byte rand = (byte)ThreadLocalRandom.current().nextInt(_3,__2+1);
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
        for (Card card : NORMAL) {
            System.out.println(card);
        }
    }

    public static void test4() {
       short s = 0x0ff;
        System.out.println(s);
    }

    public static void test5() {
        List<Card> cards = pokerWithStart();
        for (Card card : cards) {
            System.out.println(card);
        }
    }
    public static void test6() {
        List<Card> cards = pokerWithStart();
        Collections.sort(cards);
        for (Card card : cards) {
            System.out.println(card);
        }
    }

    public static void main(String[] args) {
//        test3();
        test6();
    }
}
