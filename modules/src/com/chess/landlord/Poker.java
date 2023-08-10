package com.chess.landlord;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Poker implements Comparable<Poker> {
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
    public Poker(byte type, byte value){
        this(type,value,false);
    }
    public Poker(byte type, byte value, boolean start){
        useChar = DEFAULT_USE_CHAR;
        final int intType = BYTE_MASK&type;
        this.value = (byte)(start?(intType|value| STAR_MASK):(intType|value));
    }
    public boolean isSpade(){
        return (value&TYPE_MASK) == SPADE;
    }
    @Override
    public int compareTo(Poker o) {
        return (this.value&VALUE_MASK) -(o.value&VALUE_MASK) ;
    }
    public boolean isStar(){
        return (STAR_MASK &value) == STAR_MASK;
    }
    public boolean isValueEquals(Poker o){
        if(Objects.isNull(o))
            return false;
        return (this.value & VALUE_MASK) == (o.value&VALUE_MASK);
    }
    public boolean isTypeEquals(Poker o){
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
    public static final Poker[] NORMAL = {
            new Poker(SPADE,_3),new Poker(HEART,_3),new Poker(CLUB,_3),new Poker(DIAMOND,_3)
            ,new Poker(SPADE,_4),new Poker(HEART,_4),new Poker(CLUB,_4),new Poker(DIAMOND,_4)
            ,new Poker(SPADE,_5),new Poker(HEART,_5),new Poker(CLUB,_5),new Poker(DIAMOND,_5)
            ,new Poker(SPADE,_6),new Poker(HEART,_6),new Poker(CLUB,_6),new Poker(DIAMOND,_6)
            ,new Poker(SPADE,_7),new Poker(HEART,_7),new Poker(CLUB,_7),new Poker(DIAMOND,_7)
            ,new Poker(SPADE,_8),new Poker(HEART,_8),new Poker(CLUB,_8),new Poker(DIAMOND,_8)
            ,new Poker(SPADE,_9),new Poker(HEART,_9),new Poker(CLUB,_9),new Poker(DIAMOND,_9)
            ,new Poker(SPADE,_10),new Poker(HEART,_10),new Poker(CLUB,_10),new Poker(DIAMOND,_10)
            ,new Poker(SPADE,J),new Poker(HEART,J),new Poker(CLUB,J),new Poker(DIAMOND,J)
            ,new Poker(SPADE,Q),new Poker(HEART,Q),new Poker(CLUB,Q),new Poker(DIAMOND,Q)
            ,new Poker(SPADE,K),new Poker(HEART,K),new Poker(CLUB,K),new Poker(DIAMOND,K)
            ,new Poker(SPADE,A),new Poker(HEART,A),new Poker(CLUB,A),new Poker(DIAMOND,A)
            ,new Poker(SPADE,__2),new Poker(HEART,__2),new Poker(CLUB,__2),new Poker(DIAMOND,__2)
            ,new Poker(JOKER_TYPE,BJ)
            ,new Poker(JOKER_TYPE,RJ)
    };
    public static List<Poker> pokerWithStart(){
        final byte rand = (byte)ThreadLocalRandom.current().nextInt(_3,__2+1);
        return pokerWithStart(rand);
    }
    public static List<Poker> pokerWithStart(byte value){
        if(value<_3||value>__2)
            throw new IllegalArgumentException();
        final List<Poker> pokers = new LinkedList<>(){{
            add(new Poker(JOKER_TYPE,BJ));
            add(new Poker(JOKER_TYPE,RJ));
        }};
        for(int i=_3;i<=__2;i++){
            for(int j=0;j<4;j++){
                if(value != i){
                    pokers.add(new Poker((byte)(j<<TYPE_SHIFT),(byte)i));
                }else{
                    pokers.add(new Poker((byte)(j<<TYPE_SHIFT),(byte)i,true));
                }
            }
        }
        return pokers;
    }
}