package org.example.gpu;

public class Timer {
    public  static double getTime(){
        return (double) System.nanoTime()/ 1000000000;
    }
    public  static float getFloatTime(){
        return (float) System.nanoTime()/ 1000000000;
    }
}
