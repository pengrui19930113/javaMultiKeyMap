package com.chess.landlord;

import com.chess.landlord._1game.complex.ComplexLandlord;
import com.chess.landlord._1game.Config;
import com.chess.landlord._1game.simple.SimpleLandlord;
import com.chess.landlord._1game.complex.ComplexConfig;
import com.chess.landlord._1game.simple.SimpleConfig;

import java.util.function.Function;
import java.util.function.Supplier;

public enum Type {
    SIMPLE(Type.SIMPLE_VALUE, SimpleConfig::new, SimpleLandlord::new)
    ,COMPLEX(Type.COMPLEX_VALUE, ComplexConfig::new, ComplexLandlord::new);
    public static final int
            SIMPLE_VALUE = 1
            ,COMPLEX_VALUE = 2
            ;
    public final int value;
    public final Supplier<Config> configSupplier;
    public final Function<Config,GeneralLandlord> factory;

    Type(int value, Supplier<Config> configSupplier, Function<Config,GeneralLandlord> factory) {
        this.value = value;
        this.configSupplier = configSupplier;
        this.factory = factory;
    }
}
