package com.chess;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 分类
 * delay    延迟时间    n<=0:不延迟    ｜n>0:延迟n
 * repeat   重复次数    n<=0:无穷次    ｜1:单次       ｜n>1:多次
* interval  间隔时间    n<=0:不间隔    ｜n>0:间隔n
 */
public interface Scheduler {
    List<Scheduler> MANAGER = new LinkedList<>();
    static Scheduler create(){
        SchedulerImpl scheduler = new SchedulerImpl();
        synchronized (MANAGER){
            MANAGER.add(scheduler);
        }
        return scheduler;
    }
    interface Desc{
        boolean stop();
        boolean resume();
        boolean isStopped();
        default boolean isRunning(){return !isStopped();}
        boolean destroy();
    }

    Desc schedule(Runnable r,long delay,long repeat,long interval,boolean stopped);
    default Desc schedule(Runnable r,long delay,long repeat,long interval){
        return schedule(r,delay,repeat,interval,false);
    }
    default Desc scheduleUpdate(Runnable r,long delay,long interval){
        return schedule(r,delay,-1,interval);
    }
    default Desc scheduleUpdate(Runnable r,long interval){
        return scheduleUpdate(r,0,interval);
    }
    default Desc once(Runnable r,long delay){
        return schedule(r,delay,1,0,false);
    }

    /**
     * 不允许并发调用
     */
    void tick();
    void destroy();
    class SchedulerImpl implements Scheduler{
        protected Queue<DescImpl> queue;
        public SchedulerImpl(){
            queue = new ConcurrentLinkedQueue<>();
        }
        @Override
        public Desc schedule(Runnable r, long delay, long repeat, long interval, boolean stopped) {
            final DescImpl di = new DescImpl(r,delay,repeat,interval,stopped);
            boolean offer = queue.offer(di);
            if(!offer){
                System.out.println("warning queue.offer failure");
            }
            return di;
        }
        @Override
        public void tick() {
            DescImpl ref;
            int max = 100;
            Iterator<DescImpl> iterator = queue.iterator();
            while(iterator.hasNext()){
                ref = iterator.next();
                max--;
                ref.tickAndTryRemove();
                if(max<=0)
                    break;
            }

        }

        @Override
        public void destroy() {
            synchronized (MANAGER){
                MANAGER.remove(this);
            }
        }

        protected boolean remove(DescImpl di){
            return queue.remove(di);
        }
        protected long now(){return System.currentTimeMillis();}
        protected class DescImpl implements Desc{
            protected final Runnable runnable;
            protected final long delay;
            protected final long repeat;
            protected final long interval;
            protected boolean stopped;
            protected long lastStopTime;
            protected long lastResumeTime;
            protected boolean destroyed;
            protected long nextRunningTime;
            protected long runningTimes;
            public DescImpl(Runnable r, long delay, long repeat, long interval, boolean stopped){
                runnable = r;
                this.delay=delay<0?0:delay;
                this.repeat=repeat<0?0:repeat;
                this.interval=interval<0?0:interval;
                this.stopped=stopped;
                this.destroyed = false;
                runningTimes = 0;
                nextRunningTime = now()+delay;
            }
            @Override
            public boolean stop() {
                if(stopped)
                    return false;
                else{
                    stopped = true;
                    lastStopTime = now();
                }
                return true;
            }
            @Override
            public boolean resume() {
                if(!stopped)
                    return false;
                else{
                    stopped = false;
                    lastResumeTime = now();
                }
                return true;
            }
            @Override
            public boolean isStopped() {
                return stopped;
            }
            @Override
            public boolean destroy() {
                if(destroyed)
                    return false;
                final boolean res = SchedulerImpl.this.remove(this);
                if(!res){
                    System.out.println("warning non exists");
                }
                destroyed = true;
                return true;
            }
            public void tickAndTryRemove(){
                if(stopped)
                    return;
                final long tmp = now();
                if(tmp>0){
                    if(tmp>=nextRunningTime){
                        runnable.run();
                        runningTimes++;
                        nextRunningTime = (interval==0?tmp:(nextRunningTime+interval));
                    }
                }else{
                    throw new IllegalStateException();
                }
                if(repeat == runningTimes
                        && runningTimes>0){
                    destroy();
                }
            }

        }
    }

}
