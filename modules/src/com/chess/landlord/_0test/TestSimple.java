package com.chess.landlord._0test;

import com.chess.landlord.GeneralLandlord;
import com.chess.landlord.LandlordFactory;
import com.chess.landlord.Type;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestSimple {
    public static PrintStream log(){
        return System.out;
    }
    static void sleep(long l){
        try {
            TimeUnit.MILLISECONDS.sleep(l<=0?20:l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        final GeneralLandlord game = LandlordFactory.getInstance().create(Type.SIMPLE);
        final AtomicBoolean running = new AtomicBoolean(true);
        final Thread ticker = new Thread(()->{
            log().println("thread start");
            while(running.get()){
                game.onTimer();
                sleep(50);
            }
        });
        ticker.start();
        game.onInit();
        final Scanner scanner = new Scanner(System.in);
        String line;
        log().println("wait type in main");
        while(null!=(line = scanner.nextLine())){
            line = line.trim();
            if(Objects.equals("quit",line)){
                running.set(false);
                break;
            }else if(Objects.equals("info",line)){
                game.info();
            }else{
                game.onAction(line.split("\\s"));
            }
//            log().println("wait type in main");
        }
        log().println("wait thread");
        try {
            ticker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log().println("thread done");
        game.onDestroy();
        log().println("destroy game");
    }

    public static void main2(String[] args) {
        String[] split = "abc\tdef g\nhgl".split("\\s");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
