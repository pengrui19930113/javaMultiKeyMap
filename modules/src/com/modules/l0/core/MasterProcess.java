package com.modules.l0.core;

import com.modules.core.Command;
import com.modules.core.Conf;
import com.modules.core.ThFunction;
import com.modules.l0.Core;

import java.util.List;
import java.util.Objects;

public class MasterProcess implements Command {
    private final Core core;
    public MasterProcess(Core core) {
        this.core = core;
    }

    @Override
    public String name() {
        return "master_process";
    }
    @Override
    public int type() {
        return NGX_MAIN_CONF|NGX_DIRECT_CONF|NGX_CONF_FLAG;
    }
    protected ThFunction<Conf, Command, Object, Object> setHandler = this::setWorkerProcesses;
    protected Object setWorkerProcesses(Conf f, Command c, Object cf){
        final List<String> args = f.args();
        final String value = args.get(1);
        final CoreConf ccf = (CoreConf)cf;
        ccf.setWorkerProcesses(Objects.equals("auto",value)
                ?Runtime.getRuntime().availableProcessors()
                :Integer.parseInt(value));
        return NGX_CONF_OK;
    }
    @Override
    public ThFunction<Conf, Command, Object, Object> set() {
        return setHandler;
    }
}
