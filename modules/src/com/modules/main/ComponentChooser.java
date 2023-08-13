package com.modules.main;

import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

public class ComponentChooser {

    public ComponentChooser run(Map<String,String> envVariables, Properties vmPotions, String... programArgs){
        final PrintStream log = System.out;
        log.println("env start:");
        log.println(envVariables.get("VAR1"));
        log.println(envVariables.get("VAR2"));
        log.println("vm options start:");
        log.println(vmPotions.get("MY_VM_OP"));
        log.println("program args start:");
        for (String programArg : programArgs) {
            log.print(programArg+" ");
        }
        log.println();
        return this;
    }
    public ComponentChooser sync(){
        return this;
    }
    public static void main(String[] args) {

        ComponentChooser sync = new ComponentChooser()
                .run(System.getenv(),System.getProperties(),args)
                .sync();
    }
}
