package com.chess.landlord._0test;

import com.chess.landlord.GeneralLandlord;
import com.chess.landlord.LandlordFactory;
import com.chess.landlord.Type;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestSimple {

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
            System.out.println("thread start");
            while(running.get()){
                game.onTimer();
                sleep(50);
            }
        });
        ticker.start();
        game.onInit();
        final Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println("wait type in main");
        while(null!=(line = scanner.nextLine())){
            line = line.trim();
            if(Objects.equals("quit",line)){
                running.set(false);
                break;
            }else if(Objects.equals("info",line)){
                game.info();
            }else{
                game.onAction(line);
            }
            System.out.println("wait type in main");
        }
        System.out.println("wait thread");
        try {
            ticker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread done");
        game.onDestroy();
        System.out.println("destroy game");
    }
}
