package com.chess.landlord;

import com.chess.landlord._1game.Config;

import java.util.Objects;
import java.util.function.Consumer;

public class LandlordFactory {
    private LandlordFactory(){}
    public static LandlordFactory getInstance(){
        return Holder.factory;
    }
    static class Holder{
        static LandlordFactory factory = new LandlordFactory();
    }
    public GeneralLandlord create(Type type){
        return create(type,null);
    }
    public GeneralLandlord create(Type type, Consumer<Config> c){
        if(Objects.isNull(type))
            throw new IllegalStateException();
        final Config config = type.configSupplier.get();
        if(Objects.nonNull(c)){
            c.accept(config);
        }
        return type.factory.apply(config);
    }

}
