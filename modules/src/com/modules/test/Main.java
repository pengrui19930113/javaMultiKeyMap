package com.modules.test;

import com.modules.Modules;
import com.modules.core.CoreModule;
import com.modules.core.Cycle;
import com.modules.core.Module;

import java.io.*;
import java.util.Objects;

public class Main {
    public static void test(String[] args) throws IOException {
        System.out.println((int)'\s');
        System.out.println((int)'a');
        System.out.println((int)'\r');
        System.out.println((int)'\n');
        System.out.println("Hello world!");
        InputStream is = Main.class.getResourceAsStream("text.txt");
        int data;
        while(-1!=(data = is.read())){
            System.out.println(data);
        }
        is.close();
    }

    public static void main(String[] args) {
        Cycle cycle = new Cycle(){};
        final Modules instance = Modules.getInstance();
        for (Module module : instance.modules()) {
            if(!module.isCoreModule())
                continue;
            final CoreModule cm = (CoreModule)module.ctx();
            if(Objects.nonNull(cm)) {
                cm.createConfig(cycle);
            }
        }
        for (Module module : instance.modules()) {
            if(!module.isCoreModule())
                continue;
            final CoreModule cm = (CoreModule)module.ctx();
            if(Objects.nonNull(cm)) {
                cm.initConfig(cycle);
            }
        }
    }
}