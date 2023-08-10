package com.modules.l0;

import com.modules.core.Context;
import com.modules.core.CoreModule;
import com.modules.core.Cycle;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CoreContext implements CoreModule {

    protected final Function<Cycle,Object> createHandler;
    protected final Function<Cycle,Object> initHandler;

    public CoreContext(Function<Cycle, Object> create
            , Function<Cycle, Object> init) {
        this.createHandler = create;
        this.initHandler = init;
    }

    @Override
    public String name() {
        return "core";
    }

    public Object createConfig(Cycle c){
        return createHandler.apply(c);
    }
    public Object initConfig(Cycle c){
        return initHandler.apply(c);
    }
}
