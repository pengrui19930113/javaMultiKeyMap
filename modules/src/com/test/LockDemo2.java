package com.test;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo2 {

    public static void main(String[] args) {
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
        Scanner scanner = new Scanner(System.in);
        String line;
        outer:
        while(null!=(line=scanner.nextLine())){
            line=line.trim();
            switch (line){
                case "1"-> {
                    reentrantLock.lock();
                    condition1.signal();
                    reentrantLock.unlock();
                }
                case "2"-> {
                    reentrantLock.lock();
                    condition2.signal();
                    reentrantLock.unlock();
                }
                case "3"-> {
                    reentrantLock.lock();
                    condition3.signal();
                    reentrantLock.unlock();
                }
                case "quit"->{break outer;}
                default ->{
                    System.out.println("unknown:"+line);
                }
            }
        }
        run.set(false);
        try {
            t1.join();
            t1.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("done");
    }
}
