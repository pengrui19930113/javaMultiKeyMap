package com.chess;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public interface Watchdog {
    interface Desc{
        default long now(){return System.currentTimeMillis();}
        default PrintStream log(){return System.out;}
    }
    Desc on(Runnable runnable,long interval);
    void off(Desc d);
    void feed(Desc d);
    default long now(){return System.currentTimeMillis();}
    default PrintStream log(){return System.out;}
    static Watchdog create(){
        final WatchdogImpl watchDog = new WatchdogImpl();
        synchronized (Holder.INSTANCES){
            Holder.INSTANCES.add(watchDog);
        }
        return watchDog;
    }
    static boolean start(Watchdog watchDog){
        if(Objects.isNull(watchDog))
            return false;
        if(!Holder.INSTANCES.contains(watchDog))
            return false;
        final WatchdogImpl wd = (WatchdogImpl)watchDog;
        return wd.start();
    }
    static boolean stop(Watchdog watchDog){
        if(Objects.isNull(watchDog))
            return true;
        if(!Holder.INSTANCES.contains(watchDog))
            return true;
        final WatchdogImpl wd = (WatchdogImpl)watchDog;
        return wd.stop();
    }
    static boolean destroy(Watchdog watchDog){
        if(Objects.isNull(watchDog))
            return true;
        boolean exist;
        synchronized (Holder.INSTANCES){
            exist =  Holder.INSTANCES.remove(watchDog);
        }
        if(!exist)
            return true;

        final WatchdogImpl wd = (WatchdogImpl)watchDog;
        return wd.destroy();
    }
    class WatchdogImpl implements Watchdog {
        class DescImpl implements Desc{
            final Runnable runnable;
            final long interval;
            long nextWarningTime;
            DescImpl(Runnable runnable, long interval) {
                this.runnable = runnable;
                this.interval = interval;
                this.nextWarningTime = now()+interval;
            }
            public void tick() {
                long tmp;
                if(Thread.currentThread()==t
                        &&(tmp = now())>=nextWarningTime){
                    Optional.ofNullable(runnable).ifPresent(Runnable::run);
                    nextWarningTime=tmp+interval;
                }
            }
            public void feed() {
                nextWarningTime=now()+interval;
            }
        }
        final Queue<DescImpl> queue;
        Thread t;
        final AtomicBoolean running;
        class Monitor extends Thread{
            final AtomicBoolean running;
            final Queue<DescImpl> queue;
            final long blockTime;
            Monitor(AtomicBoolean running,Queue<DescImpl> queue) {
                this.running = running;
                this.queue = queue;
                blockTime = 20;
            }
            PrintStream log(){return System.out;}
            void block(long l){
                l = l<0?0:l;
                try {
                    Thread.sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace(log());
                }
            }
            @Override
            public void run() {
                while(running.get()){
                    block(blockTime);
                    try{
                        for (DescImpl poll : queue) {
                            Optional.ofNullable(poll).ifPresent(DescImpl::tick);
                        }
                    }catch (Throwable t){
                        t.printStackTrace(log());
                    }
                }
            }
        }
        WatchdogImpl(){
            running = new AtomicBoolean(false);
            queue = new ConcurrentLinkedQueue<>();
        }
        boolean start(){
            if(running.get())
                return false;
            t = new Monitor(running,queue);
            t.start();
            running.set(true);
            return true;
        }
        boolean stop(){
            if(!running.get())
                return false;
            running.set(false);
            while(t.isAlive()){
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace(log());
                }
            }
            return true;
        }
        boolean destroy(){
            stop();
            t = null;
            return Watchdog.destroy(this);
        }
        @Override
        public Desc on(Runnable runnable,long interval) {
            final DescImpl desc = new DescImpl(runnable, interval);
            final boolean offer = queue.offer(desc);
            return offer?desc:null;
        }
        @Override
        public void off(Desc d) {
            queue.remove(d);
        }
        @Override
        public void feed(Desc d) {
            if(d instanceof DescImpl di){
                di.feed();
            }
        }
    }
    class Holder{
        static final List<Watchdog> INSTANCES = new LinkedList<>();
    }
}
