package com.modules.core;

public interface CoreModule extends Context{

    default Object createConfig(Cycle c){return null;}
    default Object initConfig(Cycle c){return null;}
}
