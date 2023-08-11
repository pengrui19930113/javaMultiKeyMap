package com.chess.landlord._2state.simple.gaming.combo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/*
    3*3+3*1
    3*3+3*2

    4*3 + 4*1 = 15
    4*3 + 4*2 = 20
    0b01000000
 */
public enum ComboType {
    JOKERS(ComboType.J_NO,0x4fffffff)
    ,BOOM(ComboType.B_NO,0x40000000,ComboType.JOKERS)
    ,SINGLE(ComboType.S_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,DOUBLE(ComboType.D_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE(ComboType.T_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE_ONE(ComboType.TO_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE_TWO(ComboType.TT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,DOUBLE_TREBLE(ComboType.DT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,DOUBLE_TREBLE_ONE(ComboType.DTO_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,DOUBLE_TREBLE_TWO(ComboType.DTT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE_TREBLE(ComboType.TTR_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE_TREBLE_ONE(ComboType.TTO_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,TREBLE_TREBLE_TWO(ComboType.TTT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,QUADRUPL_TREBLE(ComboType.QT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,QUADRUPL_TREBLE_ONE(ComboType.QTO_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,QUADRUPL_TREBLE_TWO(ComboType.QTT_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,SEQUENCE(ComboType.SE_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ,DOUBLE_SEQUENCE(ComboType.DS_NO,0xff,ComboType.BOOM,ComboType.JOKERS)
    ;
    public final int value;
    public final int feature;
    public final Set<ComboType> matcher;
    public static final int
            J_NO = 101
        , B_NO = 100
        , S_NO = 1
        , D_NO = 2
        , T_NO = 3
        , TO_NO = 4
        , TT_NO = 5
        , DT_NO = 6
        , DTO_NO = 7
        , DTT_NO = 8
        , TTR_NO = 9
        , TTO_NO = 10
        , TTT_NO = 11
        , QT_NO = 12
        , QTO_NO = 13
        , QTT_NO = 14
        , SE_NO = 15
        , DS_NO = 16
    ;
    ComboType(int value,int feature,ComboType... match) {
        ComboType[] ct = new ComboType[match.length+1];
        ct[0] = this;
        System.arraycopy(match,0,ct,1,match.length);
        this.matcher = Collections.unmodifiableSet(Arrays.stream(ct).collect(Collectors.toSet()));
        this.value = value;
        this.feature = feature;
    }
}
