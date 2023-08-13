package com.chess;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Threading {
    static PrintStream log(){
        return System.out;
    }
    static long now(){
        return System.currentTimeMillis();
    }
    static void sleep(long s){
        try {
            Thread.sleep(s>0?s:0);
        } catch (InterruptedException e) {
            e.printStackTrace(log());
        }
    }
    public static void main(String[] args) {
        final AtomicBoolean running = new AtomicBoolean(true);
        final Thread main = Thread.currentThread();
        final Thread t = new Thread(()->{
            while(running.get()){
                log().println(now());
                sleep(5000);
            }
        });
        final Thread input = new Thread(()->{
            final Scanner scanner = new Scanner(System.in);
            String line;
            outer:
            while(null!=(line=scanner.nextLine())){
                line = line.trim();
                switch (line){
                    case "main"->{main.interrupt();
                        System.out.println("input:main state is "+main.isInterrupted());}
                    case "quit"->{
                        running.set(false);
                        break outer;
                    }
                    default -> {
                        log().println(Thread.interrupted());
                        log().println("unknown:"+line);
                    }
                }
            }
        });
        t.start();
        input.start();
        log().println("joined t");
        while(t.isAlive()){
            log().println("main wait join t");
            try {
                t.join();
            } catch (InterruptedException e) {
                log().println("main interrupted");
                log().println("main isInterrupted:"+main.isInterrupted());
//                main.isInterrupted()
                while(Thread.currentThread().isInterrupted()){//just read flag
                    sleep(1000);
                    log().println("not clear");
                }
            }
        }
        log().println("joined input");
        while (input.isAlive()){
            log().println("main wait joined input");
            try {
                input.join();
            } catch (InterruptedException e) {
                e.printStackTrace(log());
            }
        }
        log().println("joined input done");

    }
}
