package com.modules;

import com.modules.core.Module;
import com.modules.l0.Core;

import java.util.Collections;
import java.util.List;

public class Modules {

    protected List<Module> list;
    private Modules(){
        list = List.of(
                new Core()
        );
    }
    public List<Module> modules(){
        return Collections.unmodifiableList(list);
    }

    public static Modules getInstance(){
        return Holder.i;
    }
    static class Holder{
        static Modules i = new Modules();
    }
}
