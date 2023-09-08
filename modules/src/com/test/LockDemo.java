package com.test;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    public void run() {
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition condition1 = reentrantLock.newCondition();
        final Condition condition2 = reentrantLock.newCondition();
        final Condition condition3 = reentrantLock.newCondition();
        final AtomicBoolean run = new AtomicBoolean(true);
        class T extends Thread{
            private final int i;
            private final Condition w;
            private final Condition a;
            private final Lock l;
            T(int i, Condition w, Condition a, Lock l){
                this.i=i;
                this.w=w;
                this.a=a;
                this.l=l;
            }
            @Override
            public void run() {
                final Thread cur = Thread.currentThread();
                while(run.get()){
                    try {
                        l.lock();
                        while(true){
                            try {
                                w.await();
                                break;
                            } catch (InterruptedException ignored) {
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                        System.out.println(i+":"+cur);
                        a.signal();
                    }finally {
                        l.unlock();
                    }
                }
            }
        }
        Thread t1 = new T(1,condition1,condition2,reentrantLock);
        Thread t2 = new T(2,condition2,condition3,reentrantLock);
        Thread t3 = new T(3,condition3,condition1,reentrantLock);
        System.out.println("start threads");
        t1.start();
        t2.start();
        t3.start();

        while(
                Objects.isNull(LockSupport.getBlocker(t1))
                ||Objects.isNull(LockSupport.getBlocker(t2))
                ||Objects.isNull(LockSupport.getBlocker(t3))
        ) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {
            }
        }
        reentrantLock.lock();
        condition1.signal();
        reentrantLock.unlock();
        try {
            System.in.read();
            System.out.println("stop");
            run.set(false);
        } catch (IOException ignored) {
        }
        try {
            t1.join();
            t1.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("done");
    }

    public static void test() {
        Lock l = new ReentrantLock();
        Condition c = l.newCondition();
        try {
            l.lock();
            c.await();
        } catch (InterruptedException e) {
        }finally {
            l.unlock();
        }
    }

    public static void test2() {
        ReentrantLock l = new ReentrantLock();
        while(true){
            if(l.getHoldCount()%100000000 ==0){
                System.out.println(l.getHoldCount());
                System.out.println(Integer.MAX_VALUE- l.getHoldCount());
            }
            l.lock();//Maximum lock count exceeded
        }
    }

    public static void main(String[] args) {
        new LockDemo().run();
    }
}
