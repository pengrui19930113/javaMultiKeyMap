package com.modules.l0;

import com.modules.core.*;
import com.modules.core.Module;
import com.modules.l0.core.CoreConf;
import com.modules.l0.core.MasterProcess;

import java.util.List;
import java.util.Objects;

public class Core implements Module {
    protected final CoreContext ctx;
    protected final Commands commands;
    protected CoreConf coreConf;
    public Core(){
        ctx  = new CoreContext(this::createConf, this::initConf);
        commands = () -> List.of(
//                new Daemon(this)
                new MasterProcess(this)
        );
    }
    protected CoreConf requireCoreConf(Cycle cycle){
        if(Objects.isNull(coreConf)){
            coreConf = new CoreConf();
        }
        return coreConf;
    }
    protected Object createConf(Cycle cycle){
        return requireCoreConf(cycle);
    }
    protected Object initConf(Cycle cycle){
        return NGX_CONF_OK;
    }
    @Override
    public String name() {
        return "core";
    }
    @Override
    public Context ctx() {
        return ctx;
    }
    @Override
    public Commands commands() {
        return commands;
    }
}
