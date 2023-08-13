package com.chess.landlord._1game;

import com.chess.*;

import java.io.PrintStream;
import java.util.Objects;

public interface StateSuperContext {
    @Write
    @Read
    @ChildCallParent
    default void async(Runnable r){
        if(Objects.nonNull(r)){
            new Thread(r).start();
        }
    }
    @Read
    @ChildCallParent
    long last();
    @Read
    @ChildCallParent
    long duration();

    default long now(){return System.currentTimeMillis();}

    default PrintStream log(){ return System.out;}

    default Scheduler scheduler(){ return Scheduler.create();}
    Watchdog watchdog();
}
