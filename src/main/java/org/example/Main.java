package org.example;

import org.example.gpu.gameProcess.trest;

public class Main {
    public  static boolean fullscreen = true;
    public  static boolean hacks = false;
    public static void main(String[] args) {
        for (String s : args){
           switch (s){
               case "-window":
                   fullscreen = false;
                   break;
               case "-hacks":
                   hacks = true;
                   break;

           }
        }
            new trest();

    }
}

// -fullscreen  полный экран
// -hacks