package com.modules.core;

import java.util.function.Function;

public interface Module {
    Object NGX_CONF_OK = Const.NGX_CONF_OK;
    Function<Log,Integer> DFT_MASTER = (l)->0;
    Function<Cycle,Integer> DFT_MODULE = (c)->0;
    Function<Cycle,Integer> DFT_PROCESS = (c)->0;
    String name();
    default int version(){ return Const.VERSION;}
    default String signature(){ return Const.SIGNATURE; }
    Context ctx();
    Commands commands();
    default int type(){return Const.NGX_CORE_MODULE;}
    default boolean isCoreModule(){
        return type()==Const.NGX_CORE_MODULE;
    }
    default Function<Log,Integer> initMaster(){ return DFT_MASTER;}
    default Function<Cycle,Integer> initModule(){ return DFT_MODULE;}
    default Function<Cycle,Integer> initProcess(){return DFT_PROCESS;}
}
