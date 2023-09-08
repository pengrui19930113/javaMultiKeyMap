package com.test;

import java.io.IOException;

public class Ring {
    public static void main(String[] args) throws InterruptedException, IOException {
        byte b = 0;
        do {
            b += 1;
            System.out.println(b);
            Thread.sleep(20);//0x80 = -128  //0b10000000    //0b
        } while (System.in.available() <= 0);
    }
}
