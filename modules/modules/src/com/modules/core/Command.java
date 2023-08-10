package com.modules.core;


public interface Command {
    int NGX_MAIN_CONF = 0x01000000;
    int NGX_DIRECT_CONF = 0x00010000;
    int NGX_CONF_FLAG = 0x00000200;

    Object NGX_CONF_OK = Const.NGX_CONF_OK;
    String name();
    int type();
    ThFunction<Conf,Command,Object,Object> set();
    default Object post(){return null;}
}
